<template>
  <div class="flex bg-gray-50 font-lexend dark:bg-gray-900 min-h-screen">
    <!-- Sidebar Container -->
    <div
      v-if="!hideNav"
      :class="sidebarOpen ? 'block lg:block' : 'hidden lg:hidden'"
      class="lg:flex-none w-64 bg-white dark:bg-gray-800 border-r dark:border-gray-700 lg:z-0 z-20 overflow-auto lg:relative fixed h-screen"
    >
      <perfect-scrollbar class="h-full">
        <Sidebar @sidebarToggle="sidebarOpen = false" />
      </perfect-scrollbar>
    </div>

    <!-- Main Content Area -->
    <div
      class="flex-auto w-full overflow-auto h-screen transition-colors flex flex-col"
      id="body-scroll"
    >
      <Header
        v-if="!hideNav"
        :sidebar-open="sidebarOpen"
        @sidebarToggle="sidebarOpen = !sidebarOpen"
      />
      
      <main class="flex-grow">
        <router-view v-slot="{ Component }">
          <transition name="slide-up" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>

      <Footer v-if="!hideNav" />
    </div>

    <!-- Global Components -->
    <NotificationToast />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import Sidebar from "@/components/Sidebar"
import Header from "@/components/Header"
import Footer from "@/components/Footer"
import NotificationToast from "@/components/common/NotificationToast"
import Scrollbar from "smooth-scrollbar"

const route = useRoute()
const sidebarOpen = ref(true)

// Computed property to determine if navigation should be hidden
// This ensures reactivity when the route changes
const hideNav = computed(() => {
  return route.meta && route.meta.hideNav === true
})

onMounted(() => {
  const scrollElement = document.querySelector("#body-scroll")
  if (scrollElement) {
    Scrollbar.init(scrollElement)
  }
})
</script>

<style>
.slide-up-enter-active {
  transition: all 0.3s ease-out;
}
.slide-up-leave-active {
  transition: all 0.3s cubic-bezier(1, 0.5, 0.8, 1);
}
.slide-up-enter-from,
.slide-up-leave-to {
  transform: translateY(10px);
  opacity: 0;
}

/* Custom scrollbar for dark mode */
.dark .ps__rail-y {
  background-color: transparent !important;
}
.dark .ps__thumb-y {
  background-color: #4b5563 !important;
}
</style>
