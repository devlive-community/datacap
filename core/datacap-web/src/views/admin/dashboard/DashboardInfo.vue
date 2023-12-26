<template>
  <div>
    <DashboardEditorBeta>
    </DashboardEditorBeta>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue';
import DashboardEditorBeta from "@/components/editor/dashboard-beta/DashboardEditor.vue";

export default defineComponent({
  name: 'DashboardInfo',
  components: {DashboardEditorBeta}
});
</script>

<style scoped>
</style>
