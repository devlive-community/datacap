import { createApp } from 'vue'
import './style.css'
import './index.css'
import App from './App.vue'
import router from '@/router'
import i18n from '@/i18n/I18n'
import store from '@/utils/store'
import { createIcons } from '@/fontawesome'

const app = createApp(App)
createIcons(app)
// Disable warnings
app.config.warnHandler = () => null
app.use(router)
app.use(i18n)
app.use(store)
app.provide('$t', i18n.global.t).mount('#app')
