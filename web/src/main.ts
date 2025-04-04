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
  faSquarePlus,
  faSitemap,
  faSliders,
  faSquareCheck,
  faHammer,
  faCrown,
  faPeopleGroup,
  faCircleMinus,
  faGear,
  faMagnifyingGlass,
  faBars,
  faEyeSlash,
} from "@fortawesome/free-solid-svg-icons";

library.add(
  faUserPlus,
  faUserMinus,
  faCirclePlus,
  faCircleMinus,
  faFolderTree,
  faPenToSquare,
  faPlus,
  faMinus,
  faTrash,
  faCheck,
  faSquarePlus,
  faSitemap,
  faSliders,
  faSquareCheck,
  faHammer,
  faCrown,
  faPeopleGroup,
  faGear,
  faMagnifyingGlass,
  faBars,
  faEyeSlash
);

const app = createApp(App);
const pinia = createPinia();

app.use(router);
app.use(pinia);

app.component("font-awesome-icon", FontAwesomeIcon);

app.mount("#app");
