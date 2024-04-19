import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import { createAuthRoute } from '@/router/auth'
import { createDefaultRouter } from '@/router/default'
import { createHttpRoute } from '@/router/http'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

NProgress.configure({
    easing: 'ease',
    speed: 600,
    showSpinner: true,
    trickleSpeed: 200,
    minimum: 0.3
})

const routes: Array<RouteRecordRaw> = []

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

createHttpRoute(router)
createAuthRoute(router)
createDefaultRouter(router)

router.beforeEach((_to, _from, _next) => {
    NProgress.start()
    if (_to.matched.length === 0) {
        _next('/common/404')
    }
    else {
        _next()
    }
})

router.afterEach(() => NProgress.done())

export default router
