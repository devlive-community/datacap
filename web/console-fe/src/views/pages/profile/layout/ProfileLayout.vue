<template>
  <div>
    <a-layout style="background: #ffffff;">
      <a-layout-sider width="200" style="background: #ffffff;">
        <ProfileLayoutSidebar></ProfileLayoutSidebar>
      </a-layout-sider>
      <a-layout style="margin-left: 10px;">
        <a-layout-content style="background: #ffffff;">
          <ProfileLayoutContent></ProfileLayoutContent>
        </a-layout-content>
      </a-layout>
    </a-layout>
  </div>
</template>

<script lang="ts">
import ProfileLayoutSidebar from "@/views/pages/profile/layout/components/ProfileLayoutSidebar.vue";
import ProfileLayoutContent from "@/views/pages/profile/layout/components/ProfileLayoutContent.vue";

export default {
  name: "ProfileLayout",
  components: {ProfileLayoutContent, ProfileLayoutSidebar},
  computed: {},
  methods: {}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
</style>
