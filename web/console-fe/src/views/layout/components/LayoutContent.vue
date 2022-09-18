<template>
  <transition name="fade" mode="out-in">
    <router-view></router-view>
  </transition>
</template>

<script>
export default {
  name: "LayoutContent",
  computed: {}
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped></style>
