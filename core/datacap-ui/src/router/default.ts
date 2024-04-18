import LayoutContainer from '@/views/layouts/common/LayoutContainer.vue'
import ProfileContainer from '@/views/layouts/profile/LayoutContainer.vue'
import { TokenUtils } from '@/utils/token'
import MetadataContainer from '@/views/layouts/metadata/MetadataContainer.vue'

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
        }
        router.addRoute(indexRouter)
    }

    createSystemRouter(router)
    createAdminRouter(router)
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
            },
            {
                name: 'template',
                path: 'template',
                meta: {
                    title: 'common.template',
                    isRoot: false
                },
                component: () => import('@/views/pages/system/template/TemplateHome.vue')
            },
            {
                name: 'menu',
                path: 'menu',
                meta: {
                    title: 'common.menu',
                    isRoot: false
                },
                component: () => import('@/views/pages/system/menu/MenuHome.vue')
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
                path: 'dashboard/preview/:code',
                meta: {
                    title: 'common.dashboard',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/dashboard/DashboardPreview.vue')
            },
            {
                path: 'dashboard/info/:code?',
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
                name: 'queryHistory',
                path: 'query/:type?/:code?',
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
                path: 'dataset/info/source/:sourceCode?',
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
            },
            {
                path: 'source',
                meta: {
                    title: 'common.source',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/source/SourceHome.vue')
            },
            {
                path: 'source/:source',
                component: MetadataContainer,
                meta: {
                    title: 'common.source',
                    isRoot: false
                },
                children: [
                    {
                        path: 'd/:database/',
                        meta: {
                            title: 'common.source',
                            isRoot: false
                        },
                        component: () => import('@/views/pages/admin/source/SourceDatabase.vue')
                    },
                    {
                        path: 'd/:database/t/info/:table',
                        meta: {
                            title: 'common.source',
                            isRoot: false,
                            type: 'info'
                        },
                        component: () => import('@/views/pages/admin/source/SourceTableInfo.vue')
                    },
                    {
                        path: 'd/:database/t/structure/:table',
                        meta: {
                            title: 'common.source',
                            isRoot: false,
                            type: 'structure'
                        },
                        component: () => import('@/views/pages/admin/source/SourceTableStructure.vue')
                    },
                    {
                        path: 'd/:database/t/data/:table',
                        meta: {
                            title: 'common.source',
                            isRoot: false,
                            type: 'data'
                        },
                        component: () => import('@/views/pages/admin/source/SourceTableData.vue')
                    }
                ]
            },
            {
                path: 'history',
                meta: {
                    title: 'common.history',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/history/HistoryHome.vue')
            },
            {
                path: 'snippet',
                meta: {
                    title: 'common.snippet',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/snippet/SnippetHome.vue')
            },
            {
                path: 'report',
                meta: {
                    title: 'common.report',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/report/ReportHome.vue')
            },
            {
                path: 'pipeline',
                meta: {
                    title: 'common.pipeline',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/pipeline/PipelineHome.vue')
            },
            {
                path: 'pipeline/info/:code?',
                meta: {
                    title: 'common.pipeline',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/pipeline/PipelineInfo.vue')
            },
            {
                path: 'chat',
                meta: {
                    title: 'common.chat',
                    isRoot: false
                },
                component: () => import('@/views/pages/admin/chat/ChatHome.vue')
            }
        ]
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
