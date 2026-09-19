import { createApp } from 'vue'
import { createPinia } from 'pinia'
import './style.css'
import './assets/utilities.local.css'
import App from './App.vue'
import router from '@/router'
import i18n from '@/i18n/I18n'
import { createIcons } from '@/fontawesome'
import Antd from 'ant-design-vue'

const app = createApp(App)
createIcons(app)
// Disable warnings
app.config.warnHandler = () => null
app.use(router)
app.use(i18n)
app.use(createPinia())
// 全站统一使用 ant-design-vue；图标一律直接使用 @ant-design/icons-vue 组件，
// antd v4 为 CSS-in-JS，无需全局 reset.css
app.use(Antd)
app.provide('$t', i18n.global.t).mount('#app')
