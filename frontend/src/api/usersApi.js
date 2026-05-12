// ✅ Đúng — dùng instance đã setup baseURL và interceptor
import axios from '@/plugins/axios';
import { useAuthStore } from '@/stores/authStore';
import { getUserIdFromJWT, debugJWT } from '@/utils/jwtHelper';
export const usersApi = {
    login(params) {
        return axios.post('/api/v1/auth/login', params);
    },
    register(params) {
        return axios.post('/api/v1/users/register', params);
    },
    refreshToken(params) {
        return axios.post('/api/v1/auth/refresh', params);
    },
    logout() {
        return axios.post('/api/v1/auth/logout');
    },
    // Check availability
    checkUsername(username) {
        return axios.get(`/api/v1/users/check-username/${username}`);
    },
    checkEmail(email) {
        return axios.get(`/api/v1/users/check-email/${email}`);
    },
    checkIdKey(idKey) {
        return axios.get(`/api/v1/users/check-id-key/${idKey}`);
    },
    // User management
    getUserStats() {
        return axios.get('/api/v1/users/stats');
    },
    getUserByUsername(username) {
        return axios.get(`/api/v1/users/username/${username}`);
    },
    getProfile() {
        // Use new profile endpoint
        return axios.get('/api/v1/profiles/me');
    },
    updateProfile(params) {
        return axios.put('/api/v1/profiles/me', params);
    },
    // 1. Update Basic Info Only
    updateBasicInfo(params) {
        return axios.put('/api/v1/profiles/me/basic', params);
    },
    // 2. Update Academic Info Only
    updateAcademicInfo(params) {
        return axios.put('/api/v1/profiles/me/academic', params);
    },
    // 3. Update Professional Info Only
    updateProfessionalInfo(params) {
        return axios.put('/api/v1/profiles/me/professional', params);
    },
    updateAvatar(formData) {
        return axios.put('/api/v1/profiles/me/avatar', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
    },
    changePassword(params) {
        return axios.post('/auth/change-password', params);
    },
    updateTenantLogo(formData) {
        return axios.put('/v1/tenant/logo', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
    },
};
