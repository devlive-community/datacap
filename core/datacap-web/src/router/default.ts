import LayoutContainer from "@/views/layout/common/Layout.vue";
import ProfileLayout from "@/views/layout/user-profile/ProfileLayout.vue";
import {Router} from "vue-router";
import {TokenCommon} from "@/common/TokenCommon";

interface RouterItem
{
  url: string;
  i18nKey: string;
  children: Array<RouterItem>;
}

const array = ['admin', 'system', 'monitor']

const addRouter = (route: any, router: Router) => {
  try {
    router.addRoute(route)
  }
  catch (e) {
    console.error(`Add route ${route} failed with ${e.message}`)
  }
}

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
    addRouter(_router, router)
  })
}

const createDefaultRouter = (router: any) => {
  // Only when the token is valid, the redirection route will be built
  if (TokenCommon.getAuthUser() !== undefined) {
    const indexRouter = {
      path: '/',
      redirect: '/dashboard/index',
      component: LayoutContainer
    };
    router.addRoute(indexRouter)
  }
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
            component: () => import("@/views/user/profile/ProfileIndex.vue")
          },
          {
            path: 'public',
            meta: {title: 'setting.profile'},
            component: () => import("@/views/user/profile/ProfilePublic.vue")
          },
          {
            path: 'log',
            meta: {title: 'setting.log'},
            component: () => import("@/views/user/profile/ProfileLog.vue")
          },
          {
            path: 'account',
            meta: {title: 'setting.accountSetting'},
            component: () => import("@/views/user/profile/ProfileAccount.vue")
          }
        ]
      }
    ]
  }
  router.addRoute(userRouters)
  router.addRoute({
    path: "/admin",
    component: LayoutContainer,
    meta: {title: 'common.admin'},
    children: [
      {
        path: "source/:id/manager",
        meta: {title: 'common.source'},
        layout: LayoutContainer,
        component: () => import("../views/admin/source/SourceManager.vue")
      }
    ]
  })
  router.addRoute({
    path: "/preview",
    meta: {title: 'common.preview'},
    children: [
      {
        path: "dashboard/:id",
        meta: {title: 'common.dashboard'},
        component: () => import("@/views/user/console/components/DashboardPreview.vue")
      }
    ]
  })
  router.addRoute({
    path: "/admin",
    meta: {title: 'common.dataset'},
    component: LayoutContainer,
    children: [
      {
        path: "dataset/create",
        layout: LayoutContainer,
        meta: {title: 'common.dataset'},
        component: () => import("@/views/admin/dataset/DatasetInfo.vue")
      },
      {
        path: "dataset/adhoc/:code",
        layout: LayoutContainer,
        meta: {title: 'common.dataset'},
        component: () => import("@/views/admin/dataset/DatasetAdhoc.vue")
      },
      {
        path: "dataset/adhoc/:code/:id",
        layout: LayoutContainer,
        meta: {title: 'common.dataset'},
        component: () => import("@/views/admin/dataset/DatasetAdhoc.vue")
      }
    ]
  })
  router.addRoute({
    path: "/admin",
    meta: {title: 'common.dashboard'},
    component: LayoutContainer,
    children: [
      {
        path: "dashboard/create",
        layout: LayoutContainer,
        meta: {title: 'common.dashboard'},
        component: () => import("@/views/admin/dashboard/DashboardInfo.vue")
      },
      {
        path: "dashboard/edit/:id",
        layout: LayoutContainer,
        meta: {title: 'common.dashboard'},
        component: () => import("@/views/admin/dashboard/DashboardInfo.vue")
      }
    ]
  })
  const chatRouters = {
    path: "/chat",
    component: LayoutContainer,
    meta: {title: 'common.chat'},
    children: [
      {
        path: 'chatgpt',
        meta: {title: 'common.chatgpt'},
        component: () => import("@/views/common/chat/ChatGPT.vue")
      }
    ]
  }
  router.addRoute(chatRouters)
}

export {
  createDefaultRouter,
  createRemoteRouter
}
