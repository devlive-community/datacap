import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";

import Antd from "ant-design-vue";
import "ant-design-vue/dist/antd.css";

import * as Icons from "@ant-design/icons-vue";

const icons: any = Icons;

const app = createApp(App);
for (const i in icons) {
  app.component(i, icons[i]);
}
app.use(router)
app.use(Antd)
app.mount("#app");
