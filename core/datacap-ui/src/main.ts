import { createApp } from 'vue'
import { createPinia } from 'pinia'
import './style.css'
import App from './App.vue'
import router from '@/router'
import i18n from '@/i18n/I18n'
import { createIcons } from '@/fontawesome'
import Antd from 'ant-design-vue'
// @ts-ignore
import ShadcnViewUI from 'view-shadcn-ui'

const app = createApp(App)
createIcons(app)
// Disable warnings
app.config.warnHandler = () => null
app.use(router)
app.use(i18n)
app.use(createPinia())
// ant-design-vue 与旧的 view-shadcn-ui 暂时共存：a-* 用 antd，Shadcn* 仍走旧库，
// 逐页迁移完成后再移除 view-shadcn-ui。antd v4 为 CSS-in-JS，无需全局引入 reset.css，
// 以免影响尚未迁移的旧库页面（迁移收尾时再统一引入 reset）。
app.use(Antd)
app.use(ShadcnViewUI)
app.provide('$t', i18n.global.t).mount('#app')
