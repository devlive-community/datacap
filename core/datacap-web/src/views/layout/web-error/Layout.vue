<template>
  <div class="layout">
    <layout>
      <Header>
        <Menu mode="horizontal" theme="dark" active-name="dashboard" :style="{'z-index':99}">
          <div class="layout-logo">
            <div class="layout-logo logo-text">
              <RouterLink to="/">
                <Avatar style="width: 45px; height: 45px;" src="/static/images/logo.png"></Avatar>
              </RouterLink>
            </div>
          </div>
        </Menu>
      </Header>
      <layout-content style="background-color: #FFFFFF; padding: 10px 0px; min-height: 500px"/>
      <layout-footer/>
      <layout-affix/>
    </layout>
  </div>
</template>

<script lang="ts">
import LayoutFooter from "@/views/layout/common/components/LayoutFooter.vue";
import LayoutAffix from "@/views/layout/common/components/LayoutAffix.vue";
import LayoutContent from "@/views/layout/common/components/LayoutContent.vue";

export default {
  name: "LayoutWebErrorContainer",
  components: {LayoutContent, LayoutAffix, LayoutFooter}
}
</script>

<style scoped>
.layout {
  background: #f5f7f9;
  position: relative;
  overflow: hidden;
}
</style>
