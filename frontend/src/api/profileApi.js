import axios from '@/plugins/axios'

export const profileApi = {
  // Get current user profile
  getProfile() {
    return axios.get('/api/v1/profiles/me')
  },

  // Update complete profile
  updateProfile(profileData) {
    return axios.put('/api/v1/profiles/me', profileData)
  },

  // Update basic information
  updateBasicInfo(basicData) {
    return axios.put('/api/v1/profiles/me/basic', basicData)
  },

  // Update academic information
  updateAcademicInfo(academicData) {
    return axios.put('/api/v1/profiles/me/academic', academicData)
  },

  // Update professional information
  updateProfessionalInfo(professionalData) {
    return axios.put('/api/v1/profiles/me/professional', professionalData)
  },

  // Update avatar
  updateAvatar(avatarUrl) {
    return axios.put(`/api/v1/profiles/me/avatar?avatarUrl=${encodeURIComponent(avatarUrl)}`)
  },

  // Upload avatar file (if backend supports file upload)
  uploadAvatar(formData) {
    return axios.put('/api/v1/profiles/me/avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
  },

  // Get user profile by ID (admin/faculty only)
  getProfileById(userId) {
    return axios.get(`/api/v1/profiles/user/${userId}`)
  },

  // Get profiles by department (admin/faculty only)
  getProfilesByDepartment(department) {
    return axios.get(`/api/v1/profiles/department/${encodeURIComponent(department)}`)
  },

  // Get profiles by major (admin/faculty only)
  getProfilesByMajor(major) {
    return axios.get(`/api/v1/profiles/major/${encodeURIComponent(major)}`)
  },

  // Get faculty by department (admin/faculty/staff only)
  getFacultyByDepartment(department) {
    return axios.get(`/api/v1/profiles/faculty/department/${encodeURIComponent(department)}`)
  },

  // Get students by year of study (admin/faculty only)
  getStudentsByYear(yearOfStudy) {
    return axios.get(`/api/v1/profiles/students/year/${encodeURIComponent(yearOfStudy)}`)
  },

  // Search profiles by name (admin/faculty only)
  searchProfilesByName(name) {
    return axios.get(`/api/v1/profiles/search?name=${encodeURIComponent(name)}`)
  },

  // Get public profiles
  getPublicProfiles() {
    return axios.get('/api/v1/profiles/public')
  },

  // Delete current user profile
  deleteProfile() {
    return axios.delete('/api/v1/profiles/me')
  }
}
