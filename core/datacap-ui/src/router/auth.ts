import { Router } from 'vue-router';

const createAuthRoute = (router: Router) => {
    router.addRoute({
        path: '/auth',
        name: 'auth',
        children: [
            {
                name: 'signin',
                path: 'signin',
                component: () => import('@/views/pages/auth/signin/AuthSignin.vue')
            }
        ]
    })
}

export {
    createAuthRoute
}
