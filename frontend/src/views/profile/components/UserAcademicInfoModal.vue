<template>
  <div v-if="visible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white dark:bg-gray-800 rounded-lg p-6 w-full max-w-2xl max-h-[90vh] overflow-y-auto">
      <div class="flex justify-between items-center mb-6">
        <h3 class="text-lg font-medium text-gray-900 dark:text-white">Edit Academic Information</h3>
        <button
          @click="$emit('close')"
          class="text-gray-400 hover:text-gray-600 dark:hover:text-gray-300"
        >
          <Icon icon="mdi:close" class="h-6 w-6" />
        </button>
      </div>

      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <!-- Student ID -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Student ID</label>
            <input
              v-model="formData.studentId"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              placeholder="Enter your student ID"
            />
          </div>

          <!-- Faculty ID -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Faculty ID</label>
            <input
              v-model="formData.facultyId"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              placeholder="Enter your faculty ID"
            />
          </div>

          <!-- Department -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Department</label>
            <select
              v-model="formData.department"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            >
              <option value="">Select Department</option>
              <option value="Computer Science">Computer Science</option>
              <option value="Engineering">Engineering</option>
              <option value="Business">Business</option>
              <option value="Medicine">Medicine</option>
              <option value="Arts">Arts</option>
              <option value="Science">Science</option>
              <option value="Mathematics">Mathematics</option>
            </select>
          </div>

          <!-- Major -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Major</label>
            <input
              v-model="formData.major"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              placeholder="Enter your major"
            />
          </div>

          <!-- Year of Study -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Year of Study</label>
            <select
              v-model="formData.yearOfStudy"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            >
              <option value="">Select Year</option>
              <option value="1st Year">1st Year</option>
              <option value="2nd Year">2nd Year</option>
              <option value="3rd Year">3rd Year</option>
              <option value="4th Year">4th Year</option>
              <option value="5th Year">5th Year</option>
              <option value="Graduate">Graduate</option>
              <option value="Postgraduate">Postgraduate</option>
            </select>
          </div>

          <!-- GPA -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">GPA</label>
            <input
              v-model="formData.gpa"
              type="number"
              step="0.01"
              min="0"
              max="4.0"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              placeholder="Enter your GPA (0.0 - 4.0)"
            />
          </div>

          <!-- Enrollment Date -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Enrollment Date</label>
            <input
              v-model="formData.enrollmentDate"
              type="date"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            />
          </div>

          <!-- Expected Graduation Date -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Expected Graduation Date</label>
            <input
              v-model="formData.expectedGraduationDate"
              type="date"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            />
          </div>
        </div>

        <!-- Form Actions -->
        <div class="flex justify-end space-x-3 pt-4 border-t">
          <button
            type="button"
            @click="$emit('close')"
            class="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary dark:bg-gray-700 dark:text-gray-300 dark:border-gray-600 dark:hover:bg-gray-600"
          >
            Cancel
          </button>
          <button
            type="submit"
            :disabled="submitting"
            class="px-4 py-2 text-sm font-medium text-white bg-primary border border-transparent rounded-md hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <Icon v-if="submitting" icon="mdi:loading" class="h-4 w-4 mr-2 animate-spin" />
            {{ submitting ? 'Saving...' : 'Save Changes' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { ref, watch } from 'vue'
import { Icon } from '@iconify/vue'

export default {
  name: 'UserAcademicInfoModal',
  components: {
    Icon
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    userData: {
      type: Object,
      default: () => ({})
    }
  },
  emits: ['close', 'submit'],
  setup(props, { emit }) {
    const submitting = ref(false)
    
    const formData = ref({
      studentId: '',
      facultyId: '',
      department: '',
      major: '',
      yearOfStudy: '',
      gpa: '',
      enrollmentDate: '',
      expectedGraduationDate: ''
    })

    // Watch for userData changes and update form
    watch(() => props.userData, (newData) => {
      if (newData && Object.keys(newData).length > 0) {
        formData.value = {
          studentId: newData.studentId || '',
          facultyId: newData.facultyId || '',
          department: newData.department || '',
          major: newData.major || '',
          yearOfStudy: newData.yearOfStudy || '',
          gpa: newData.gpa ? newData.gpa.toString() : '',
          enrollmentDate: newData.enrollmentDate ? newData.enrollmentDate.split('T')[0] : '',
          expectedGraduationDate: newData.expectedGraduationDate ? newData.expectedGraduationDate.split('T')[0] : ''
        }
      }
    }, { immediate: true })

    const handleSubmit = async () => {
      submitting.value = true
      try {
        // Format dates for backend
        const submitData = { ...formData.value }
        if (submitData.enrollmentDate) {
          submitData.enrollmentDate = new Date(submitData.enrollmentDate).toISOString()
        }
        if (submitData.expectedGraduationDate) {
          submitData.expectedGraduationDate = new Date(submitData.expectedGraduationDate).toISOString()
        }
        
        // Convert GPA to number
        if (submitData.gpa) {
          submitData.gpa = parseFloat(submitData.gpa)
        }
        
        emit('submit', submitData)
      } finally {
        submitting.value = false
      }
    }

    return {
      formData,
      submitting,
      handleSubmit
    }
  }
}
</script>
