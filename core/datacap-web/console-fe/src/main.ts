import {createApp} from "vue";
import App from "./App.vue";
import router from "./router";

import ViewUIPlus from 'view-ui-plus';
import 'view-ui-plus/dist/styles/viewuiplus.css';

import {library} from '@fortawesome/fontawesome-svg-core';
import {faFilter} from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeIcon} from '@fortawesome/vue-fontawesome';
import i18n from "@/i18n/I18n";

library.add(faFilter);

const app = createApp(App);

// Disable warnings
app.config.warnHandler = () => null;
app.use(router);
app.use(ViewUIPlus);
app.use(i18n);
app.component("FontAwesomeIcon", FontAwesomeIcon);
app.mount("#app");
