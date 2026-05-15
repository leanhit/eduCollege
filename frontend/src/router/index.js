import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from '../stores/authStore';

// Default Pages
import Dashboard from "../views/Dashboard.vue";
// Auth Pages
import Login from "../views/auth/Login.vue";
import Register from "../views/auth/Register.vue";
import Profile from "../views/profile/Profile.vue";
import Help from "../views/help/Help.vue";
import ForgotPassword from "../views/auth/ForgotPassword.vue";
// Payment Pages
import PaymentDeposit from "../views/payment/Deposit.vue";
import PaymentHistory from "../views/payment/History.vue";

var appname = " - EduCollege University System";

const routes = [
  {
    path: "/",
    redirect: "/login"
  },
  {
    path: "/login",
    name: "login",
    component: Login,
    meta: { hideNav: true },
  },
  {
    path: "/register",
    name: "register", 
    component: Register,
    meta: { hideNav: true },
  },
  {
    path: "/auth/forgot-password",
    name: "forgot-password",
    component: ForgotPassword,
    meta: { hideNav: true },
  },
  {
    path: "/profile",
    name: "profile",
    component: Profile,
    meta: { requiresAuth: true, title: "Profile" + appname },
  },
  {
    path: "/help",
    name: "help",
    component: Help,
    meta: { requiresAuth: true, title: "Help Center" + appname },
  },
  {
    path: "/dashboard",
    name: "dashboard",
    component: Dashboard,
    meta: { requiresAuth: true, title: "Dashboard" + appname },
  },
  // Academic Routes (To be implemented)
  {
    path: "/academic/faculties",
    name: "faculties",
    component: () => import("../views/academic/Faculties.vue"),
    meta: { requiresAuth: true, title: "Faculties" + appname },
  },
  // Payment Routes
  {
    path: "/payment/deposit",
    name: "payment-deposit",
    component: PaymentDeposit,
    meta: { requiresAuth: true, title: "Payment Deposit" + appname },
  },
  {
    path: "/payment/history",
    name: "payment-history",
    component: PaymentHistory,
    meta: { requiresAuth: true, title: "Payment History" + appname },
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
  linkExactActiveClass: "exact-active",
});

router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore();
  const token = authStore.token;

  // 1. If not logged in
  if (!token) {
    if (to.meta.requiresAuth) {
      return next({ name: 'login', query: { redirect: to.fullPath } });
    }
    return next();
  }

  // 2. If logged in and trying to access login
  if (to.name === 'login') {
    return next({ name: 'dashboard' });
  }

  next();
});

export default router;
