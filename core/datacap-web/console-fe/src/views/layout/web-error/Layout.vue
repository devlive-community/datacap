<template>
  <div class="layout">
    <layout>
      <layout-content style="background-color: #FFFFFF; padding: 0; min-height: 500px"/>
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
