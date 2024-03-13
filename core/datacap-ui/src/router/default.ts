import { RouterItem } from '@/model/router/item'
import { Router } from 'vue-router'
import LayoutContainer from '@/views/layouts/common/LayoutContainer.vue'
import { TokenUtils } from '@/utils/token'

const array = ['admin', 'system', 'monitor']

/**
 * Adds a route to the router and handles any errors that occur.
 *
 * @param {any} route - The route to be added to the router.
 * @param {Router} router - The router to which the route will be added.
 */
const addRouter = (route: any, router: Router) => {
    try {
        router.addRoute(route)
    }
    catch (e: any) {
        console.error(`Add route ${route} failed with ${e.message}`)
    }
}

/**
 * Generates a remote router based on the provided items array and adds it to the given router.
 *
 * @param {RouterItem[]} items - The array of router items to create the remote router from.
 * @param {Router} router - The router to add the remote router to.
 * @param {any} [parent] - The parent element for the remote router.
 */
const createRemoteRouter = (items: RouterItem[], router: Router, parent?: any) => {
    items?.forEach(item => {
        const viewAndPath = item.url.split('/')
        // Remove first, but it is empty
        viewAndPath.shift()
        const isAdmin = array.includes(viewAndPath[0]) ? true : false
        let _router
        if (item.children?.length > 0) {
            _router = {
                path: item.url,
                component: LayoutContainer,
                meta: {title: item.i18nKey},
                children: [],
                url: item.url
            }
            createRemoteRouter(item.children, router, _router)
        }
        else {
            if (viewAndPath.length < 2) {
                return
            }
            const f1 = viewAndPath[0].charAt(0).toUpperCase() + viewAndPath[0].slice(1)
            const f2 = viewAndPath[1].charAt(0).toUpperCase() + viewAndPath[1].slice(1)
            let url = `pages/default/${viewAndPath[0]}/${f1}${f2}.vue`
            if (isAdmin) {
                url = `${viewAndPath[0]}/${viewAndPath[1]}/${f1}${f2}.vue`
            }
            if (parent) {
                _router = parent
                _router.children.push({
                    path: viewAndPath[1],
                    meta: {title: item.i18nKey},
                    component: () => import('@/views/' + url)
                })
            }
            else {
                _router = {
                    path: '/' + viewAndPath[0],
                    component: LayoutContainer,
                    meta: {title: item.i18nKey},
                    children: [
                        {
                            path: viewAndPath[1],
                            meta: {title: item.i18nKey},
                            component: () => import('../views/' + url)
                        }
                    ]
                }
            }
        }
        addRouter(_router, router)
    })
}

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
}

export {
    createRemoteRouter,
    createDefaultRouter
}
