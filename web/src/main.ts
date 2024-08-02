import { createApp } from "vue";
import "./style.css";
import App from "./App.vue";
import { createPinia } from "pinia";
import router from "./router";
import { library } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

import {
  faUserPlus,
  faUserMinus,
  faCirclePlus,
  faFolderTree,
  faPenToSquare,
  faPlus,
  faMinus,
  faTrash,
  faCheck,
} from "@fortawesome/free-solid-svg-icons";

library.add(
  faUserPlus,
  faUserMinus,
  faCirclePlus,
  faFolderTree,
  faPenToSquare,
  faPlus,
  faMinus,
  faTrash,
  faCheck
);

const app = createApp(App);
const pinia = createPinia();

app.use(router);
app.use(pinia);

app.component("font-awesome-icon", FontAwesomeIcon);

app.mount("#app");
