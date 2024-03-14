import LayoutContainer from '@/views/layouts/common/LayoutContainer.vue'
import { TokenUtils } from '@/utils/token'

/**
 * Create a default router
 *
 * @param {any} router - the router object
 * @return {void}
 */
const createDefaultRouter = (router: any) => {
    // Only when the token is valid, the redirection route will be built
    if (TokenUtils.getAuthUser() !== undefined) {
        const indexRouter = {
            path: '/',
            redirect: '/home',
            component: LayoutContainer,
            children: [
                {
                    name: 'home',
                    path: 'home',
                    component: () => import('@/views/pages/default/dashboard/DashboardHome.vue')
                }
            ]
        };
        router.addRoute(indexRouter)
    }

    createSystemRouter(router)
}

const createSystemRouter = (router: any) => {
    const newRouter = {
        path: '/system',
        component: LayoutContainer,
        children: [
            {
                name: 'user',
                path: 'user',
                component: () => import('@/views/pages/system/user/UserHome.vue')
            },
            {
                name: 'role',
                path: 'role',
                component: () => import('@/views/pages/system/role/RoleHome.vue')
            }
        ]
    };
    router.addRoute(newRouter)
}

export {
    createDefaultRouter
}
