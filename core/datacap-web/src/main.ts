import {createApp} from "vue";
import App from "./App.vue";
import router from "./router";

import ViewUIPlus from 'view-ui-plus';
import 'view-ui-plus/dist/styles/viewuiplus.css';

import i18n from "@/i18n/I18n";
import {createIcons} from "@/fontawesome";

const app = createApp(App);
// Import fa icons
createIcons(app);
// Disable warnings
app.config.warnHandler = () => null;
app.use(router);
app.use(ViewUIPlus);
app.use(i18n);
app.mount("#app");
