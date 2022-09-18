<template>
  <a-menu theme="dark" mode="horizontal" :style="{ lineHeight: '64px' }">
    <a-menu-item>DataCap(incubator)</a-menu-item>
    <a-menu-item key="dashboard">
      <router-link to="/">
        <home-filled/>
        Home
      </router-link>
    </a-menu-item>
  </a-menu>
</template>

<script>
import {HomeFilled,} from '@ant-design/icons-vue';

export default {
  name: "LayoutHeader",
  components: {HomeFilled},
  computed: {}
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped></style>
