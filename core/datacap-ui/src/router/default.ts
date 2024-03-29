import LayoutContainer from '@/views/layouts/common/LayoutContainer.vue'
import ProfileContainer from '@/views/layouts/profile/LayoutContainer.vue'
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
    createAdminRouter(router)
    createPreviewRouter(router)
    createProfileRouter(router)
}

/**
 * Creates a system router and adds it to the provided router.
 *
 * @param {any} router - The router instance to which the system router will be added.
 * @return {void}
 */
const createSystemRouter = (router: any) => {
    const newRouter = {
        path: '/system',
        meta: {
            title: 'common.system',
            isRoot: true
        },
        component: LayoutContainer,
        children: [
            {
                name: 'user',
                path: 'user',
                meta: {
                    title: 'common.user',
                    isRoot: false
                },
                component: () => import('@/views/pages/system/user/UserHome.vue')
            },
            {
                name: 'role',
                path: 'role',
                meta: {
                    title: 'common.role',
                    isRoot: false
                },
                component: () => import('@/views/pages/system/role/RoleHome.vue')
            },
            {
                name: 'schedule',
                path: 'schedule',
                meta: {
                    title: 'common.schedule',
                    isRoot: false
                },
                component: () => import('@/views/pages/system/schedule/ScheduleHome.vue')
            },
            {
                name: 'function',
                path: 'function',
                meta: {
                    title: 'common.function',
                    isRoot: false
                },
                component: () => import('@/views/pages/system/function/FunctionHome.vue')
            }
        ]
    }
    router.addRoute(newRouter)
}

/**
 * Creates an admin router and adds it to the given router.
 *
 * @param {any} router - The router to which the admin router will be added.
 */
const createAdminRouter = (router: any) => {
    const newRouter = {
        path: '/admin',
        meta: {
            title: 'common.admin',
            isRoot: true
        },
        component: LayoutContainer,
        children: [
            {
                name: 'dashboard',
                path: 'dashboard',
                meta: {
                    title: 'common.dashboard',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/dashboard/DashboardHome.vue')
            },
            {
                name: 'info',
                path: 'dashboard/info/:id',
                meta: {
                    title: 'common.dashboard',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/dashboard/DashboardInfo.vue')
            },
            {
                name: 'query',
                path: 'query',
                meta: {
                    title: 'common.query',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/query/QueryHome.vue')
            },
            {
                name: 'dataset',
                path: 'dataset',
                meta: {
                    title: 'common.dataset',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/dataset/DatasetHome.vue')
            },
            {
                path: 'dataset/info/:code?',
                layout: LayoutContainer,
                meta: {
                    title: 'common.dataset',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/dataset/DatasetInfo.vue')
            },
            {
                path: 'dataset/adhoc/:code',
                layout: LayoutContainer,
                meta: {
                    title: 'common.dataset',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/dataset/DatasetAdhoc.vue')
            },
            {
                path: 'dataset/adhoc/:code/:id',
                layout: LayoutContainer,
                meta: {
                    title: 'common.dataset',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/dataset/DatasetAdhoc.vue')
            }
        ]
    }
    router.addRoute(newRouter)
}

/**
 * Creates a new route for previewing a dashboard based on the provided ID.
 *
 * @param {any} router - The router instance where the new route will be added.
 * @return {void}
 */
const createPreviewRouter = (router: any) => {
    const newRouter = {
        path: '/admin/dashboard/preview/:id',
        meta: {
            title: 'common.dashboard',
            isRoot: false
        },
        component: () => import('@/views/pages/admin/dashboard/DashboardPreview.vue')
    }
    router.addRoute(newRouter)
}

const createProfileRouter = (router: any) => {
    const newRouter = {
        path: '/admin/user',
        meta: {
            title: 'user.common.setting',
            isRoot: true
        },
        redirect: '/admin/user/profile',
        component: ProfileContainer,
        children: [
            {
                name: 'info',
                path: 'info',
                meta: {
                    title: 'user.common.info',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/user/profile/InfoHome.vue')
            },
            {
                name: 'profile',
                path: 'profile',
                meta: {
                    title: 'user.common.profile',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/user/profile/ProfileHome.vue')
            },
            {
                name: 'log',
                path: 'log',
                meta: {
                    title: 'user.common.log',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/user/profile/LogHome.vue')
            },
            {
                name: 'editor',
                path: 'editor',
                meta: {
                    title: 'user.common.editor',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/user/profile/EditorHome.vue')
            },
            {
                name: 'assistant',
                path: 'assistant',
                meta: {
                    title: 'user.common.assistant',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/user/profile/AssistantHome.vue')
            },
            {
                name: 'username',
                path: 'username',
                meta: {
                    title: 'user.common.modifyUsername',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/user/security/UsernameHome.vue')
            },
            {
                username: 'password',
                path: 'password',
                meta: {
                    title: 'user.common.modifyPassword',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/user/security/PasswordHome.vue')
            }
        ]
    }
    router.addRoute(newRouter)
}

export {
    createDefaultRouter
}
