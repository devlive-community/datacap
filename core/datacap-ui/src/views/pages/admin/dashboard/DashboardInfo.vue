<template>
  <div class="w-full">
    <CircularLoading v-if="loading" :show="loading" class="mt-20"></CircularLoading>
    <div v-else>
      <DashboardEditor :elements="nodes" :source-configure="sourceConfigure"/>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import DashboardService from '@/services/dashboard'
import { ToastUtils } from '@/utils/toast'
import { useRouter } from 'vue-router'
import { DashboardModel } from '@/model/dashboard'
import DashboardEditor from '@/views/pages/admin/dashboard/components/DashboardEditor.vue'

export default defineComponent({
  name: 'DashboardInfo',
  components: {DashboardEditor, CircularLoading},
  data()
  {
    return {
      loading: false,
      saving: false,
      configure: null as DashboardModel | null,
      nodes: [],
      sourceConfigure: null as DashboardModel | null
    }
  },
  created()
  {
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
              const configure = JSON.parse(response.data.configure)
              configure.nodes?.forEach((node: any) => {
                this.nodes.push({id: node.id, type: node.type, label: node.label, position: node.position, data: node.data})
              })
              this.sourceConfigure = response.data
            }
            else {
              ToastUtils.error(response.message)
            }
          })
          .finally(() => this.loading = false)
    }
  }
})
</script>
