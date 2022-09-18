<template>
  <a-menu theme="dark" mode="horizontal" :style="{ lineHeight: '64px' }">
    <a-menu-item>DataCap(incubator)</a-menu-item>
    <a-menu-item key="dashboard">
      <router-link to="/">
        <home-filled/>
        Home
      </router-link>
    </a-menu-item>
    <a-sub-menu key="admin">
      <template #icon>
        <setting-outlined/>
      </template>
      <template #title>Admin</template>
      <a-menu-item key="admin_source">
        <router-link to="/admin/source">
          <aim-outlined/>
          Source
        </router-link>
      </a-menu-item>
    </a-sub-menu>
  </a-menu>
</template>

<script>
import {AimOutlined, HomeFilled, SettingOutlined} from '@ant-design/icons-vue';

export default {
  name: "LayoutHeader",
  components: {HomeFilled, SettingOutlined, AimOutlined},
  computed: {}
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped></style>
