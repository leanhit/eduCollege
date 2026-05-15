<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <div>
        <h1 class="text-2xl font-bold text-gray-900 dark:text-white">Faculties</h1>
        <p class="text-gray-500 dark:text-gray-400">Manage university faculties and departments</p>
      </div>
      <button class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors">
        Add Faculty
      </button>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div v-for="faculty in faculties" :key="faculty.id" class="bg-white dark:bg-gray-800 rounded-xl p-6 border dark:border-gray-700 shadow-sm">
        <div class="flex items-center mb-4">
          <div class="p-3 bg-blue-100 dark:bg-blue-900/30 rounded-lg text-blue-600 dark:text-blue-400 mr-4">
            <Icon icon="mdi:office-building-outline" class="text-2xl" />
          </div>
          <div>
            <h3 class="font-bold text-gray-900 dark:text-white">{{ faculty.vietnameseName }}</h3>
            <p class="text-xs text-gray-500 uppercase">{{ faculty.code }}</p>
          </div>
        </div>
        <p class="text-sm text-gray-600 dark:text-gray-300 mb-4">{{ faculty.description }}</p>
        <div class="flex justify-between items-center text-sm">
          <span class="text-gray-500">Dean: {{ faculty.deanName || 'N/A' }}</span>
          <button class="text-blue-600 hover:underline">View Details</button>
        </div>
      </div>
    </div>
    
    <div v-if="loading" class="flex justify-center items-center py-12">
      <Icon icon="eos-icons:loading" class="text-4xl text-blue-600 animate-spin" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Icon } from '@iconify/vue'
import { academicApi } from '@/api/academicApi'

const faculties = ref([])
const loading = ref(false)

const fetchFaculties = async () => {
  loading.value = true
  try {
    const response = await academicApi.getFaculties()
    faculties.value = response.data
  } catch (error) {
    console.error('Failed to fetch faculties:', error)
    // Mock data if API fails for now
    faculties.value = [
      { id: 1, code: 'CNTT', vietnameseName: 'Khoa Công nghệ Thông tin', description: 'Faculty of Information Technology', deanName: 'Dr. Nguyen Van A' },
      { id: 2, code: 'TOAN', vietnameseName: 'Khoa Toán - Tin học', description: 'Faculty of Mathematics and Informatics', deanName: 'Dr. Le Van B' }
    ]
  } finally {
    loading.value = false
  }
}

onMounted(fetchFaculties)
</script>
