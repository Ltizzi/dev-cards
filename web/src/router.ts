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
import App from "./App.vue";
import HomeLayout from "./layouts/HomeLayout.vue";
import ScrumView from "./layouts/ScrumView.vue";
import DesignatedView from "./layouts/DesignatedView.vue";
import AllTasksView from "./layouts/AllTasksView.vue";
import ProjectSettings from "./layouts/ProjectSettings.vue";
import { checkIsModOrOwner } from "./utils/auth.utils";
import BlockedTasksView from "./layouts/BlockedTasksView.vue";
import AppSettings from "./layouts/AppSettings.vue";
import UserProfileView from "./layouts/UserProfileView.vue";
import OfflineSignup from "./components/auth/OfflineSignup.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      name: "Home",
      component: TheHome,
      children: [
        { path: "", component: HomeLayout, name: "HomeLayout" },
        { path: "/newproject", component: NewProject },
        { path: "/appConfig", component: AppSettings },
        {
          path: "/project",
          component: TheProject,
          children: [
            { path: "info", component: ProjectInfo },
            { path: "task", component: TheTask },
            { path: "scrum", component: ScrumView },
            { path: "designated", component: DesignatedView },
            { path: "tasks", component: AllTasksView },
            { path: "user", component: UserProfileView },
            {
              path: "blocked",
              component: BlockedTasksView,
              beforeEnter: (to, from, next) => {
                const workspace_id = localStorage.getItem(
                  "current_workspace_id"
                )
                  ? +JSON.parse(
                      localStorage.getItem("current_workspace_id") as string
                    )
                  : null;
                if (workspace_id && checkIsModOrOwner(workspace_id)) {
                  next();
                } else {
                  next("/");
                }
                //return checkIsModOrOwner(workspace_id) ? true : "/";
              },
            },
            {
              path: "settings",
              component: ProjectSettings,
              beforeEnter: (to, from, next) => {
                const workspace_id = to.query.id as unknown as number;

                if (checkIsModOrOwner(workspace_id)) {
                  next();
                } else {
                  next("/");
                }

                // return checkIsModOrOwner(workspace_id)
                //   ? checkIsModOrOwner(workspace_id)
                //   : "/";
              },
            },
          ],
        },
      ],
    },

    { path: "/login", name: "Login", component: Login },
    { path: "/offline", name: "OfflineSignup", component: OfflineSignup },
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
  ],
});

export default router;
