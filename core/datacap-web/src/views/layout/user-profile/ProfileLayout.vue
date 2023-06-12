<template>
  <div>
    <Layout class="content">
      <Layout class="content" style="padding-top: 15px;">
        <Sider hide-trigger class="content">
          <ProfileLayoutSidebar/>
        </Sider>
        <Layout class="content" :style="{padding: '0 12px 12px'}">
          <ProfileLayoutContent/>
        </Layout>
      </Layout>
    </Layout>
  </div>
</template>

<script lang="ts">
import ProfileLayoutSidebar from "@/views/layout/user-profile/components/ProfileLayoutSidebar.vue";
import ProfileLayoutContent from "@/views/layout/user-profile/components/ProfileLayoutContent.vue";

export default {
  name: "ProfileLayout",
  components: {ProfileLayoutSidebar, ProfileLayoutContent},
}
</script>
<style scoped>
.content {
  background-color: #FFFFFF;
}
</style>
