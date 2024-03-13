import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import { createAuthRoute } from '@/router/auth'
import { createDefaultRouter, createRemoteRouter } from '@/router/default'
import { TokenUtils } from '@/utils/token'
import { createHttpRoute } from '@/router/http'
import { ObjectUtils } from '@/utils/object'

const routes: Array<RouteRecordRaw> = []

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

createHttpRoute(router)
createAuthRoute(router)
createDefaultRouter(router)
const userMenu = TokenUtils.getUserMenu()
if (ObjectUtils.isNotEmpty(userMenu)) {
    createRemoteRouter(userMenu, router)
}

router.beforeEach((to, from, next) => {
    if (to.matched.length === 0) {
        next('/common/404')
    }
    else {
        next()
    }
})

export default router
