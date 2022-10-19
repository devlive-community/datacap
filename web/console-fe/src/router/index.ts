import LayoutContainer from "@/views/layout/Layout.vue";
import {createRouter, createWebHashHistory, RouteRecordRaw} from "vue-router";

import NProgress from "nprogress";
import "nprogress/nprogress.css";
import Common from "@/common/Common";

NProgress.configure({
  easing: 'ease',
  speed: 600,
  showSpinner: true,
  trickleSpeed: 200,
  minimum: 0.3
});

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    redirect: "/dashboard/index",
    component: LayoutContainer,
    children: []
  },
  {
    path: "/dashboard",
    name: "dashboard",
    redirect: "/dashboard/index",
    component: LayoutContainer,
    meta: {
      requireAuth: true,
    },
    children: [
      {
        path: "index",
        component: () => import("../views/pages/dashboard/DashboardConsole.vue")
      }
    ]
  },
  {
    path: "/console",
    name: "console",
    redirect: "/console/index",
    component: LayoutContainer,
    meta: {
      requireAuth: true,
    },
    children: [
      {
        path: "index",
        component: () => import("../views/pages/console/ConsoleOnline.vue")
      }
    ]
  },
  {
    path: "/admin",
    name: "admin",
    component: LayoutContainer,
    meta: {
      requireAuth: true,
    },
    children: [
      {
        path: "source",
        component: () => import("../views/pages/admin/source/SourceConsole.vue")
      },
      {
        path: "history",
        component: () => import("../views/pages/console/ConsoleHistory.vue")
      }
    ]
  },
  {
    path: "/common",
    name: "common",
    children: [
      {
        name: "routerNotFound",
        path: "not_found",
        component: () => import("../views/common/NotFound.vue")
      }
    ]
  },
  {
    path: "/auth",
    name: "auth",
    children: [
      {
        name: "signin",
        path: "signin",
        component: () => import("../views/pages/auth/AuthLogin.vue")
      }
    ]
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

const authRouter = '/auth/signin';

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
        next(authRouter);
      }
    }
    else {
      if (localStorage.getItem(Common.token) && to.path == authRouter) {
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
