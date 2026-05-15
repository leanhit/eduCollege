<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <div class="flex items-center">
        <Icon icon="mdi:cash-register" class="text-2xl text-blue-600 dark:text-blue-400 mr-3" />
        <h1 class="text-2xl font-bold text-gray-800 dark:text-white">
          Tuition Fee Payment
        </h1>
      </div>
    </div>

    <div v-if="loading" class="flex justify-center py-12">
      <Icon icon="eos-icons:loading" class="text-4xl text-blue-600 animate-spin" />
    </div>

    <div v-else-if="fees.length === 0" class="text-center py-12 bg-white dark:bg-gray-800 rounded-xl border dark:border-gray-700">
      <Icon icon="mdi:check-decagram" class="text-6xl text-green-500 mb-4 mx-auto" />
      <h2 class="text-xl font-semibold text-gray-900 dark:text-white">No Pending Fees</h2>
      <p class="text-gray-500">You have no outstanding tuition fees at this time.</p>
    </div>

    <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Fee List -->
      <div class="lg:col-span-2 space-y-4">
        <div 
          v-for="fee in fees" 
          :key="fee.id" 
          @click="selectedFee = fee"
          :class="[
            'p-6 rounded-xl border-2 transition-all cursor-pointer bg-white dark:bg-gray-800',
            selectedFee?.id === fee.id ? 'border-blue-500 shadow-md' : 'border-gray-200 dark:border-gray-700'
          ]"
        >
          <div class="flex justify-between items-start mb-4">
            <div>
              <h3 class="font-bold text-lg text-gray-900 dark:text-white">{{ fee.description || 'Semester Tuition' }}</h3>
              <p class="text-sm text-gray-500">Due Date: {{ fee.dueDate }}</p>
            </div>
            <span class="px-3 py-1 bg-yellow-100 text-yellow-800 rounded-full text-xs font-bold uppercase">Pending</span>
          </div>
          <div class="flex justify-between items-end">
            <div class="text-2xl font-bold text-blue-600 dark:text-blue-400">
              {{ formatCurrency(fee.amount) }}
            </div>
            <button class="text-sm text-blue-600 font-medium hover:underline">Select for Payment</button>
          </div>
        </div>
      </div>

      <!-- Payment Sidebar -->
      <div class="lg:col-span-1">
        <div class="bg-white dark:bg-gray-800 p-6 rounded-xl border dark:border-gray-700 shadow-sm sticky top-6">
          <h2 class="text-lg font-bold text-gray-900 dark:text-white mb-4">Payment Summary</h2>
          <div v-if="selectedFee" class="space-y-4">
            <div class="flex justify-between text-sm">
              <span class="text-gray-500">Fee Amount:</span>
              <span class="font-medium text-gray-900 dark:text-white">{{ formatCurrency(selectedFee.amount) }}</span>
            </div>
            <div class="flex justify-between text-sm">
              <span class="text-gray-500">Service Fee:</span>
              <span class="font-medium text-gray-900 dark:text-white">$0.00</span>
            </div>
            <div class="pt-4 border-t dark:border-gray-700 flex justify-between">
              <span class="font-bold text-gray-900 dark:text-white">Total:</span>
              <span class="font-bold text-xl text-blue-600 dark:text-blue-400">{{ formatCurrency(selectedFee.amount) }}</span>
            </div>

            <div class="pt-6">
              <p class="text-xs text-gray-500 mb-3 uppercase font-bold">Select Payment Method</p>
              <div class="grid grid-cols-2 gap-3">
                <button class="p-3 border rounded-lg hover:border-blue-500 transition-colors flex flex-col items-center">
                  <Icon icon="mdi:bank" class="text-2xl mb-1" />
                  <span class="text-xs">Bank Transfer</span>
                </button>
                <button class="p-3 border rounded-lg hover:border-blue-500 transition-colors flex flex-col items-center">
                  <Icon icon="mdi:qrcode-scan" class="text-2xl mb-1" />
                  <span class="text-xs">QR Code</span>
                </button>
              </div>
            </div>

            <button 
              @click="handlePayment"
              class="w-full mt-6 bg-blue-600 text-white py-3 rounded-lg font-bold hover:bg-blue-700 transition-colors"
            >
              Pay Now
            </button>
          </div>
          <div v-else class="text-center py-8">
            <Icon icon="mdi:cursor-default-click-outline" class="text-4xl text-gray-300 mb-2" />
            <p class="text-sm text-gray-500">Please select a fee to proceed with payment.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Icon } from '@iconify/vue'
import { financeApi } from '@/api/financeApi'
import { useAuthStore } from '@/stores/authStore'

const authStore = useAuthStore()
const fees = ref([])
const selectedFee = ref(null)
const loading = ref(false)

const formatCurrency = (amount) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD'
  }).format(amount)
}

const fetchFees = async () => {
  loading.value = true
  try {
    const studentId = authStore.user?.studentId || 1 // Fallback for dev
    const response = await financeApi.getStudentFees(studentId)
    fees.value = response.data
    if (fees.value.length > 0) selectedFee.value = fees.value[0]
  } catch (error) {
    console.error('Failed to fetch fees:', error)
    // Mock data
    fees.value = [
      { id: 1, amount: 1500, description: 'Fall Semester 2025 Tuition', dueDate: '2025-09-01' },
      { id: 2, amount: 200, description: 'Library & Lab Fees', dueDate: '2025-08-15' }
    ]
    selectedFee.value = fees.value[0]
  } finally {
    loading.value = false
  }
}

const handlePayment = async () => {
  if (!selectedFee.value) return
  alert('Redirecting to payment gateway...')
}

onMounted(fetchFees)
</script>
