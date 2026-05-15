<template>
  <!-- Modern Header -->
  <header class="bg-white dark:bg-gray-900 border-b border-gray-200 dark:border-gray-700 shadow-sm">
    <div class="px-4 sm:px-6 lg:px-8">
      <div class="flex items-center justify-between h-16">
        <!-- Left Section -->
        <div class="flex items-center">
          <!-- Sidebar Toggle Button -->
          <button
            @click="handleSidebarToggle"
            class="p-2 rounded-lg text-gray-500 hover:text-gray-700 hover:bg-gray-100 dark:text-gray-400 dark:hover:text-gray-200 dark:hover:bg-gray-800 transition-all duration-200"
            title="Toggle Sidebar"
          >
            <Icon :icon="sidebarOpen ? 'mdi:menu-open' : 'mdi:menu'" class="text-xl" />
          </button>
          
          <!-- Search Bar -->
          <div class="ml-4 flex-1 max-w-lg">
            <div class="relative">
              <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <Icon icon="ei:search" class="h-5 w-5 text-gray-400" />
              </div>
              <input
                type="text"
                v-model="searchStore.searchQuery"
                :placeholder="searchPlaceholder"
                class="block w-full pl-10 pr-10 py-2 border border-gray-300 rounded-lg bg-gray-50 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-800 dark:border-gray-600 dark:text-white dark:placeholder-gray-400 dark:focus:ring-blue-400"
              />
            </div>
          </div>
        </div>

        <!-- Right Section -->
        <div class="flex items-center space-x-2" v-if="authStore.isLoggedIn">
          <!-- Language Switcher -->
          <LanguageSwitcher />
          
          <!-- Theme Toggle -->
          <button
            @click="setTheme(!darkMode)"
            class="p-2 rounded-lg text-gray-500 hover:text-gray-700 hover:bg-gray-100 dark:text-gray-400 dark:hover:text-gray-200 dark:hover:bg-gray-800 transition-all duration-200"
            :title="darkMode ? 'Switch to Light Mode' : 'Switch to Dark Mode'"
          >
            <Icon :icon="darkMode ? 'ri:sun-fill' : 'ri:moon-fill'" class="text-xl" />
          </button>
          
          <!-- Notifications -->
          <div class="relative">
            <button
              @click="notifToggle"
              class="p-2 rounded-lg text-gray-500 hover:text-gray-700 hover:bg-gray-100 dark:text-gray-400 dark:hover:text-gray-200 dark:hover:bg-gray-800 transition-all duration-200 relative"
              title="Notifications"
            >
              <Icon icon="clarity:notification-line" class="text-xl" />
            </button>
            
            <!-- Notification Dropdown (Simplified) -->
            <transition
              enter-active-class="transition ease-out duration-200"
              enter-from-class="transform opacity-0 scale-95"
              enter-to-class="transform opacity-100 scale-100"
              leave-active-class="transition ease-in duration-75"
              leave-from-class="transform opacity-100 scale-100"
              leave-to-class="transform opacity-0 scale-95"
            >
              <div
                v-show="notification"
                class="absolute right-0 mt-2 w-80 bg-white dark:bg-gray-800 rounded-lg shadow-lg ring-1 ring-black ring-opacity-5 z-50"
              >
                <div class="p-4 border-b border-gray-200 dark:border-gray-700">
                  <h3 class="text-sm font-medium text-gray-900 dark:text-white">Notifications</h3>
                </div>
                <div class="p-8 text-center text-gray-500 dark:text-gray-400">
                  No notifications yet.
                </div>
              </div>
            </transition>
          </div>
          
          <!-- User Menu -->
          <div class="relative">
            <button
              @click="menuToggle"
              class="flex items-center p-2 rounded-lg text-gray-500 hover:text-gray-700 hover:bg-gray-100 dark:text-gray-400 dark:hover:text-gray-200 dark:hover:bg-gray-800 transition-all duration-200"
            >
              <div class="flex items-center space-x-3">
                <img
                  :src="userAvatar"
                  class="h-8 w-8 rounded-full object-cover ring-2 ring-gray-300 dark:ring-gray-600 flex-shrink-0"
                  alt="User Avatar"
                  @error="handleAvatarError"
                />
                <div class="hidden lg:block text-left">
                  <p class="text-sm font-medium text-gray-900 dark:text-white">{{ userName }}</p>
                  <p class="text-xs text-gray-500 dark:text-gray-400">{{ userRole }}</p>
                </div>
                <Icon icon="mdi:chevron-down" class="h-4 w-4" />
              </div>
            </button>
            
            <!-- User Dropdown -->
            <transition
              enter-active-class="transition ease-out duration-200"
              enter-from-class="transform opacity-0 scale-95"
              enter-to-class="transform opacity-100 scale-100"
              leave-active-class="transition ease-in duration-75"
              leave-from-class="transform opacity-100 scale-100"
              leave-to-class="transform opacity-0 scale-95"
            >
              <div
                v-show="menu"
                class="absolute right-0 mt-2 w-48 bg-white dark:bg-gray-800 rounded-lg shadow-lg ring-1 ring-black ring-opacity-5 z-50"
                @blur="menuToggleBlur"
              >
                <div class="p-4 border-b border-gray-200 dark:border-gray-700">
                  <p class="text-sm font-medium text-gray-900 dark:text-white">{{ userName }}</p>
                  <p class="text-xs text-gray-500 dark:text-gray-400 truncate">{{ userEmail }}</p>
                </div>
                
                <div class="py-2">
                  <router-link
                    to="/profile"
                    @click="menu = false"
                    class="flex items-center px-4 py-2 text-sm text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700"
                  >
                    <Icon icon="mdi:account-circle" class="mr-3 h-4 w-4" />
                    Profile
                  </router-link>
                  
                  <router-link
                    to="/help"
                    @click="menu = false"
                    class="flex items-center px-4 py-2 text-sm text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700"
                  >
                    <Icon icon="mdi:help-circle" class="mr-3 h-4 w-4" />
                    Help Center
                  </router-link>
                </div>
                
                <div class="border-t border-gray-200 dark:border-gray-700 py-2">
                  <a
                    href="#"
                    @click.prevent="handleLogout"
                    class="flex items-center px-4 py-2 text-sm text-red-600 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-900/20"
                  >
                    <Icon icon="mdi:logout" class="mr-3 h-4 w-4" />
                    Sign Out
                  </a>
                </div>
              </div>
            </transition>
          </div>
        </div>
        
        <!-- Login Button -->
        <div v-else class="flex items-center space-x-2">
          <LanguageSwitcher />
          <router-link 
            to="/login" 
            class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-lg text-white bg-blue-600 hover:bg-blue-700"
          >
            Sign In
          </router-link>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
  import { Icon } from "@iconify/vue";
  import { useRouter } from "vue-router";
  import { ref, computed } from 'vue';
  import { setDarkMode, loadDarkMode } from "@/helper/theme";
  import LanguageSwitcher from "./LanguageSwitcher.vue";
  import { useAuthStore } from '@/stores/authStore';
  import { useSearchStore } from '@/stores/searchStore';

  export default {
    props: {
      sidebarOpen: {
        type: Boolean,
        default: true
      }
    },
    data() {
      return {
        menu: false,
        darkMode: false,
        notification: false,
      };
    },
    components: {
      Icon,
      LanguageSwitcher,
    },
    setup() {
      const authStore = useAuthStore();
      const searchStore = useSearchStore();
      const router = useRouter();
      
      const searchPlaceholder = computed(() => {
        return 'Search academic records...';
      });

      return {
        authStore,
        searchStore,
        router,
        searchPlaceholder,
      };
    },
    computed: {
      userName() {
        return this.authStore.currentUser?.profile?.fullName || 
               this.authStore.currentUser?.name || 
               'User';
      },
      userEmail() {
        return this.authStore.currentUser?.email || '';
      },
      userRole() {
        return this.authStore.currentUser?.role || 'User';
      },
      userAvatar() {
        const user = this.authStore.currentUser;
        const avatar = user?.profile?.avatar || user?.avatar;
        if (avatar) {
          if (avatar.startsWith('http')) return avatar;
          const apiUrl = process.env.VITE_API_URL || 'http://localhost:8080/api';
          return `${apiUrl}/images/public/${avatar}/content`;
        }
        return require("@/assets/img/user.jpg");
      }
    },
    methods: {
      setDarkMode,
      loadDarkMode,
      handleSidebarToggle() {
        this.$emit('sidebarToggle');
      },
      menuToggle() {
        this.menu = !this.menu;
      },
      menuToggleBlur() {
        this.menu = false;
      },
      notifToggle() {
        this.notification = !this.notification;
        this.menu = false;
      },
      setTheme(bool) {
        this.darkMode = bool;
        this.setDarkMode(bool);
      },
      handleLogout() {
        this.authStore.logout();
        this.menu = false;
      },
      handleAvatarError(event) {
        event.target.src = require("@/assets/img/user.jpg");
      }
    },
    mounted() {
      this.darkMode = this.loadDarkMode();
    },
  };
</script>
