import {
  createRouter,
  createWebHistory,
  NavigationGuardNext,
  RouteLocationNormalized,
  RouteRecordRaw,
} from "vue-router";
import HomeView from "../views/HomeView.vue";
import JobsView from "../views/JobsView.vue";
import LoginRedirectView from "../views/LoginRedirectView.vue";
import CandidateCenterView from "../views/CandidateCenterView.vue";
import { openAuthModal } from "@/composables/useAuthModal";
import EnterpriseCenterView from "../views/EnterpriseCenterView.vue";
import AdminCenterView from "../views/AdminCenterView.vue";
import { getAccessToken, getUserType } from "@/utils/auth";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/jobs",
    name: "jobs",
    component: JobsView,
  },
  {
    path: "/login",
    name: "login",
    component: LoginRedirectView,
    beforeEnter: (to, from, next) => {
      openAuthModal({
        tab: typeof to.query.tab === "string" ? to.query.tab : undefined,
        redirect:
          typeof to.query.redirect === "string" ? to.query.redirect : undefined,
      });
      if (from.matched.length) {
        next(false);
      } else {
        next({ path: "/", replace: true });
      }
    },
  },
  {
    path: "/candidate",
    name: "candidate",
    component: CandidateCenterView,
    meta: {
      requiresAuth: true,
      userType: "CANDIDATE",
    },
  },
  {
    path: "/enterprise",
    name: "enterprise",
    component: EnterpriseCenterView,
    meta: {
      requiresAuth: true,
      userType: "ENTERPRISE",
    },
  },
  {
    path: "/admin",
    name: "admin",
    component: AdminCenterView,
    meta: {
      requiresAuth: true,
      userType: "ADMIN",
    },
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

router.beforeEach(
  (
    to: RouteLocationNormalized,
    _from: RouteLocationNormalized,
    next: NavigationGuardNext
  ) => {
    const token = getAccessToken();
    const userType = getUserType();
    const requiresAuth = Boolean(to.meta.requiresAuth);
    if (requiresAuth && !token) {
      openAuthModal({ redirect: to.fullPath });
      next(false);
      return;
    }
    const routeUserType = to.meta.userType as string | undefined;
    if (routeUserType && routeUserType !== userType) {
      next(
        userType === "ENTERPRISE"
          ? "/enterprise"
          : userType === "CANDIDATE"
          ? "/candidate"
          : userType === "ADMIN"
          ? "/admin"
          : "/"
      );
      return;
    }
    next();
  }
);

export default router;
