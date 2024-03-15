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

router.beforeEach((to, _from, next) => {
    if (to.matched.length === 0) {
        next('/common/404')
    }
    else {
        next()
    }
})

export default router
