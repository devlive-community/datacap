<template>
  <div class="w-full">
    <Loader2 v-if="loading" class="w-full justify-center animate-spin mt-10"/>
    <DashboardView v-else-if="data" :layouts="JSON.parse(data.configure as string)"/>
    <Alert v-else type="error" :title="$t('dashboard.tip.notFound').replace('$VALUE', $router.currentRoute?.value?.params['code'] as string)"/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Loader2 } from 'lucide-vue-next'
import DashboardService from '@/services/dashboard'
import { useRouter } from 'vue-router'
import DashboardView from '@/views/pages/admin/dashboard/components/DashboardView.vue'
import { DashboardModel } from '@/model/dashboard'
import Alert from '@/views/ui/alert'

export default defineComponent({
  name: 'DashboardPreview',
  components: {
    Alert,
    DashboardView,
    Loader2
  },
  data()
  {
    return {
      loading: true,
      data: null as DashboardModel | null
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
      DashboardService.getByCode(params['code'] as string)
                      .then(response => {
                        if (response.status) {
                          this.data = response.data
                        }
                      })
                      .finally(() => this.loading = false)
    }
  }
})
</script>
