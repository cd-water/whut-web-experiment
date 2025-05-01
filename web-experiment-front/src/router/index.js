import { createRouter, createWebHistory } from "vue-router";

import LoginView from "@/views/login/index.vue";
import LayoutView from "@/views/layout/index.vue";
import HomeView from "@/views/home/index.vue";
import UserView from "@/views/user/index.vue";
import EssayView from "@/views/essay/index.vue";

const routes = [
  {
    path: "/",
    name: "",
    component: LayoutView,
    redirect: "/home", //重定向
    children: [
      { path: "/home", component: HomeView },
      { path: "/user", component: UserView },
      { path: "/essay", component: EssayView },
    ],
  },
  { path: "/login", component: LoginView },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes,
});

export default router;
