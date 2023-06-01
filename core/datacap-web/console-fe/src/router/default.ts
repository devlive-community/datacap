import LayoutContainer from "@/views/layout/common/Layout.vue";
import ProfileLayout from "@/views/pages/profile/layout/ProfileLayout.vue";
import {Router} from "vue-router";

interface RouterItem
{
  url: string;
  i18nKey: string;
  children: Array<RouterItem>;
}

const array = ['admin', 'system', 'monitor']

const createRemoteRouter = (items: RouterItem[], router: Router, parent?: any) => {
  items?.forEach(item => {
    const viewAndPath = item.url.split('/')
    // Remove first, but it is empty
    viewAndPath.shift()
    console.log(viewAndPath[0])
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
      let url = `user/${viewAndPath[0]}/${f1}${f2}.vue`
      if (isAdmin) {
        url = `${viewAndPath[0]}/${viewAndPath[1]}/${f1}${f2}.vue`
      }
      if (parent) {
        _router = parent
        _router.children.push({
          path: viewAndPath[1],
          meta: {title: item.i18nKey},
          component: () => import(`@/views/${url}`)
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
              component: () => import(`@/views/${url}`)
            }
          ]
        }
      }
    }
    router.addRoute(_router)
  })
}

const createDefaultRouter = (router: any) => {
  const userRouters = {
    path: '/profile',
    redirect: '/profile/index',
    component: LayoutContainer,
    meta: {title: 'common.profile'},
    children: [
      {
        path: '',
        component: ProfileLayout,
        children: [
          {
            path: 'index',
            meta: {title: 'common.profile'},
            component: () => import("../views/pages/profile/ProfileIndex.vue")
          },
          {
            path: 'public',
            meta: {title: 'common.profile'},
            component: () => import("../views/pages/profile/ProfilePublic.vue")
          },
          {
            path: 'log',
            meta: {title: 'common.log'},
            component: () => import("../views/pages/profile/ProfileLog.vue")
          },
          {
            path: 'chatgpt',
            meta: {title: 'common.profile'},
            component: () => import("../views/pages/profile/ProfileChatGPT.vue")
          },
          {
            path: 'account',
            meta: {title: 'common.log'},
            component: () => import("../views/pages/profile/ProfileAccount.vue")
          }
        ]
      }
    ]
  }
  router.addRoute(userRouters)
}

export {
  createDefaultRouter,
  createRemoteRouter
}
