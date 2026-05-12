<template>
  <div v-if="visible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white dark:bg-gray-800 rounded-lg p-6 w-full max-w-2xl max-h-[90vh] overflow-y-auto">
      <div class="flex justify-between items-center mb-6">
        <h3 class="text-lg font-medium text-gray-900 dark:text-white">Edit Professional Information</h3>
        <button
          @click="$emit('close')"
          class="text-gray-400 hover:text-gray-600 dark:hover:text-gray-300"
        >
          <Icon icon="mdi:close" class="h-6 w-6" />
        </button>
      </div>

      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <!-- Job Title -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Job Title</label>
            <input
              v-model="formData.jobTitle"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              placeholder="Enter your job title"
            />
          </div>

          <!-- Office Location -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Office Location</label>
            <input
              v-model="formData.officeLocation"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              placeholder="Enter your office location"
            />
          </div>

          <!-- Office Hours -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Office Hours</label>
            <input
              v-model="formData.officeHours"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              placeholder="e.g., Mon-Wed 2-4 PM"
            />
          </div>

          <!-- LinkedIn URL -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">LinkedIn URL</label>
            <input
              v-model="formData.linkedInUrl"
              type="url"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              placeholder="https://linkedin.com/in/yourprofile"
            />
          </div>
        </div>

        <!-- Research Interests -->
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Research Interests</label>
          <textarea
            v-model="formData.researchInterests"
            rows="3"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            placeholder="Enter your research interests"
          ></textarea>
        </div>

        <!-- Publications -->
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Publications</label>
          <textarea
            v-model="formData.publications"
            rows="4"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            placeholder="List your publications"
          ></textarea>
        </div>

        <!-- Personal Website -->
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Personal Website</label>
          <input
            v-model="formData.personalWebsite"
            type="url"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            placeholder="https://yourwebsite.com"
          />
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
  name: 'UserProfessionalInfoModal',
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
      jobTitle: '',
      officeLocation: '',
      officeHours: '',
      researchInterests: '',
      publications: '',
      linkedInUrl: '',
      personalWebsite: ''
    })

    // Watch for userData changes and update form
    watch(() => props.userData, (newData) => {
      if (newData && Object.keys(newData).length > 0) {
        formData.value = {
          jobTitle: newData.jobTitle || '',
          officeLocation: newData.officeLocation || '',
          officeHours: newData.officeHours || '',
          researchInterests: newData.researchInterests || '',
          publications: newData.publications || '',
          linkedInUrl: newData.linkedInUrl || '',
          personalWebsite: newData.personalWebsite || ''
        }
      }
    }, { immediate: true })

    const handleSubmit = async () => {
      submitting.value = true
      try {
        emit('submit', formData.value)
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
