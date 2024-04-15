<template>
  <div class="w-full">
    <CircularLoading v-if="loading" :show="loading" class="mt-20"></CircularLoading>
    <div v-else>
      <DashboardEditor :info="dataInfo"/>
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
  components: { DashboardEditor, CircularLoading },
  data()
  {
    return {
      loading: false,
      saving: false,
      dataInfo: null as DashboardModel | null
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      const router = useRouter()
      const params = router.currentRoute.value.params
      const code = params['code'] as string
      if (code) {
        this.loading = true
        DashboardService.getByCode(code)
                        .then(response => {
                          if (response.status) {
                            this.dataInfo = response.data
                          }
                          else {
                            ToastUtils.error(response.message)
                          }
                        })
                        .finally(() => this.loading = false)
      }
    }
  }
})
</script>
