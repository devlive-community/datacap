<template>
  <div style="background-color: #FFFFFF;">
    <Breadcrumb style="margin: 10px 20px;">
      <BreadcrumbItem to="/">{{ $t('common.home') }}</BreadcrumbItem>
      <BreadcrumbItem v-for="(item, index) in breadcrumbs"
                      :key="index"
                      :to="item.path">
        {{ $t(`${item.meta.title}`) }}
      </BreadcrumbItem>
    </Breadcrumb>
  </div>
</template>

<script>
export default {
  name: "LayoutBreadcrumb",
  data() {
    return {
      breadcrumbs: [],
    };
  },
  watch: {
    $route() {
      this.getBreadcrumb();
    },
  },
  methods: {
    isHome(route) {
      return route.meta.title === "common.home";
    },
    getBreadcrumb() {
      let matched = this.$route['matched'];
      if (!this.isHome(matched[0])) {
        matched = [].concat(matched);
      }
      this.breadcrumbs = matched;
    },
  },
  created() {
    this.getBreadcrumb();
  },
};
</script>
