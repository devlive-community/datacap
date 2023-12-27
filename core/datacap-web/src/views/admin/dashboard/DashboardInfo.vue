<template>
  <div>
    <DashboardEditorBeta :obj-info="info"/>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue';
import DashboardEditorBeta from "@/components/editor/dashboard-beta/DashboardEditor.vue";
import DashboardService from "@/services/DashboardService";

export default defineComponent({
  name: 'DashboardInfo',
  components: {DashboardEditorBeta},
  data()
  {
    return {
      info: null
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    async handlerInitialize()
    {
      const id = this.$router.currentRoute?.value?.params['id']
      if (id) {
        try {
          const response = await DashboardService.getById(id);
          if (response.status && response.data) {
            this.info = response.data;
          }
        }
        catch (error) {
          console.error("An error occurred while getting dashboard information:", error);
        }
      }
    }
  }
});
</script>

<style scoped>
</style>
