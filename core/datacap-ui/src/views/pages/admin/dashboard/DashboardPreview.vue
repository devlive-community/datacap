<template>
  <div class="relative min-h-screen">
    <a-spin :spinning="loading">
      <DashboardView v-if="data && !loading" :layouts="JSON.parse(String(data.configure))"/>

      <a-alert v-else-if="!loading" type="error" :message="$t('dashboard.tip.notFound').replace('$VALUE', String(route.currentRoute?.value?.params['code']))"/>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import DashboardService from '@/services/dashboard'
import { useRouter } from 'vue-router'
import DashboardView from '@/views/pages/admin/dashboard/components/DashboardView.vue'
import { DashboardModel } from '@/model/dashboard'

defineOptions({ name: 'DashboardPreview' })

const route = useRouter()

const loading = ref(true)
const data = ref<DashboardModel | null>(null)

const handlerInitialize = () => {
  loading.value = true
  const params = route.currentRoute.value.params
  DashboardService.getByCode(params['code'] as string)
                  .then((response) => {
                    if (response.status) {
                      data.value = response.data
                    }
                  })
                  .finally(() => (loading.value = false))
}

handlerInitialize()
</script>
