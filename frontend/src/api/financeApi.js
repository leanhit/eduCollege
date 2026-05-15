import axios from '@/plugins/axios';

export const financeApi = {
    // Tuition Fees
    getStudentFees(studentId) {
        return axios.get(`/api/v1/finance/fees/student/${studentId}`);
    },
    getFeeDetails(feeId) {
        return axios.get(`/api/v1/finance/fees/${feeId}`);
    },

    // Payments
    processPayment(data) {
        // data: { tuitionFeeId, amount, method, reference }
        return axios.post('/api/v1/finance/payments/pay', null, { params: data });
    },
    getPaymentHistory(studentId) {
        return axios.get(`/api/v1/finance/payments/history/student/${studentId}`);
    },

    // Statistics (if needed)
    getFinancialSummary(studentId) {
        return axios.get(`/api/v1/finance/summary/student/${studentId}`);
    }
};

export default financeApi;
