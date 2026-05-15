import axios from 'axios';
import { useAuthStore } from '@/stores/authStore';
import router from '@/router';

const instance = axios.create({
    baseURL: process.env.VITE_API_URL,
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
    },
});

// Standard auth endpoints that don't require specific headers or can be accessed without full context
const EXCLUDED_PATHS = [
    '/auth/login',
    '/auth/login/vietnamese-id',
    '/auth/register',
    '/auth/register/student',
    '/auth/register/teacher',
    '/auth/refresh-token',
    '/auth/logout',
    '/auth/forgot-password',
    '/auth/reset-password',
    '/images'
];

instance.interceptors.request.use(
    (config) => {
        // 1. JWT Handling
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        
        // 2. Language Header
        const language = localStorage.getItem('language') || 'vi';
        config.headers['Accept-Language'] = language;
        
        return config;
    },
    (error) => Promise.reject(error)
);

instance.interceptors.response.use(
    (response) => {
        return response;
    },
    async (error) => {
        // Handle 401 - Unauthorized
        if (error.response?.status === 401) {
            const authStore = useAuthStore();
            
            // Don't retry if it's a refresh token request or auth endpoints
            const isAuthRequest = error.config.url?.includes('/auth/') || 
                                error.config.url?.includes('/refresh-token') ||
                                error.config.url?.includes('/logout');
            
            if (!isAuthRequest && !authStore.isRefreshing) {
                // Try to refresh the token
                const refreshed = await authStore.refreshAccessToken();
                if (refreshed) {
                    // Retry the original request with new token
                    const newToken = localStorage.getItem('accessToken');
                    error.config.headers.Authorization = `Bearer ${newToken}`;
                    return instance.request(error.config);
                }
            }
            
            // If refresh failed or it's an auth request, logout
            authStore.logout();
            router.push({ name: 'login' });
        }
        
        return Promise.reject(error);
    }
);

export default instance;
