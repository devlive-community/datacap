import { Router } from 'vue-router'

const createHttpRoute = (router: Router) => {
    router.addRoute({
        path: '/common',
        name: 'common',
        children: [
            {
                name: '404',
                path: '404',
                component: () => import('@/views/pages/common/error/NotFound.vue')
            }
        ]
    })
}

export {
    createHttpRoute
}
