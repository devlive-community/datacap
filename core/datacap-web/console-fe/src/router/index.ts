import {createRouter, createWebHashHistory, RouteRecordRaw} from "vue-router";

import NProgress from "nprogress";
import "nprogress/nprogress.css";
import Common from "@/common/Common";
import LayoutWebErrorContainer from "@/views/layout/web-error/Layout.vue";
import {createDefaultRouter, createRemoteRouter} from "@/router/default";

NProgress.configure({
  easing: 'ease',
  speed: 600,
  showSpinner: true,
  trickleSpeed: 200,
  minimum: 0.3
});

const commonRouters = [
  {
    path: "/common",
    name: "common",
    component: LayoutWebErrorContainer,
    children: [
      {
        name: "routerNotFound",
        path: "not_found",
        component: () => import("../views/common/http-code/NotFound.vue")
      },
      {
        name: "routerNotAuthorized",
        path: "not_authorized",
        component: () => import("../views/common/http-code/NotAuthorized.vue")
      },
      {
        name: "routerNotNetwork",
        path: "not_network",
        component: () => import("../views/common/http-code/NotNetwork.vue")
      }
    ]
  }
]
const authRouters = [
  {
    path: "/auth",
    name: "auth",
    children: [
      {
        name: "signin",
        path: "signin",
        component: () => import("@/views/common/auth/AuthSignin.vue")
      },
      {
        name: "signup",
        path: "signup",
        component: () => import("@/views/common/auth/AuthSignup.vue")
      }
    ]
  }
]

const routes: Array<RouteRecordRaw> = []
routes.push(...commonRouters, ...authRouters)

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

const authRouterWith = '/auth/sign';

createDefaultRouter(router)
createRemoteRouter(JSON.parse(localStorage.getItem(Common.menu)), router)

router.beforeEach((to, from, next) => {
  NProgress.start();
  if (to.matched.length === 0) {
    next({name: "routerNotFound"})
  }
  else {
    if (to.meta.requireAuth) {
      if (localStorage.getItem(Common.token)) {
        next();
      }
      else {
        next('/auth/signin');
      }
    }
    else {
      if (localStorage.getItem(Common.token) && to.path.startsWith(authRouterWith)) {
        next('/');
      }
      else {
        next();
      }
    }
  }
})

router.afterEach(() => {
  NProgress.done();
});

export default router;
