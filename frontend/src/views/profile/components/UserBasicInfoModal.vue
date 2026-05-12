<template>
  <div v-if="visible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white dark:bg-gray-800 rounded-lg p-6 w-full max-w-2xl max-h-[90vh] overflow-y-auto">
      <div class="flex justify-between items-center mb-6">
        <h3 class="text-lg font-medium text-gray-900 dark:text-white">Edit Basic Information</h3>
        <button
          @click="$emit('close')"
          class="text-gray-400 hover:text-gray-600 dark:hover:text-gray-300"
        >
          <Icon icon="mdi:close" class="h-6 w-6" />
        </button>
      </div>

      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <!-- First Name -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">First Name</label>
            <input
              v-model="formData.firstName"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              placeholder="Enter your first name"
            />
          </div>

          <!-- Last Name -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Last Name</label>
            <input
              v-model="formData.lastName"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              placeholder="Enter your last name"
            />
          </div>

          <!-- Date of Birth -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Date of Birth</label>
            <input
              v-model="formData.dateOfBirth"
              type="date"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            />
          </div>

          <!-- Gender -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Gender</label>
            <select
              v-model="formData.gender"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            >
              <option value="">Select Gender</option>
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
              <option value="OTHER">Other</option>
            </select>
          </div>

          <!-- Phone Number -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Phone Number</label>
            <input
              v-model="formData.phoneNumber"
              type="tel"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              placeholder="Enter your phone number"
            />
          </div>

          <!-- Alternate Email -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Alternate Email</label>
            <input
              v-model="formData.alternateEmail"
              type="email"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              placeholder="Enter alternate email address"
            />
          </div>
        </div>

        <!-- Address -->
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Address</label>
          <textarea
            v-model="formData.address"
            rows="2"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            placeholder="Enter your address"
          ></textarea>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <!-- City -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">City</label>
            <input
              v-model="formData.city"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              placeholder="Enter your city"
            />
          </div>

          <!-- State -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">State</label>
            <input
              v-model="formData.state"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              placeholder="Enter your state"
            />
          </div>

          <!-- Postal Code -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Postal Code</label>
            <input
              v-model="formData.postalCode"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
              placeholder="Enter postal code"
            />
          </div>
        </div>

        <!-- Country -->
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Country</label>
          <input
            v-model="formData.country"
            type="text"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            placeholder="Enter your country"
          />
        </div>

        <!-- Bio -->
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Bio</label>
          <textarea
            v-model="formData.bio"
            rows="4"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            placeholder="Tell us about yourself"
          ></textarea>
        </div>

        <!-- Emergency Contact -->
        <div class="border-t pt-4">
          <h4 class="text-md font-medium text-gray-900 dark:text-white mb-4">Emergency Contact</h4>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Contact Name</label>
              <input
                v-model="formData.emergencyContactName"
                type="text"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
                placeholder="Emergency contact name"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Contact Phone</label>
              <input
                v-model="formData.emergencyContactPhone"
                type="tel"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
                placeholder="Emergency contact phone"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Relationship</label>
              <input
                v-model="formData.emergencyContactRelationship"
                type="text"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
                placeholder="Relationship to contact"
              />
            </div>
          </div>
        </div>

        <!-- Privacy Settings -->
        <div class="border-t pt-4">
          <h4 class="text-md font-medium text-gray-900 dark:text-white mb-4">Privacy Settings</h4>
          <div class="space-y-3">
            <label class="flex items-center">
              <input
                v-model="formData.isProfilePublic"
                type="checkbox"
                class="mr-2 h-4 w-4 text-primary focus:ring-primary border-gray-300 rounded dark:bg-gray-700 dark:border-gray-600"
              />
              <span class="text-sm text-gray-700 dark:text-gray-300">Make my profile public</span>
            </label>
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
  name: 'UserBasicInfoModal',
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
      firstName: '',
      lastName: '',
      dateOfBirth: '',
      gender: '',
      phoneNumber: '',
      alternateEmail: '',
      address: '',
      city: '',
      state: '',
      postalCode: '',
      country: '',
      bio: '',
      emergencyContactName: '',
      emergencyContactPhone: '',
      emergencyContactRelationship: '',
      isProfilePublic: false
    })

    // Watch for userData changes and update form
    watch(() => props.userData, (newData) => {
      if (newData && Object.keys(newData).length > 0) {
        formData.value = {
          firstName: newData.firstName || '',
          lastName: newData.lastName || '',
          dateOfBirth: newData.dateOfBirth ? newData.dateOfBirth.split('T')[0] : '',
          gender: newData.gender || '',
          phoneNumber: newData.phoneNumber || '',
          alternateEmail: newData.alternateEmail || '',
          address: newData.address || '',
          city: newData.city || '',
          state: newData.state || '',
          postalCode: newData.postalCode || '',
          country: newData.country || '',
          bio: newData.bio || '',
          emergencyContactName: newData.emergencyContactName || '',
          emergencyContactPhone: newData.emergencyContactPhone || '',
          emergencyContactRelationship: newData.emergencyContactRelationship || '',
          isProfilePublic: newData.isProfilePublic || false
        }
      }
    }, { immediate: true })

    const handleSubmit = async () => {
      submitting.value = true
      try {
        // Format date for backend
        const submitData = { ...formData.value }
        if (submitData.dateOfBirth) {
          submitData.dateOfBirth = new Date(submitData.dateOfBirth).toISOString()
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
