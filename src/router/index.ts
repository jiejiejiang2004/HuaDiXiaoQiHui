import {
  createRouter,
  createWebHistory,
  NavigationGuardNext,
  RouteLocationNormalized,
  RouteRecordRaw,
} from "vue-router";
import HomeView from "../views/HomeView.vue";
import LoginView from "../views/LoginView.vue";
import CandidateCenterView from "../views/CandidateCenterView.vue";
import EnterpriseCenterView from "../views/EnterpriseCenterView.vue";
import { getAccessToken, getUserType } from "@/utils/auth";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/login",
    name: "login",
    component: LoginView,
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
      next("/login");
      return;
    }
    const routeUserType = to.meta.userType as string | undefined;
    if (routeUserType && routeUserType !== userType) {
      next(
        userType === "ENTERPRISE"
          ? "/enterprise"
          : userType === "CANDIDATE"
          ? "/candidate"
          : "/login"
      );
      return;
    }
    next();
  }
);

export default router;
