<template>
  <div :style="{height: height + 'px'}">
    <CircularLoading v-if="loading"
                     :show="loading">
    </CircularLoading>
    <DashboardView v-else-if="data"
                   style="margin-top: 10px;"
                   :height="height - 20"
                   :configure="JSON.parse(data.configure)">
    </DashboardView>
    <Result v-else
            style="margin-top: 80px;"
            type="error"
            title="Not Found">
    </Result>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import {useRouter} from "vue-router";
import DashboardService from "@/services/DashboardService";
import DashboardView from "@/components/editor/dashboard/DashboardView.vue";

export default defineComponent({
  name: "DashboardPreview",
  components: {DashboardView, CircularLoading},
  data()
  {
    return {
      loading: false,
      height: 500,
      data: null
    }
  },
  created()
  {
    this.height = window.innerHeight
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      const router = useRouter()
      const params = router.currentRoute.value.params
      DashboardService.getById(params['id'] as unknown as number)
        .then(response => {
          if (response.status) {
            this.data = response.data
          }
        })
        .finally(() => this.loading = false)
    }
  }
});
</script>
<style scoped>
:deep(.ivu-modal-body) {
  padding: 0;
}
</style>
