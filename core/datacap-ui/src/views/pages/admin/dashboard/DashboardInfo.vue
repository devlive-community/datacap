<template>
  <div class="relative min-h-screen">
    <a-spin :spinning="loading">
      <div v-if="!loading">
        <DashboardEditor v-if="version === '1.0'" :info="dataInfo"/>

        <DashboardEditorV2 v-else :info="dataInfo"/>
      </div>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import DashboardService from '@/services/dashboard'
import { DashboardModel } from '@/model/dashboard'
import DashboardEditor from '@/views/pages/admin/dashboard/components/DashboardEditor.vue'
import DashboardEditorV2 from '@/views/pages/admin/dashboard/components/DashboardEditorV2.vue'

defineOptions({ name: 'DashboardInfo' })

const route = useRoute()
const loading = ref(false)
const dataInfo = ref<DashboardModel | null>(null)
const version = ref('1.0')

const handleInitialize = async () => {
  const code = route.params.code as string

  if (!code) {
    dataInfo.value = null
    return
  }

  try {
    loading.value = true
    const response = await DashboardService.getByCode(code)
    if (response.status) {
      dataInfo.value = response.data
      version.value = response.data.version
    }
    else {
      message.error(response.message)
    }
  }
  catch (error) {
    console.error('Failed to fetch dashboard:', error)
    message.error('Failed to load dashboard data')
  }
  finally {
    loading.value = false
  }
}

onMounted(() => {
  handleInitialize()
})
</script>
