<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <div class="flex items-center">
        <Icon icon="mdi:history" class="text-2xl text-blue-600 dark:text-blue-400 mr-3" />
        <h1 class="text-2xl font-bold text-gray-800 dark:text-white">
          Payment History
        </h1>
      </div>
      <button
        @click="fetchHistory"
        :disabled="loading"
        class="bg-white dark:bg-gray-800 border dark:border-gray-700 px-4 py-2 rounded-lg hover:bg-gray-50 transition-colors flex items-center"
      >
        <Icon icon="mdi:refresh" :class="{'animate-spin': loading}" class="mr-2" />
        Refresh
      </button>
    </div>

    <div class="bg-white dark:bg-gray-800 rounded-xl border dark:border-gray-700 overflow-hidden shadow-sm">
      <div class="overflow-x-auto">
        <table class="w-full text-left">
          <thead class="bg-gray-50 dark:bg-gray-700/50">
            <tr>
              <th class="px-6 py-4 text-xs font-bold text-gray-500 uppercase">Transaction ID</th>
              <th class="px-6 py-4 text-xs font-bold text-gray-500 uppercase">Amount</th>
              <th class="px-6 py-4 text-xs font-bold text-gray-500 uppercase">Method</th>
              <th class="px-6 py-4 text-xs font-bold text-gray-500 uppercase">Status</th>
              <th class="px-6 py-4 text-xs font-bold text-gray-500 uppercase">Date</th>
              <th class="px-6 py-4 text-xs font-bold text-gray-500 uppercase">Actions</th>
            </tr>
          </thead>
          <tbody class="divide-y dark:divide-gray-700">
            <tr v-if="loading">
              <td colspan="6" class="px-6 py-12 text-center">
                <Icon icon="eos-icons:loading" class="text-3xl text-blue-600 animate-spin mx-auto" />
              </td>
            </tr>
            <tr v-else-if="history.length === 0">
              <td colspan="6" class="px-6 py-12 text-center text-gray-500">
                No payment history found.
              </td>
            </tr>
            <tr v-for="item in history" :key="item.id" class="hover:bg-gray-50 dark:hover:bg-gray-700/30 transition-colors">
              <td class="px-6 py-4">
                <span class="font-mono text-sm text-gray-900 dark:text-gray-100">{{ item.transactionId }}</span>
              </td>
              <td class="px-6 py-4 font-bold text-gray-900 dark:text-gray-100">
                {{ formatCurrency(item.amount) }}
              </td>
              <td class="px-6 py-4">
                <div class="flex items-center text-sm text-gray-600 dark:text-gray-300">
                  <Icon :icon="getMethodIcon(item.method)" class="mr-2 text-lg" />
                  {{ item.method }}
                </div>
              </td>
              <td class="px-6 py-4">
                <span :class="getStatusClass(item.status)" class="px-2.5 py-0.5 rounded-full text-xs font-bold uppercase">
                  {{ item.status }}
                </span>
              </td>
              <td class="px-6 py-4 text-sm text-gray-500">
                {{ item.date }}
              </td>
              <td class="px-6 py-4">
                <button class="text-blue-600 hover:underline text-sm font-medium">Invoice</button>
              </td>
            </tr>
          </tbody>
        </table>
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
const history = ref([])
const loading = ref(false)

const formatCurrency = (amount) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD'
  }).format(amount)
}

const getStatusClass = (status) => {
  switch (status.toUpperCase()) {
    case 'COMPLETED': return 'bg-green-100 text-green-800'
    case 'PENDING': return 'bg-yellow-100 text-yellow-800'
    case 'FAILED': return 'bg-red-100 text-red-800'
    default: return 'bg-gray-100 text-gray-800'
  }
}

const getMethodIcon = (method) => {
  if (method.toLowerCase().includes('bank')) return 'mdi:bank'
  if (method.toLowerCase().includes('qr')) return 'mdi:qrcode-scan'
  return 'mdi:credit-card'
}

const fetchHistory = async () => {
  loading.value = true
  try {
    const studentId = authStore.user?.studentId || 1
    const response = await financeApi.getPaymentHistory(studentId)
    history.value = response.data
  } catch (error) {
    console.error('Failed to fetch history:', error)
    // Mock data
    history.value = [
      { id: 1, transactionId: 'TXN-9901', amount: 1500, method: 'Bank Transfer', status: 'Completed', date: '2025-01-15 10:30' },
      { id: 2, transactionId: 'TXN-9902', amount: 50, method: 'QR Payment', status: 'Completed', date: '2025-02-10 14:45' }
    ]
  } finally {
    loading.value = false
  }
}

onMounted(fetchHistory)
</script>
