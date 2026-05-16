package com.educollege.finance.service;

import com.educollege.finance.enums.PaymentStatus;
import com.educollege.finance.enums.TransactionStatus;
import com.educollege.finance.model.PaymentTransaction;
import com.educollege.finance.model.TuitionFee;
import com.educollege.finance.repository.PaymentTransactionRepository;
import com.educollege.finance.repository.TuitionFeeRepository;
import com.educollege.core.logging.VietnameseAcademicAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Vietnamese Payment Gateway Service
 * Integrates with Vietnamese payment gateways (VNPay, MoMo, ZaloPay, etc.)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VietnamesePaymentGatewayService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final TuitionFeeRepository tuitionFeeRepository;
    private final VietnameseAcademicAuditService auditService;

    @Value("${payment.vnpay.tmnCode:}")
    private String vnpayTmnCode;

    @Value("${payment.vnpay.hashSecret:}")
    private String vnpayHashSecret;

    @Value("${payment.vnpay.apiUrl:}")
    private String vnpayApiUrl;

    @Value("${payment.momo.partnerCode:}")
    private String momoPartnerCode;

    @Value("${payment.momo.accessKey:}")
    private String momoAccessKey;

    @Value("${payment.momo.secretKey:}")
    private String momoSecretKey;

    @Value("${payment.momo.apiUrl:}")
    private String momoApiUrl;

    /**
     * Create payment request for VNPay
     */
    public Map<String, String> createVNPayPayment(Long tuitionFeeId, BigDecimal amount, String returnUrl) {
        log.info("Creating VNPay payment request: tuitionFeeId={}, amount={}", tuitionFeeId, amount);

        String txnRef = generateTransactionReference();

        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", vnpayTmnCode);
        params.put("vnp_Amount", amount.multiply(new BigDecimal("100")).toString()); // Convert to VND cents
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", txnRef);
        params.put("vnp_OrderInfo", "Thanh toan hoc phi - " + txnRef);
        params.put("vnp_OrderType", "billpayment");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", returnUrl);
        params.put("vnp_IpAddr", "127.0.0.1");
        params.put("vnp_CreateDate", LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        // Generate secure hash
        String secureHash = generateVNPaySecureHash(params);
        params.put("vnp_SecureHash", secureHash);
        params.put("vnp_SecureHashType", "HmacSHA512");

        // Build payment URL
        String paymentUrl = buildVNPayPaymentUrl(params);

        // Get tuition fee
        TuitionFee tuitionFee = tuitionFeeRepository.findById(tuitionFeeId)
            .orElseThrow(() -> new RuntimeException("Tuition fee not found"));

        // Save transaction record
        PaymentTransaction transaction = PaymentTransaction.builder()
            .tuitionFee(tuitionFee)
            .amount(amount)
            .transactionDate(LocalDateTime.now())
            .paymentMethod("VNPAY")
            .referenceNumber(txnRef)
            .status(TransactionStatus.PENDING)
            .build();
        paymentTransactionRepository.save(transaction);

        auditService.logTuitionPayment(transaction.getId(), null, null, amount.doubleValue(), "VNPAY", "PENDING");

        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", paymentUrl);
        response.put("transactionId", transaction.getId().toString());
        response.put("txnRef", txnRef);

        return response;
    }

    /**
     * Create payment request for MoMo
     */
    public Map<String, String> createMoMoPayment(Long tuitionFeeId, BigDecimal amount, String returnUrl) {
        log.info("Creating MoMo payment request: tuitionFeeId={}, amount={}", tuitionFeeId, amount);

        String requestId = generateRequestId();
        String orderId = generateOrderId(tuitionFeeId);

        Map<String, String> params = new HashMap<>();
        params.put("partnerCode", momoPartnerCode);
        params.put("accessKey", momoAccessKey);
        params.put("requestId", requestId);
        params.put("amount", amount.toString());
        params.put("orderId", orderId);
        params.put("orderInfo", "Thanh toan hoc phi - " + orderId);
        params.put("returnUrl", returnUrl);
        params.put("notifyUrl", returnUrl + "/notify");
        params.put("extraData", "");

        // Generate signature
        String signature = generateMoMoSignature(params);
        params.put("signature", signature);

        // Get tuition fee
        TuitionFee tuitionFee = tuitionFeeRepository.findById(tuitionFeeId)
            .orElseThrow(() -> new RuntimeException("Tuition fee not found"));

        // Save transaction record
        PaymentTransaction transaction = PaymentTransaction.builder()
            .tuitionFee(tuitionFee)
            .amount(amount)
            .transactionDate(LocalDateTime.now())
            .paymentMethod("MOMO")
            .referenceNumber(orderId)
            .status(TransactionStatus.PENDING)
            .build();
        paymentTransactionRepository.save(transaction);

        auditService.logTuitionPayment(transaction.getId(), null, null, amount.doubleValue(), "MOMO", "PENDING");

        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", momoApiUrl);
        response.put("transactionId", transaction.getId().toString());
        response.put("orderId", orderId);

        return response;
    }

    /**
     * Process VNPay callback
     */
    @Transactional
    public Map<String, Object> processVNPayCallback(Map<String, String> params) {
        log.info("Processing VNPay callback: params={}", params);

        String vnp_TxnRef = params.get("vnp_TxnRef");
        String vnp_ResponseCode = params.get("vnp_ResponseCode");

        // Verify secure hash
        if (!verifyVNPaySecureHash(params)) {
            log.error("Invalid VNPay secure hash");
            return Map.of("status", "INVALID_SIGNATURE");
        }

        // Find transaction by reference number
        Optional<PaymentTransaction> transactionOpt = paymentTransactionRepository.findAll().stream()
            .filter(t -> vnp_TxnRef.equals(t.getReferenceNumber()))
            .findFirst();
        
        PaymentTransaction transaction = transactionOpt
            .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Check if already processed
        if (transaction.getStatus() != TransactionStatus.PENDING) {
            return Map.of("status", "ALREADY_PROCESSED");
        }

        // Update transaction status
        boolean success = "00".equals(vnp_ResponseCode);
        transaction.setStatus(success ? TransactionStatus.SUCCESS : TransactionStatus.FAILED);
        transaction.setTransactionDate(LocalDateTime.now());
        paymentTransactionRepository.save(transaction);

        // Update tuition fee if payment successful
        if (success) {
            updateTuitionFeePaidAmount(transaction.getTuitionFee().getId(), transaction.getAmount());
        }

        auditService.logTuitionPayment(transaction.getId(), null, null, transaction.getAmount().doubleValue(), 
            "VNPAY", success ? "SUCCESS" : "FAILED");

        return Map.of(
            "status", success ? "SUCCESS" : "FAILED",
            "transactionId", transaction.getId(),
            "responseCode", vnp_ResponseCode
        );
    }

    /**
     * Process MoMo callback
     */
    @Transactional
    public Map<String, Object> processMoMoCallback(Map<String, String> params) {
        log.info("Processing MoMo callback: params={}", params);

        String orderId = params.get("orderId");
        String resultCode = params.get("resultCode");

        // Verify signature
        if (!verifyMoMoSignature(params)) {
            log.error("Invalid MoMo signature");
            return Map.of("status", "INVALID_SIGNATURE");
        }

        // Find transaction by reference number
        Optional<PaymentTransaction> transactionOpt = paymentTransactionRepository.findAll().stream()
            .filter(t -> orderId.equals(t.getReferenceNumber()))
            .findFirst();
        
        PaymentTransaction transaction = transactionOpt
            .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Check if already processed
        if (transaction.getStatus() != TransactionStatus.PENDING) {
            return Map.of("status", "ALREADY_PROCESSED");
        }

        // Update transaction status
        boolean success = "0".equals(resultCode);
        transaction.setStatus(success ? TransactionStatus.SUCCESS : TransactionStatus.FAILED);
        transaction.setTransactionDate(LocalDateTime.now());
        paymentTransactionRepository.save(transaction);

        // Update tuition fee if payment successful
        if (success) {
            updateTuitionFeePaidAmount(transaction.getTuitionFee().getId(), transaction.getAmount());
        }

        auditService.logTuitionPayment(transaction.getId(), null, null, transaction.getAmount().doubleValue(), 
            "MOMO", success ? "SUCCESS" : "FAILED");

        return Map.of(
            "status", success ? "SUCCESS" : "FAILED",
            "transactionId", transaction.getId(),
            "resultCode", resultCode
        );
    }

    /**
     * Check payment status
     */
    public Map<String, Object> checkPaymentStatus(Long transactionId) {
        PaymentTransaction transaction = paymentTransactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));

        return Map.of(
            "transactionId", transaction.getId(),
            "status", transaction.getStatus().name(),
            "amount", transaction.getAmount(),
            "paymentMethod", transaction.getPaymentMethod(),
            "referenceNumber", transaction.getReferenceNumber(),
            "transactionDate", transaction.getTransactionDate()
        );
    }

    /**
     * Refund payment
     */
    @Transactional
    public Map<String, Object> refundPayment(Long transactionId, String reason) {
        PaymentTransaction transaction = paymentTransactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getStatus() != TransactionStatus.SUCCESS) {
            throw new RuntimeException("Cannot refund non-successful transaction");
        }

        // Update transaction status to REFUNDED
        transaction.setStatus(TransactionStatus.REFUNDED);
        transaction.setTransactionDate(LocalDateTime.now());
        paymentTransactionRepository.save(transaction);

        // Deduct from tuition fee paid amount
        TuitionFee tuitionFee = transaction.getTuitionFee();
        tuitionFee.setPaidAmount(tuitionFee.getPaidAmount().subtract(transaction.getAmount()));
        tuitionFee.setStatus(tuitionFee.getPaidAmount().compareTo(tuitionFee.getTotalAmount()) >= 0 ? PaymentStatus.PAID : PaymentStatus.PARTIAL);
        tuitionFeeRepository.save(tuitionFee);

        auditService.logTuitionPayment(transaction.getId(), null, null, transaction.getAmount().doubleValue(), 
            transaction.getPaymentMethod() + "_REFUND", "SUCCESS");

        return Map.of(
            "status", "SUCCESS",
            "transactionId", transaction.getId(),
            "refundAmount", transaction.getAmount()
        );
    }

    private String generateTransactionReference() {
        return "EDU" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateRequestId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    private String generateOrderId(Long tuitionFeeId) {
        return "EDU" + tuitionFeeId + System.currentTimeMillis();
    }

    private String generateVNPaySecureHash(Map<String, String> params) {
        // In production, implement proper HMACSHA512 hashing
        // This is a simplified version for demonstration
        return "SIMULATED_HASH_" + params.hashCode();
    }

    private boolean verifyVNPaySecureHash(Map<String, String> params) {
        // In production, implement proper hash verification
        return true;
    }

    private String buildVNPayPaymentUrl(Map<String, String> params) {
        StringBuilder url = new StringBuilder(vnpayApiUrl);
        url.append("?");
        params.forEach((key, value) -> {
            url.append(key).append("=").append(value).append("&");
        });
        // Remove trailing &
        if (url.length() > 0 && url.charAt(url.length() - 1) == '&') {
            url.setLength(url.length() - 1);
        }
        return url.toString();
    }

    private String generateMoMoSignature(Map<String, String> params) {
        // In production, implement proper HMACSHA256 hashing
        return "SIMULATED_MOMO_SIGNATURE_" + params.hashCode();
    }

    private boolean verifyMoMoSignature(Map<String, String> params) {
        // In production, implement proper signature verification
        return true;
    }

    private void updateTuitionFeePaidAmount(Long tuitionFeeId, BigDecimal amount) {
        TuitionFee tuitionFee = tuitionFeeRepository.findById(tuitionFeeId)
            .orElseThrow(() -> new RuntimeException("Tuition fee not found"));

        tuitionFee.setPaidAmount(tuitionFee.getPaidAmount().add(amount));
        tuitionFee.setStatus(tuitionFee.getPaidAmount().compareTo(tuitionFee.getTotalAmount()) >= 0 ? PaymentStatus.PAID : PaymentStatus.PARTIAL);
        tuitionFeeRepository.save(tuitionFee);
    }
}
