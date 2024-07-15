import { createRouter, createWebHistory } from "vue-router";
import TheHome from "./pages/TheHome.vue";
import Login from "./components/auth/Login.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      name: "Home",
      component: TheHome,
    },
    { path: "/login", name: "Login", component: Login },
  ],
});

export default router;
