import {createRouter, createWebHistory, RouteRecordRaw} from "vue-router";
import LayoutContainer from "@/views/layout/Layout.vue";

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
    children: [
      {
        path: "index",
        component: () => import("../views/pages/dashboard/DashboardConsole.vue")
      }
    ]
  },
  {
    path: "/admin",
    name: "admin",
    component: LayoutContainer,
    children: [
      {
        path: "source",
        component: () => import("../views/pages/admin/source/SourceConsole.vue")
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
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  if (to.matched.length === 0) {
    next({name: "routerNotFound"})
  }
  else {
    next()
  }
})

export default router;
