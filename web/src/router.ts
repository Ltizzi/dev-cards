import { createRouter, createWebHistory } from "vue-router";
import TheHome from "./pages/TheHome.vue";
import Login from "./components/auth/Login.vue";
import SignUp from "./layouts/SignUp.vue";
import Register from "./components/auth/Register.vue";
import ProfileSignUp from "./components/auth/ProfileSignUp.vue";
import NewProject from "./components/project/newProject.vue";

import TheProject from "./pages/TheProject.vue";
import ProjectInfo from "./components/project/ProjectInfo.vue";
import TheTask from "./pages/TheTask.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      name: "Home",
      component: TheHome,
    },
    {
      path: "/project",
      component: TheProject,
      children: [{ path: "info", component: ProjectInfo }],
    },

    { path: "/login", name: "Login", component: Login },
    {
      path: "/signup",
      name: "SignUp",
      component: SignUp,
      children: [
        { path: "register", name: "Register", component: Register },
        {
          path: "complete",
          name: "CompleteProfile",
          component: ProfileSignUp,
        },
        { path: "project", component: NewProject },
      ],
    },
    { path: "/task", component: TheTask },
  ],
});

export default router;
