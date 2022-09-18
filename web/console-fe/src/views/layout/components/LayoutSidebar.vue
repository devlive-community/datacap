<template>
  <a-layout-sider width="200">
  </a-layout-sider>
</template>

<script lang="ts">
import { ref } from "vue";

export default {
  name: "LayoutSidebar",
  components: {},
  setup() {
    return {
      selectedKeys: ref<string[]>(["home"]),
      openKeys: ref<string[]>(["template"])
    };
  },
  computed: {}
};
</script>
