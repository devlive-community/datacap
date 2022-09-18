<template>
  <a-layout-header>
    <LayoutHeader/>
  </a-layout-header>
  <a-layout class="layout">
    <a-layout-content style="padding: 10px">
      <LayoutContent/>
    </a-layout-content>
  </a-layout>
  <LayoutFooter/>
</template>

<script>
import LayoutFooter from "@/views/layout/components/LayoutFooter";
import LayoutContent from "@/views/layout/components/LayoutContent";
import LayoutHeader from "@/views/layout/components/LayoutHeader";

export default {
  name: "LayoutContainer",
  components: {LayoutHeader, LayoutContent, LayoutFooter},
  computed: {}
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped></style>
