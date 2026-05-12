import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import router from '@/router'
import { usersApi } from '@/api/usersApi'
import { useGatewayTenantStore } from './tenant/gateway/myTenantStore'
import { tenantApi } from '@/api/tenantApi'
import axios from '@/plugins/axios'
// Import constants from tenant store (giống frontend)
const TENANT_DATA = 'tenant_data'
const ACTIVE_TENANT_ID = 'active_tenant_id'
export const useAuthStore = defineStore('auth', () => {
  // State
  const user = ref(null)
  const token = ref(localStorage.getItem('accessToken') || null)
  const refreshToken = ref(localStorage.getItem('refreshToken') || null)
  const isLoading = ref(false)
  const error = ref(null)
  const isRefreshing = ref(false)
  // Getters
  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.systemRole === 'ADMIN')
  const userId = computed(() => user.value?.id)
  const currentUser = computed(() => user.value)
  // Actions
  /**
   * Khởi tạo trạng thái Auth (Được gọi từ main.ts)
   * Đọc token và user từ localStorage để khôi phục phiên làm việc
   */
  const initialize = () => {
    const savedToken = localStorage.getItem('accessToken')
    const savedRefreshToken = localStorage.getItem('refreshToken')
    const savedUser = localStorage.getItem('user')
    if (savedToken) {
      token.value = savedToken
    }
    if (savedRefreshToken) {
      refreshToken.value = savedRefreshToken
    }
    if (savedUser) {
      try {
        user.value = JSON.parse(savedUser)
      } catch (e) {
        localStorage.removeItem('user')
      }
    }
  }
  /**
   * Xử lý đăng nhập thành công
   */
  const login = async (authData) => {
    token.value = authData.token
    refreshToken.value = authData.refreshToken || null
    // Backend returns username, role, fullName - create user object
    user.value = {
      id: null, // Will be fetched later
      username: authData.username,
      email: authData.username, // Backend uses username as identifier
      role: authData.role,
      fullName: authData.fullName,
      systemRole: authData.role
    }
    localStorage.setItem('accessToken', authData.token)
    if (authData.refreshToken) {
      localStorage.setItem('refreshToken', authData.refreshToken)
    }
    localStorage.setItem('user', JSON.stringify(user.value))
  }
  /**
   * Đăng nhập với credentials
   */
  const loginWithCredentials = async (credentials) => {
    isLoading.value = true
    error.value = null
    try {
      // 1. Gọi API Login
      const res = await usersApi.login(credentials)
      // Backend returns { success: true, data: {token: "...", username: "..."} }
      const response = res.data
      if (!response.success || !response.data || !response.data.token) {
        throw new Error("No token received")
      }
      const authData = response.data
      // 2. Lưu token và thông tin user vào Store & LocalStorage
      await login(authData)
      
      // EduCollege: Skip tenant logic, go directly to dashboard
      console.log('Login - EduCollege backend, skipping tenant logic')
      await router.push('/dashboard')
      return { success: true, data: authData }
    } catch (err) {
      const message = err.response?.data?.message || err.message || 'Login failed'
      error.value = message
      return { success: false, error: message }
    } finally {
      isLoading.value = false
    }
  }
  /**
   * Đăng ký tài khoản mới (EduCollege Backend)
   */
  const register = async (userData) => {
    isLoading.value = true
    error.value = null
    try {
      const response = await usersApi.register(userData)
      
      // EduCollege backend returns: { success: true, message: "...", data: {...} }
      if (response.data.success) {
        // Registration successful, but don't auto-login
        // Let user login manually after registration
        return { 
          success: true, 
          data: response.data.data,
          message: response.data.message 
        }
      } else {
        throw new Error(response.data.message || 'Registration failed')
      }
    } catch (err) {
      const message = err.response?.data?.message || err.message || 'Registration failed'
      error.value = message
      return { success: false, error: message }
    } finally {
      isLoading.value = false
    }
  }
  /**
   * Refresh access token
   */
  const refreshAccessToken = async () => {
    if (!refreshToken.value || isRefreshing.value) {
      return false
    }

    isRefreshing.value = true
    try {
      const response = await usersApi.refreshToken({ refreshToken: refreshToken.value })
      const authData = response.data
      
      // Update tokens
      token.value = authData.accessToken
      refreshToken.value = authData.refreshToken
      localStorage.setItem('accessToken', authData.accessToken)
      localStorage.setItem('refreshToken', authData.refreshToken)
      
      return true
    } catch (error) {
      console.error('Refresh token failed:', error)
      // Refresh token expired or invalid - logout
      logout()
      return false
    } finally {
      isRefreshing.value = false
    }
  }

  /**
   * Đăng xuất và dọn dẹp dữ liệu
   */
  const logout = async () => {
    try {
      // Call backend logout if we have a token
      if (token.value) {
        await usersApi.logout()
      }
    } catch (error) {
      console.error('Logout API call failed:', error)
    } finally {
      const tenantStore = useGatewayTenantStore()
      
      // Clear ALL tenant data first
      tenantStore.clearTenant()
      tenantStore.userTenants = [] // Clear tenant list in memory
      
      // Clear ALL auth data
      token.value = null
      refreshToken.value = null
      user.value = null
      
      // Clear ALL localStorage data
      localStorage.removeItem('accessToken')
      localStorage.removeItem('refreshToken')
      localStorage.removeItem('user')
      localStorage.removeItem(ACTIVE_TENANT_ID)
      localStorage.removeItem(TENANT_DATA)
      
      // Prevent tenant store from hydrating old data on next login
      localStorage.setItem('should_hydrate_tenant', 'false')
      
      console.log('Logout completed - all data cleared, hydrate disabled')
      
      // Redirect to login
      await router.push({ name: 'login' })
    }
  }
  /**
   * Lấy lại thông tin user profile từ Backend
   */
  const fetchUser = async () => {
    if (!token.value) return
    isLoading.value = true
    try {
      const response = await usersApi.getProfile()
      user.value = response.data
      localStorage.setItem('user', JSON.stringify(response.data))
      return response
    } catch (error) {
      // Don't logout on 400/404 errors, only on auth errors (401/403)
      if (error.response?.status === 401 || error.response?.status === 403) {
        logout()
      }
      // For other errors, just log but don't logout
      throw error
    } finally {
      isLoading.value = false
    }
  }
  /**
   * Cập nhật thông tin user cục bộ (ví dụ đổi avatar, đổi tên)
   */
  const updateAuthUser = (updates) => {
    if (!user.value) return
    user.value = { ...user.value, ...updates }
    localStorage.setItem('user', JSON.stringify(user.value))
  }
  return {
    // State
    user,
    token,
    refreshToken,
    isLoading,
    error,
    isRefreshing,
    // Getters
    isLoggedIn,
    isAdmin,
    userId,
    currentUser,
    // Actions
    initialize,
    login,
    loginWithCredentials,
    register,
    refreshAccessToken,
    logout,
    fetchUser,
    updateAuthUser
  }
})
