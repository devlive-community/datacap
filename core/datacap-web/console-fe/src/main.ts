import {createApp} from "vue";
import App from "./App.vue";
import router from "./router";

import ViewUIPlus from 'view-ui-plus';
import 'view-ui-plus/dist/styles/viewuiplus.css';

import VMdEditor from '@kangc/v-md-editor';
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github';
import '@kangc/v-md-editor/lib/theme/style/github.css';

VMdPreview.use(githubTheme);

import i18n from "@/i18n/I18n";

const app = createApp(App);
app.use(router);
app.use(ViewUIPlus);
app.use(i18n);
app.use(VMdEditor);
app.use(VMdPreview);
app.mount("#app");
