<template>
  <div>
    <a-layout-header>
      <LayoutHeader />
    </a-layout-header>
    <a-layout class="layout">
      <a-layout-content style="padding: 10px">
        <LayoutContent />
      </a-layout-content>
    </a-layout>
    <LayoutFooter />
    <div style="float: right; margin-right: 60px;">
      <a-affix :offsetBottom="20">
        <a-dropdown placement="top">
          <a-button type="primary" shape="circle">
            <template #icon>
              <github-outlined />
            </template>
          </a-button>
          <template #overlay>
            <a-menu>
              <a-menu-item>
                <a target="_blank" href="https://github.com/EdurtIO/incubator-datacap/fork">
                  <fork-outlined /> Fork
                </a>
              </a-menu-item>
              <a-menu-item>
                <a target="_blank" href="https://github.com/EdurtIO/incubator-datacap/stargazers">
                  <fork-outlined /> Star
                </a>
              </a-menu-item>
              <a-menu-item>
                <a target="_blank" href="https://github.com/EdurtIO/incubator-datacap">
                  <code-outlined /> GitHub
                </a>
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </a-affix>
    </div>
  </div>
</template>

<script lang="ts">
import LayoutContent from "@/views/layout/components/LayoutContent.vue";
import LayoutFooter from "@/views/layout/components/LayoutFooter.vue";
import LayoutHeader from "@/views/layout/components/LayoutHeader.vue";

export default {
  name: "LayoutContainer",
  components: { LayoutHeader, LayoutContent, LayoutFooter },
  computed: {}
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
