package com.educollege.finance.service;

import com.educollege.finance.model.TuitionFee;
import com.educollege.finance.repository.TuitionFeeRepository;
import com.educollege.finance.enums.PaymentStatus;
import com.educollege.user.model.Student;
import com.educollege.user.repository.StudentRepository;
import com.educollege.academic.model.Semester;
import com.educollege.academic.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Tuition Fee Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TuitionFeeService {
    
    private final TuitionFeeRepository tuitionFeeRepository;
    private final StudentRepository studentRepository;
    private final SemesterRepository semesterRepository;
    
    public TuitionFee createTuitionFee(TuitionFee tuitionFee) {
        System.out.println("Creating tuition fee");
        
        Student student = studentRepository.findById(tuitionFee.getStudent().getId())
            .orElseThrow(() -> new RuntimeException("Student not found with id: " + tuitionFee.getStudent().getId()));
        
        Semester semester = semesterRepository.findById(tuitionFee.getSemester().getId())
            .orElseThrow(() -> new RuntimeException("Semester not found with id: " + tuitionFee.getSemester().getId()));
        
        // Check if tuition fee already exists for this student and semester
        if (tuitionFeeRepository.existsByStudentIdAndSemesterId(student.getId(), semester.getId())) {
            throw new RuntimeException("Tuition fee already exists for this student and semester");
        }
        
        tuitionFee.setStudent(student);
        tuitionFee.setSemester(semester);
        tuitionFee.setPaidAmount(BigDecimal.ZERO);
        tuitionFee.setStatus(PaymentStatus.UNPAID);
        
        TuitionFee savedTuitionFee = tuitionFeeRepository.save(tuitionFee);
        System.out.println("Tuition fee created successfully");
        return savedTuitionFee;
    }
    
    public TuitionFee updateTuitionFee(Long id, TuitionFee tuitionFee) {
        System.out.println("Updating tuition fee with id: " + id);
        
        TuitionFee existingTuitionFee = tuitionFeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tuition fee not found with id: " + id));
        
        if (tuitionFee.getStudent() != null && 
            !existingTuitionFee.getStudent().getId().equals(tuitionFee.getStudent().getId())) {
            Student student = studentRepository.findById(tuitionFee.getStudent().getId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + tuitionFee.getStudent().getId()));
            existingTuitionFee.setStudent(student);
        }
        
        if (tuitionFee.getSemester() != null && 
            !existingTuitionFee.getSemester().getId().equals(tuitionFee.getSemester().getId())) {
            Semester semester = semesterRepository.findById(tuitionFee.getSemester().getId())
                .orElseThrow(() -> new RuntimeException("Semester not found with id: " + tuitionFee.getSemester().getId()));
            existingTuitionFee.setSemester(semester);
        }
        
        existingTuitionFee.setTotalAmount(tuitionFee.getTotalAmount());
        existingTuitionFee.setPaidAmount(tuitionFee.getPaidAmount());
        existingTuitionFee.setDueDate(tuitionFee.getDueDate());
        
        // Update status based on payment
        updatePaymentStatus(existingTuitionFee);
        
        TuitionFee updatedTuitionFee = tuitionFeeRepository.save(existingTuitionFee);
        System.out.println("Tuition fee updated successfully");
        return updatedTuitionFee;
    }
    
    public void deleteTuitionFee(Long id) {
        System.out.println("Deleting tuition fee with id: " + id);
        
        TuitionFee tuitionFee = tuitionFeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tuition fee not found with id: " + id));
        
        tuitionFeeRepository.delete(tuitionFee);
        System.out.println("Tuition fee deleted successfully");
    }
    
    @Transactional(readOnly = true)
    public Optional<TuitionFee> getTuitionFeeById(Long id) {
        return tuitionFeeRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<TuitionFee> getAllTuitionFees() {
        return tuitionFeeRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<TuitionFee> getTuitionFeesByStudentId(Long studentId) {
        return tuitionFeeRepository.findByStudentId(studentId);
    }
    
    @Transactional(readOnly = true)
    public List<TuitionFee> getTuitionFeesBySemesterId(Long semesterId) {
        return tuitionFeeRepository.findBySemesterId(semesterId);
    }
    
    @Transactional(readOnly = true)
    public Optional<TuitionFee> getTuitionFeeByStudentIdAndSemesterId(Long studentId, Long semesterId) {
        return tuitionFeeRepository.findByStudentIdAndSemesterId(studentId, semesterId);
    }
    
    @Transactional(readOnly = true)
    public List<TuitionFee> getTuitionFeesByStatus(PaymentStatus status) {
        return tuitionFeeRepository.findByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public List<TuitionFee> getOverdueTuitionFees() {
        LocalDate today = LocalDate.now();
        return tuitionFeeRepository.findByDueDateBeforeAndStatus(today, PaymentStatus.UNPAID);
    }
    
    @Transactional(readOnly = true)
    public List<TuitionFee> getTuitionFeesByDueDateRange(LocalDate startDate, LocalDate endDate) {
        return tuitionFeeRepository.findByDueDateBetween(startDate, endDate);
    }
    
    public TuitionFee recordPayment(Long id, BigDecimal amount) {
        System.out.println("Recording payment for tuition fee with id: " + id + ", amount: " + amount);
        
        TuitionFee tuitionFee = tuitionFeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tuition fee not found with id: " + id));
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Payment amount must be positive");
        }
        
        BigDecimal newPaidAmount = tuitionFee.getPaidAmount().add(amount);
        if (newPaidAmount.compareTo(tuitionFee.getTotalAmount()) > 0) {
            throw new RuntimeException("Payment amount exceeds total tuition fee");
        }
        
        tuitionFee.setPaidAmount(newPaidAmount);
        updatePaymentStatus(tuitionFee);
        
        TuitionFee updatedTuitionFee = tuitionFeeRepository.save(tuitionFee);
        System.out.println("Payment recorded successfully");
        return updatedTuitionFee;
    }
    
    public TuitionFee markAsOverdue(Long id) {
        System.out.println("Marking tuition fee as overdue with id: " + id);
        
        TuitionFee tuitionFee = tuitionFeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tuition fee not found with id: " + id));
        
        tuitionFee.setStatus(PaymentStatus.OVERDUE);
        
        TuitionFee updatedTuitionFee = tuitionFeeRepository.save(tuitionFee);
        System.out.println("Tuition fee marked as overdue successfully");
        return updatedTuitionFee;
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getTotalOutstandingByStudentId(Long studentId) {
        List<TuitionFee> tuitionFees = tuitionFeeRepository.findByStudentId(studentId);
        return tuitionFees.stream()
            .filter(tf -> tf.getStatus() != PaymentStatus.PAID)
            .map(tf -> tf.getTotalAmount().subtract(tf.getPaidAmount()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getTotalPaidByStudentId(Long studentId) {
        List<TuitionFee> tuitionFees = tuitionFeeRepository.findByStudentId(studentId);
        return tuitionFees.stream()
            .map(TuitionFee::getPaidAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Transactional(readOnly = true)
    public long countTuitionFeesByStatus(PaymentStatus status) {
        return tuitionFeeRepository.countByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public boolean existsByStudentIdAndSemesterId(Long studentId, Long semesterId) {
        return tuitionFeeRepository.existsByStudentIdAndSemesterId(studentId, semesterId);
    }
    
    private void updatePaymentStatus(TuitionFee tuitionFee) {
        if (tuitionFee.getPaidAmount().compareTo(tuitionFee.getTotalAmount()) >= 0) {
            tuitionFee.setStatus(PaymentStatus.PAID);
        } else if (tuitionFee.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
            tuitionFee.setStatus(PaymentStatus.PARTIAL);
        } else {
            tuitionFee.setStatus(PaymentStatus.UNPAID);
        }
    }
}
