import { Router } from 'vue-router'

const createAuthRoute = (router: Router) => {
    router.addRoute({
        path: '/auth',
        name: 'auth',
        children: [
            {
                name: 'signin',
                path: 'signin',
                component: () => import('@/views/pages/auth/signin/AuthSignin.vue')
            },
            {
                name: 'signup',
                path: 'signup',
                component: () => import('@/views/pages/auth/signup/AuthSignup.vue')
            }
        ]
    })
}

export {
    createAuthRoute
}
