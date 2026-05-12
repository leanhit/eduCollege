<template>
  <div class="min-h-screen bg-gray-50 dark:bg-gray-900 py-8">
    <!-- Loading Overlay -->
    <div v-if="loading" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white dark:bg-gray-800 rounded-lg p-6 flex items-center space-x-3">
        <Icon icon="mdi:loading" class="h-6 w-6 animate-spin text-primary" />
        <span class="text-gray-900 dark:text-white">Loading profile...</span>
      </div>
    </div>

    <div class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- Header -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900 dark:text-white">{{ $t('profile.title') }}</h1>
        <p class="mt-2 text-gray-600 dark:text-gray-400">{{ $t('profile.subtitle') }}</p>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <!-- Sidebar - Avatar & Basic Info -->
        <div class="lg:col-span-1">
          <div class="bg-white dark:bg-gray-800 shadow rounded-lg p-6">
            <!-- Avatar Section -->
            <div class="text-center mb-6">
              <div class="relative inline-block">
                <img
                  :src="userAvatar"
                  class="mx-auto h-32 w-32 rounded-full object-cover ring-4 ring-white dark:ring-gray-600 shadow-lg"
                  alt="User Avatar"
                  @error="handleAvatarError"
                />
                <button
                  @click="triggerAvatarUpload"
                  :disabled="avatarUploading"
                  class="absolute bottom-0 right-0 bg-primary text-white p-2 rounded-full shadow-lg hover:bg-primary/80 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                  :title="avatarUploading ? 'Uploading...' : 'Update Avatar'"
                >
                  <Icon v-if="!avatarUploading" icon="mdi:camera" class="h-4 w-4" />
                  <Icon v-else icon="mdi:loading" class="h-4 w-4 animate-spin" />
                </button>
              </div>
              <h2 class="mt-4 text-xl font-semibold text-gray-900 dark:text-white">
                {{ userFullName }}
              </h2>
              <p class="mt-1 text-sm text-gray-500 dark:text-gray-400">
                {{ userEmail }}
              </p>
              <!-- Role Badges -->
              <div class="mt-3 flex justify-center gap-2">
                <span :class="getRoleBadgeClass()">
                  {{ getRoleLabel() }}
                </span>
                <span v-if="userProfile?.jobTitle" class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200">
                  <Icon icon="mdi:briefcase" class="mr-1 h-3 w-3" />
                  {{ userProfile.jobTitle }}
                </span>
              </div>
            </div>

            <!-- Academic Details -->
            <div class="space-y-3 border-t dark:border-gray-700 pt-4">
              <div v-if="userProfile?.department" class="flex items-center justify-center text-sm text-gray-600 dark:text-gray-400">
                <Icon icon="mdi:office-building" class="mr-2 h-4 w-4" />
                {{ userProfile.department }}
              </div>
              <div v-if="userProfile?.major" class="flex items-center justify-center text-sm text-gray-600 dark:text-gray-400">
                <Icon icon="mdi:school" class="mr-2 h-4 w-4" />
                {{ userProfile.major }}
              </div>
              <div v-if="userProfile?.yearOfStudy" class="flex items-center justify-center text-sm text-gray-600 dark:text-gray-400">
                <Icon icon="mdi:calendar" class="mr-2 h-4 w-4" />
                {{ userProfile.yearOfStudy }}
              </div>
              <div v-if="userProfile?.gpa" class="flex items-center justify-center text-sm text-gray-600 dark:text-gray-400">
                <Icon icon="mdi:star" class="mr-2 h-4 w-4" />
                GPA: {{ userProfile.gpa }}
              </div>
            </div>

            <!-- Location Display -->
            <div v-if="userLocation !== 'Not set'" class="flex items-center justify-center text-sm text-gray-600 dark:text-gray-400">
              <Icon icon="mdi:map-marker" class="mr-2 h-4 w-4" />
              <span>{{ userLocation }}</span>
            </div>

            <!-- Social Links -->
            <div v-if="hasSocialLinks" class="mt-4 pt-4 border-t dark:border-gray-700">
              <div class="flex justify-center space-x-2">
                <button
                  v-if="userProfile?.linkedInUrl"
                  @click="openLink(userProfile.linkedInUrl)"
                  class="p-2 text-gray-400 hover:text-blue-600 transition-colors"
                  title="LinkedIn"
                >
                  <Icon icon="mdi:linkedin" class="h-5 w-5" />
                </button>
                <button
                  v-if="userProfile?.personalWebsite"
                  @click="openLink(userProfile.personalWebsite)"
                  class="p-2 text-gray-400 hover:text-gray-600 transition-colors"
                  title="Website"
                >
                  <Icon icon="mdi:web" class="h-5 w-5" />
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Main Content - Tabbed Interface -->
        <div class="lg:col-span-2">
          <div class="bg-white dark:bg-gray-800 shadow rounded-lg">
            <div class="p-6">
              <!-- Tab Navigation -->
              <div class="border-b border-gray-200 dark:border-gray-700">
                <nav class="-mb-px flex space-x-8">
                  <button
                    v-for="tab in tabs"
                    :key="tab.id"
                    @click="activeTab = tab.id"
                    :class="[
                      activeTab === tab.id
                        ? 'border-primary text-primary'
                        : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300 dark:text-gray-400 dark:hover:text-gray-300',
                      'whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm transition-colors duration-200'
                    ]"
                  >
                    <span class="flex items-center">
                      <Icon :icon="tab.icon" class="h-4 w-4 mr-2" />
                      {{ tab.label }}
                    </span>
                  </button>
                </nav>
              </div>

              <!-- Tab Content -->
              <div class="mt-6">
                <!-- Basic Info Tab -->
                <div v-if="activeTab === 'basic'" class="space-y-6">
                  <div class="bg-white dark:bg-gray-800 shadow rounded-lg p-6">
                    <div class="flex justify-between items-center mb-6">
                      <h3 class="text-lg font-medium text-gray-900 dark:text-white">{{ $t('profile.basicInfo') }}</h3>
                      <button
                        @click="handleEditBasic"
                        :disabled="loading"
                        class="inline-flex items-center px-3 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary dark:bg-gray-700 dark:text-gray-300 dark:border-gray-600 dark:hover:bg-gray-600 disabled:opacity-50 disabled:cursor-not-allowed"
                      >
                        <Icon v-if="!loading" icon="mdi:pencil" class="h-4 w-4 mr-2" />
                        <Icon v-else icon="mdi:loading" class="h-4 w-4 mr-2 animate-spin" />
                        {{ loading ? 'Loading...' : 'Edit' }}
                      </button>
                    </div>
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                      <!-- Left Column -->
                      <div class="space-y-4">
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.firstName') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ userProfile.firstName || 'Not provided' }}</p>
                        </div>
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.lastName') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ userProfile.lastName || 'Not provided' }}</p>
                        </div>
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.phoneNumber') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ userProfile.phoneNumber || 'Not provided' }}</p>
                        </div>
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.alternateEmail') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ userProfile.alternateEmail || 'Not provided' }}</p>
                        </div>
                      </div>
                      <!-- Right Column -->
                      <div class="space-y-4">
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.dateOfBirth') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ formatDate(userProfile.dateOfBirth) || 'Not provided' }}</p>
                        </div>
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.gender') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ formatGender(userProfile.gender) || 'Not provided' }}</p>
                        </div>
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.bio') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ userProfile.bio || 'Not provided' }}</p>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Academic Info Tab -->
                <div v-if="activeTab === 'academic'" class="space-y-6">
                  <div class="bg-white dark:bg-gray-800 shadow rounded-lg p-6">
                    <div class="flex justify-between items-center mb-6">
                      <h3 class="text-lg font-medium text-gray-900 dark:text-white">{{ $t('profile.academicInfo') }}</h3>
                      <button
                        @click="handleEditAcademic"
                        :disabled="loading"
                        class="inline-flex items-center px-3 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary dark:bg-gray-700 dark:text-gray-300 dark:border-gray-600 dark:hover:bg-gray-600 disabled:opacity-50 disabled:cursor-not-allowed"
                      >
                        <Icon v-if="!loading" icon="mdi:pencil" class="h-4 w-4 mr-2" />
                        <Icon v-else icon="mdi:loading" class="h-4 w-4 mr-2 animate-spin" />
                        {{ loading ? 'Loading...' : 'Edit' }}
                      </button>
                    </div>
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                      <!-- Left Column -->
                      <div class="space-y-4">
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.studentId') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ userProfile.studentId || 'Not provided' }}</p>
                        </div>
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.facultyId') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ userProfile.facultyId || 'Not provided' }}</p>
                        </div>
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.department') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ userProfile.department || 'Not provided' }}</p>
                        </div>
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.major') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ userProfile.major || 'Not provided' }}</p>
                        </div>
                      </div>
                      <!-- Right Column -->
                      <div class="space-y-4">
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.yearOfStudy') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ userProfile.yearOfStudy || 'Not provided' }}</p>
                        </div>
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.gpa') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ userProfile.gpa || 'Not provided' }}</p>
                        </div>
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.enrollmentDate') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ formatDate(userProfile.enrollmentDate) || 'Not provided' }}</p>
                        </div>
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.expectedGraduationDate') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ formatDate(userProfile.expectedGraduationDate) || 'Not provided' }}</p>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Professional Info Tab -->
                <div v-if="activeTab === 'professional'" class="space-y-6">
                  <div class="bg-white dark:bg-gray-800 shadow rounded-lg p-6">
                    <div class="flex justify-between items-center mb-6">
                      <h3 class="text-lg font-medium text-gray-900 dark:text-white">{{ $t('profile.professional') }}</h3>
                      <button
                        @click="handleEditProfessional"
                        :disabled="loading"
                        class="inline-flex items-center px-3 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary dark:bg-gray-700 dark:text-gray-300 dark:border-gray-600 dark:hover:bg-gray-600 disabled:opacity-50 disabled:cursor-not-allowed"
                      >
                        <Icon v-if="!loading" icon="mdi:pencil" class="h-4 w-4 mr-2" />
                        <Icon v-else icon="mdi:loading" class="h-4 w-4 mr-2 animate-spin" />
                        {{ loading ? 'Loading...' : 'Edit' }}
                      </button>
                    </div>
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                      <!-- Left Column -->
                      <div class="space-y-4">
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.jobTitle') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ userProfile.jobTitle || 'Not provided' }}</p>
                        </div>
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.officeLocation') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ userProfile.officeLocation || 'Not provided' }}</p>
                        </div>
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.officeHours') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ userProfile.officeHours || 'Not provided' }}</p>
                        </div>
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.researchInterests') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ userProfile.researchInterests || 'Not provided' }}</p>
                        </div>
                      </div>
                      <!-- Right Column -->
                      <div class="space-y-4">
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.publications') }}</label>
                          <p class="text-gray-900 dark:text-white">{{ userProfile.publications || 'Not provided' }}</p>
                        </div>
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.linkedInUrl') }}</label>
                          <p class="text-gray-900 dark:text-white break-all">
                            <a v-if="userProfile.linkedInUrl" :href="userProfile.linkedInUrl" target="_blank" class="text-primary hover:underline">
                              {{ userProfile.linkedInUrl }}
                            </a>
                            <span v-else>Not provided</span>
                          </p>
                        </div>
                        <div>
                          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">{{ $t('profile.personalWebsite') }}</label>
                          <p class="text-gray-900 dark:text-white break-all">
                            <a v-if="userProfile.personalWebsite" :href="userProfile.personalWebsite" target="_blank" class="text-primary hover:underline">
                              {{ userProfile.personalWebsite }}
                            </a>
                            <span v-else>Not provided</span>
                          </p>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Hidden file input for avatar upload -->
    <input
      ref="avatarInput"
      type="file"
      accept="image/*"
      @change="handleAvatarUpload"
      style="display: none"
    />

    <!-- Modals -->
    <UserBasicInfoModal
      :visible="showBasicModal"
      :user-data="userProfile"
      @close="showBasicModal = false"
      @submit="handleBasicSubmit"
    />
    <UserAcademicInfoModal
      :visible="showAcademicModal"
      :user-data="userProfile"
      @close="showAcademicModal = false"
      @submit="handleAcademicSubmit"
    />
    <UserProfessionalInfoModal
      :visible="showProfessionalModal"
      :user-data="userProfile"
      @close="showProfessionalModal = false"
      @submit="handleProfessionalSubmit"
    />
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { Icon } from '@iconify/vue'
import { useI18n } from 'vue-i18n'
import { formatDate } from '@/utils/dateUtils'
import { useAuthStore } from '@/stores/authStore'
import { secureImageUrl } from '@/utils/imageUtils'
import { usersApi } from '@/api/usersApi'
import { getCurrentInstance } from 'vue'
import UserBasicInfoModal from './components/UserBasicInfoModal.vue'
import UserAcademicInfoModal from './components/UserAcademicInfoModal.vue'
import UserProfessionalInfoModal from './components/UserProfessionalInfoModal.vue'
import defaultAvatar from '@/assets/img/user.jpg'

export default {
  name: 'ProfileNew',
  components: {
    Icon,
    UserBasicInfoModal,
    UserAcademicInfoModal,
    UserProfessionalInfoModal
  },
  emits: ['profile-updated'],
  setup(props, { emit }) {
    const { t } = useI18n()
    const authStore = useAuthStore()
    const instance = getCurrentInstance()
    const toast = instance?.appContext.config.globalProperties.$toast
    
    // Refs
    const avatarInput = ref(null)
    const activeTab = ref('basic')
    
    // Modal state
    const showBasicModal = ref(false)
    const showAcademicModal = ref(false)
    const showProfessionalModal = ref(false)
    
    // Loading states
    const loading = ref(false)
    const avatarUploading = ref(false)
    const avatarTimestamp = ref(Date.now())
    const dataRefreshTimestamp = ref(Date.now())
    
    // Tab configuration
    const tabs = [
      { id: 'basic', label: 'Basic Info', icon: 'mdi:account-details' },
      { id: 'academic', label: 'Academic Info', icon: 'mdi:school' },
      { id: 'professional', label: 'Professional Info', icon: 'mdi:briefcase' }
    ]
    
    // Computed properties
    const user = computed(() => authStore.currentUser)
    
    const userFullName = computed(() => {
      dataRefreshTimestamp.value
      const profile = userProfile.value
      if (profile.firstName && profile.lastName) {
        return `${profile.firstName} ${profile.lastName}`
      }
      return authStore.currentUser?.fullName || 
             authStore.currentUser?.name || 
             authStore.currentUser?.email?.split('@')[0] || 
             'User'
    })
    
    const userEmail = computed(() => {
      return authStore.currentUser?.email || ''
    })
    
    const userProfile = computed(() => {
      dataRefreshTimestamp.value
      return authStore.currentUser?.profile || {}
    })
    
    const userId = computed(() => {
      return authStore.currentUser?.id
    })
    
    const userAvatar = computed(() => {
      const user = authStore.currentUser
      const avatar = user?.profile?.avatar || user?.avatar
      if (avatar) {
        if (avatar.startsWith('http')) {
          const securedUrl = secureImageUrl(avatar)
          return `${securedUrl}?t=${avatarTimestamp.value}`
        }
        const apiUrl = process.env.VITE_API_URL || 'http://localhost:8080/api'
        const baseUrl = apiUrl.endsWith('/') ? apiUrl.slice(0, -1) : apiUrl
        return `${baseUrl}/images/public/${avatar}/content?t=${avatarTimestamp.value}`
      }
      return defaultAvatar
    })
    
    const hasSocialLinks = computed(() => {
      const profile = userProfile.value
      return profile.linkedInUrl || profile.personalWebsite
    })
    
    const userLocation = computed(() => {
      const profile = userProfile.value
      const parts = []
      if (profile.city) parts.push(profile.city)
      if (profile.state) parts.push(profile.state)
      if (profile.country) parts.push(profile.country)
      return parts.length > 0 ? parts.join(', ') : 'Not set'
    })
    
    // Methods
    const getRoleBadgeClass = () => {
      const role = authStore.currentUser?.role
      switch (role) {
        case 'ADMIN':
          return 'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200'
        case 'FACULTY':
          return 'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-200'
        case 'STUDENT':
          return 'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200'
        case 'STAFF':
          return 'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200'
        default:
          return 'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200'
      }
    }
    
    const getRoleLabel = () => {
      const role = authStore.currentUser?.role
      switch (role) {
        case 'ADMIN': return 'Administrator'
        case 'FACULTY': return 'Faculty'
        case 'STUDENT': return 'Student'
        case 'STAFF': return 'Staff'
        default: return 'User'
      }
    }
    
    const formatGender = (gender) => {
      switch (gender) {
        case 'MALE': return 'Male'
        case 'FEMALE': return 'Female'
        case 'OTHER': return 'Other'
        default: return gender || 'Not specified'
      }
    }
    
    const handleAvatarError = (event) => {
      const img = event.target
      const originalSrc = img.src
      if (originalSrc && originalSrc.includes('cwsv.truyenthongviet.vn:9000')) {
        try {
          const urlObj = new URL(originalSrc)
          const isDevelopment = window.location.hostname === 'localhost'
          let proxyUrl
          if (isDevelopment) {
            proxyUrl = `http://localhost:3004/files${urlObj.pathname}${urlObj.search}`
          } else {
            proxyUrl = `/files${urlObj.pathname}${urlObj.search}`
          }
          img.src = proxyUrl
          img.onerror = () => {
            img.src = defaultAvatar
          }
        } catch (e) {
          img.src = defaultAvatar
        }
      } else {
        img.src = defaultAvatar
      }
    }
    
    const triggerAvatarUpload = () => {
      avatarInput.value?.click()
    }
    
    const handleAvatarUpload = async (event) => {
      const file = event.target.files[0]
      if (!file) return
      
      if (!file.type.startsWith('image/')) {
        toast?.error('Please select an image file')
        return
      }
      
      if (file.size > 5 * 1024 * 1024) {
        toast?.error('File size should be less than 5MB')
        return
      }
      
      avatarUploading.value = true
      try {
        const formData = new FormData()
        formData.append('avatar', file)
        
        const response = await usersApi.updateAvatar(formData)
        toast?.success('Avatar updated successfully!')
        
        // Update avatar timestamp to force refresh
        avatarTimestamp.value = Date.now()
        
        // Reload user data
        await loadUserData()
        
        // Emit event to notify other components
        window.dispatchEvent(new CustomEvent('avatar-updated', { 
          detail: { timestamp: avatarTimestamp.value } 
        }))
        
      } catch (error) {
        console.error('Avatar upload error:', error)
        toast?.error('Failed to upload avatar. Please try again.')
      } finally {
        avatarUploading.value = false
        event.target.value = ''
      }
    }
    
    const openLink = (url) => {
      window.open(url, '_blank')
    }
    
    const handleEditBasic = () => {
      showBasicModal.value = true
    }
    
    const handleEditAcademic = () => {
      showAcademicModal.value = true
    }
    
    const handleEditProfessional = () => {
      showProfessionalModal.value = true
    }
    
    const handleBasicSubmit = async (formData) => {
      try {
        await usersApi.updateBasicInfo(formData)
        showBasicModal.value = false
        await loadUserData()
        toast?.success('Basic information updated successfully!')
      } catch (error) {
        toast?.error('Failed to update basic information. Please try again.')
      }
    }
    
    const handleAcademicSubmit = async (formData) => {
      try {
        await usersApi.updateAcademicInfo(formData)
        showAcademicModal.value = false
        await loadUserData()
        toast?.success('Academic information updated successfully!')
      } catch (error) {
        toast?.error('Failed to update academic information. Please try again.')
      }
    }
    
    const handleProfessionalSubmit = async (formData) => {
      try {
        await usersApi.updateProfessionalInfo(formData)
        showProfessionalModal.value = false
        await loadUserData()
        toast?.success('Professional information updated successfully!')
      } catch (error) {
        toast?.error('Failed to update professional information. Please try again.')
      }
    }
    
    const loadUserData = async () => {
      loading.value = true
      try {
        const response = await usersApi.getProfile()
        const userData = response.data
        
        if (userData.success && userData.data) {
          // Update auth store with full user data
          authStore.updateAuthUser(userData.data)
          localStorage.setItem('user', JSON.stringify(userData.data))
        }
      } catch (error) {
        toast?.error('Failed to load user data. Please try refreshing the page.')
      } finally {
        loading.value = false
      }
    }
    
    // Load user data on mount
    onMounted(() => {
      loadUserData()
    })
    
    return {
      authStore,
      user,
      avatarInput,
      activeTab,
      showBasicModal,
      showAcademicModal,
      showProfessionalModal,
      loading,
      avatarUploading,
      avatarTimestamp,
      dataRefreshTimestamp,
      tabs,
      userFullName,
      userEmail,
      userProfile,
      userId,
      userAvatar,
      hasSocialLinks,
      userLocation,
      getRoleBadgeClass,
      getRoleLabel,
      formatGender,
      formatDate,
      triggerAvatarUpload,
      handleAvatarUpload,
      handleAvatarError,
      openLink,
      handleEditBasic,
      handleEditAcademic,
      handleEditProfessional,
      handleBasicSubmit,
      handleAcademicSubmit,
      handleProfessionalSubmit,
      loadUserData
    }
  }
}
</script>
