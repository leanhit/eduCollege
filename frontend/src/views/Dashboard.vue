<template>
  <div class="dashboard p-4">
    <!-- Header -->
    <div class="mt-2 w-full">
      <div class="lg:flex grid-cols-1 lg:space-y-0 space-y-3 gap-5 justify-between">
        <div>
          <p class="uppercase text-xs text-gray-700 font-semibold">Academic Overview</p>
          <h1 class="text-2xl text-gray-900 dark:text-gray-200 font-medium">
            Welcome to EduCollege Portal
          </h1>
        </div>
        <div class="flex gap-2">
          <button
            @click="refreshData"
            :disabled="loading"
            class="bg-white dark:bg-gray-800 hover:border-gray-200 dark:hover:bg-gray-700 dark:text-white dark:border-gray-700 border rounded py-2 px-5 flex items-center gap-2 transition-colors"
          >
            <Icon icon="mdi:refresh" :class="{'animate-spin': loading}" class="text-lg" />
            Refresh
          </button>
        </div>
      </div>
    </div>

    <!-- Statistics Cards -->
    <div class="wrapper-card grid lg:grid-cols-4 grid-cols-1 md:grid-cols-2 gap-4 mt-6">
      <!-- Total Students -->
      <div class="card bg-white dark:bg-gray-800 w-full rounded-lg p-5 border dark:border-gray-700 flex shadow-sm">
        <div class="p-2">
          <div class="bg-blue-100 dark:bg-blue-900/30 rounded-full w-14 h-14 flex items-center justify-center text-blue-600 dark:text-blue-400">
            <Icon icon="mdi:account-school" class="text-3xl" />
          </div>
        </div>
        <div class="ml-4 p-2">
          <p class="text-2xl font-bold text-gray-900 dark:text-gray-100">1,250</p>
          <h2 class="text-sm font-medium text-gray-500 dark:text-gray-400">Total Students</h2>
        </div>
      </div>

      <!-- Total Teachers -->
      <div class="card bg-white dark:bg-gray-800 w-full rounded-lg p-5 border dark:border-gray-700 flex shadow-sm">
        <div class="p-2">
          <div class="bg-green-100 dark:bg-green-900/30 rounded-full w-14 h-14 flex items-center justify-center text-green-600 dark:text-green-400">
            <Icon icon="mdi:teacher" class="text-3xl" />
          </div>
        </div>
        <div class="ml-4 p-2">
          <p class="text-2xl font-bold text-gray-900 dark:text-gray-100">85</p>
          <h2 class="text-sm font-medium text-gray-500 dark:text-gray-400">Faculty Members</h2>
        </div>
      </div>

      <!-- Active Courses -->
      <div class="card bg-white dark:bg-gray-800 w-full rounded-lg p-5 border dark:border-gray-700 flex shadow-sm">
        <div class="p-2">
          <div class="bg-purple-100 dark:bg-purple-900/30 rounded-full w-14 h-14 flex items-center justify-center text-purple-600 dark:text-purple-400">
            <Icon icon="mdi:book-open-variant" class="text-3xl" />
          </div>
        </div>
        <div class="ml-4 p-2">
          <p class="text-2xl font-bold text-gray-900 dark:text-gray-100">42</p>
          <h2 class="text-sm font-medium text-gray-500 dark:text-gray-400">Active Courses</h2>
        </div>
      </div>

      <!-- Revenue/Fees -->
      <div class="card bg-white dark:bg-gray-800 w-full rounded-lg p-5 border dark:border-gray-700 flex shadow-sm">
        <div class="p-2">
          <div class="bg-orange-100 dark:bg-orange-900/30 rounded-full w-14 h-14 flex items-center justify-center text-orange-600 dark:text-orange-400">
            <Icon icon="mdi:currency-usd" class="text-3xl" />
          </div>
        </div>
        <div class="ml-4 p-2">
          <p class="text-2xl font-bold text-gray-900 dark:text-gray-100">$45.2K</p>
          <h2 class="text-sm font-medium text-gray-500 dark:text-gray-400">Fees Collected</h2>
        </div>
      </div>
    </div>

    <!-- Main Content Grid -->
    <div class="mt-6 grid lg:grid-cols-3 gap-6">
      <!-- Quick Actions -->
      <div class="bg-white dark:bg-gray-800 p-6 rounded-lg border dark:border-gray-700 shadow-sm">
        <h2 class="text-lg font-semibold text-gray-900 dark:text-gray-100 mb-4">Quick Actions</h2>
        <div class="space-y-4">
          <router-link to="/academic/enrollments" class="flex items-center p-4 bg-blue-50 dark:bg-blue-900/20 rounded-xl hover:bg-blue-100 transition-colors">
            <Icon icon="mdi:clipboard-text-outline" class="text-blue-600 dark:text-blue-400 text-2xl mr-4" />
            <div>
              <p class="font-bold text-gray-900 dark:text-gray-100">Course Registration</p>
              <p class="text-xs text-gray-500">Manage student enrollments</p>
            </div>
          </router-link>
          
          <router-link to="/finance/tuition" class="flex items-center p-4 bg-green-50 dark:bg-green-900/20 rounded-xl hover:bg-green-100 transition-colors">
            <Icon icon="mdi:credit-card-outline" class="text-green-600 dark:text-green-400 text-2xl mr-4" />
            <div>
              <p class="font-bold text-gray-900 dark:text-gray-100">Fee Payment</p>
              <p class="text-xs text-gray-500">Pay tuition and other fees</p>
            </div>
          </router-link>

          <router-link to="/profile" class="flex items-center p-4 bg-purple-50 dark:bg-purple-900/20 rounded-xl hover:bg-purple-100 transition-colors">
            <Icon icon="mdi:account-cog-outline" class="text-purple-600 dark:text-purple-400 text-2xl mr-4" />
            <div>
              <p class="font-bold text-gray-900 dark:text-gray-100">Account Settings</p>
              <p class="text-xs text-gray-500">Update your personal profile</p>
            </div>
          </router-link>
        </div>
      </div>

      <!-- Recent Activity -->
      <div class="lg:col-span-2 bg-white dark:bg-gray-800 p-6 rounded-lg border dark:border-gray-700 shadow-sm">
        <h2 class="text-lg font-semibold text-gray-900 dark:text-gray-100 mb-4">Recent Academic Activity</h2>
        <div class="overflow-x-auto">
          <table class="w-full text-left">
            <thead>
              <tr class="text-gray-400 text-xs uppercase border-b dark:border-gray-700">
                <th class="pb-3 px-2 font-medium">Activity</th>
                <th class="pb-3 px-2 font-medium">Status</th>
                <th class="pb-3 px-2 font-medium">Date</th>
              </tr>
            </thead>
            <tbody class="divide-y dark:divide-gray-700">
              <tr v-for="i in 5" :key="i" class="hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors">
                <td class="py-4 px-2">
                  <p class="text-sm font-medium text-gray-900 dark:text-gray-100">Course Enrollment: CS101</p>
                  <p class="text-xs text-gray-500">Student ID: SV24001</p>
                </td>
                <td class="py-4 px-2">
                  <span class="px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400">
                    Completed
                  </span>
                </td>
                <td class="py-4 px-2 text-sm text-gray-500">May 15, 2026</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Icon } from '@iconify/vue'

const loading = ref(false)

const refreshData = async () => {
  loading.value = true
  // Mocking refresh
  setTimeout(() => {
    loading.value = false
  }, 1000)
}

onMounted(() => {
  // Initial load logic
})
</script>

<style scoped>
.animate-spin {
  animation: spin 1s linear infinite;
}
@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>
