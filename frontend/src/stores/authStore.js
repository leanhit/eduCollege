import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import router from '@/router'
import { usersApi } from '@/api/usersApi'
import websocketService from '@/services/websocketService'

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
  const isAdmin = computed(() => user.value?.role === 'ADMIN')
  const userId = computed(() => user.value?.id)
  const currentUser = computed(() => user.value)

  // Actions
  const initialize = () => {
    const savedToken = localStorage.getItem('accessToken')
    const savedRefreshToken = localStorage.getItem('refreshToken')
    const savedUser = localStorage.getItem('user')
    
    if (savedToken) token.value = savedToken
    if (savedRefreshToken) refreshToken.value = savedRefreshToken
    if (savedUser) {
      try {
        user.value = JSON.parse(savedUser)
      } catch (e) {
        localStorage.removeItem('user')
      }
    }
  }

  const login = async (authData) => {
    token.value = authData.token
    refreshToken.value = authData.refreshToken
    user.value = authData.user
    
    localStorage.setItem('accessToken', authData.token)
    localStorage.setItem('refreshToken', authData.refreshToken)
    localStorage.setItem('user', JSON.stringify(authData.user))
  }

  const loginWithCredentials = async (credentials) => {
    isLoading.value = true
    error.value = null
    try {
      const res = await usersApi.login(credentials)
      const authData = res.data
      
      if (!authData.token) {
        throw new Error("No token received")
      }
      
      await login(authData)
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

  const register = async (userData) => {
    isLoading.value = true
    error.value = null
    try {
      let response
      if (userData.academicLevel) {
        response = await usersApi.registerStudent(userData)
      } else if (userData.academicTitle) {
        response = await usersApi.registerTeacher(userData)
      } else {
        response = await usersApi.register(userData)
      }
      
      const authData = response.data
      if (!authData.token) {
        throw new Error("No token received")
      }
      
      await login(authData)
      await router.push('/dashboard')
      
      return { success: true, data: authData }
    } catch (err) {
      const message = err.response?.data?.message || err.message || 'Registration failed'
      error.value = message
      return { success: false, error: message }
    } finally {
      isLoading.value = false
    }
  }

  const refreshAccessToken = async () => {
    if (!refreshToken.value || isRefreshing.value) {
      return false
    }

    isRefreshing.value = true
    try {
      const response = await usersApi.refreshToken({ refreshToken: refreshToken.value })
      const authData = response.data
      
      token.value = authData.accessToken
      refreshToken.value = authData.refreshToken
      localStorage.setItem('accessToken', authData.accessToken)
      localStorage.setItem('refreshToken', authData.refreshToken)
      
      return true
    } catch (error) {
      console.error('Refresh token failed:', error)
      logout()
      return false
    } finally {
      isRefreshing.value = false
    }
  }

  const logout = async () => {
    try {
      if (token.value) {
        await usersApi.logout()
      }
    } catch (error) {
      console.error('Logout API call failed:', error)
    } finally {
      websocketService.disconnect()
      
      token.value = null
      refreshToken.value = null
      user.value = null
      
      localStorage.removeItem('accessToken')
      localStorage.removeItem('refreshToken')
      localStorage.removeItem('user')
      localStorage.removeItem('active_tenant_id')
      localStorage.removeItem('tenant_data')
      
      await router.push({ name: 'login' })
    }
  }

  const fetchUser = async () => {
    if (!token.value) return
    isLoading.value = true
    try {
      const response = await usersApi.getProfile()
      user.value = response.data
      localStorage.setItem('user', JSON.stringify(response.data))
      return response
    } catch (error) {
      if (error.response?.status === 401 || error.response?.status === 403) {
        logout()
      }
      throw error
    } finally {
      isLoading.value = false
    }
  }

  const updateAuthUser = (updates) => {
    if (!user.value) return
    user.value = { ...user.value, ...updates }
    localStorage.setItem('user', JSON.stringify(user.value))
  }

  return {
    user,
    token,
    refreshToken,
    isLoading,
    error,
    isRefreshing,
    isLoggedIn,
    isAdmin,
    userId,
    currentUser,
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
