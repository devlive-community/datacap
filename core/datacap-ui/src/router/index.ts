import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import { createAuthRoute } from '@/router/auth'

const routes: Array<RouteRecordRaw> = []

const router = createRouter({
    history: createWebHashHistory(),
    routes
});

createAuthRoute(router)

export default router
