import {createApp} from "vue";
import App from "./App.vue";
import router from "./router";

import ViewUIPlus from 'view-ui-plus';
import 'view-ui-plus/dist/styles/viewuiplus.css';

import i18n from "@/i18n/I18n";

const app = createApp(App);
app.use(router);
app.use(ViewUIPlus);
app.use(i18n);
app.mount("#app");
