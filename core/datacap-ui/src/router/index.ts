import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import { createAuthRoute } from '@/router/auth'
import { createDefaultRouter } from '@/router/default'
import { createHttpRoute } from '@/router/http'

const routes: Array<RouteRecordRaw> = []

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

createHttpRoute(router)
createAuthRoute(router)
createDefaultRouter(router)

router.beforeEach((_to, _from, _next) => {
    if (_to.matched.length === 0) {
        _next('/common/404')
    }
    else {
        _next()
    }
})

export default router
