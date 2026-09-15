<template>
  <a-modal v-model:open="visible"
           width="60%"
           :title="$t('report.common.list')"
           :footer="null"
           @cancel="onCancel">
    <div class="relative w-full h-full">
      <a-spin :spinning="loading">
        <div v-if="!loading" class="p-2">
          <a-checkbox-group v-model:value="report" :style="{ width: '100%' }">
            <a-row :gutter="8">
              <a-col v-for="item of data" :key="item.code" :span="8">
                <a-checkbox class="px-1 py-1 dc-chart-checkbox" :value="item.code">
                  <a-card :title="item.name">
                    <template #extra>
                      <a-tooltip v-if="item.description" :title="item.description">
                        <ShadcnIcon icon="Info" :size="20"/>
                      </a-tooltip>
                    </template>

                    <VisualView width="300px" height="250px"
                                :code="item.dataset?.code as string"
                                :configuration="JSON.parse(item.configure as string)"
                                :type="item.type"
                                :query="item.type === 'DATASET' ? JSON.parse(item.query as string) : item.query"
                                :original="item?.source?.code"/>
                  </a-card>
                </a-checkbox>
              </a-col>
            </a-row>
          </a-checkbox-group>

          <div v-if="data.length === 0" class="flex w-full items-center">
            {{ $t('common.noData') }}
          </div>
        </div>

        <div v-if="data.length > 0" class="py-2 mt-2 flex justify-end">
          <a-pagination :current="pageIndex"
                        :page-size="pageSize"
                        :total="dataCount"
                        show-size-changer
                        :page-size-options="['12', '24', '36']"
                        @change="onPageChange"
                        @show-size-change="onSizeChange"/>
        </div>
      </a-spin>
    </div>

    <template #footer>
      <a-space>
        <a-button @click="onCancel">
          {{ $t('common.cancel') }}
        </a-button>

        <a-button type="primary" :disabled="!report || report.length === 0" @click="onSubmit()">
          {{ $t('common.save') }}
        </a-button>
      </a-space>
    </template>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import ReportService from '@/services/report.ts'
import { FilterModel } from '@/model/filter.ts'
import { ReportModel } from '@/model/report.ts'
import VisualView from '@/views/components/visual/VisualView.vue'

defineOptions({ name: 'ChartContainer' })

const props = withDefaults(defineProps<{ isVisible?: boolean }>(), { isVisible: false })
const emit = defineEmits<{
  (e: 'close', value: boolean): void
  (e: 'change', value: ReportModel[]): void
}>()

const filter: FilterModel = new FilterModel()
filter.size = 12

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const loading = ref(false)
const data = ref<ReportModel[]>([])
const report = ref<any[]>([])
const pageIndex = ref(1)
const pageSize = ref(12)
const dataCount = ref(0)

const handleInitialize = () => {
  loading.value = true
  ReportService.getAll(filter)
               .then((response) => {
                 if (response.status) {
                   data.value = response.data.content
                   dataCount.value = response.data.total
                   pageSize.value = response.data.size
                   pageIndex.value = response.data.page
                 }
               })
               .finally(() => (loading.value = false))
}

const fetchData = (value: number) => {
  filter.page = value
  filter.size = pageSize.value
  handleInitialize()
}

const onPageChange = (value: number) => fetchData(value)

const onSizeChange = (_current: number, size: number) => {
  pageSize.value = size
  fetchData(pageIndex.value)
}

const onCancel = () => {
  visible.value = false
}

const onSubmit = () => {
  const nodes = data.value.filter((item) =>
      report.value.some((reportId: string) => item.code === reportId)
  )
  emit('change', nodes)
  onCancel()
}

handleInitialize()
</script>

<style scoped>
/* 让 checkbox 内容区占满整列，卡片撑满 */
.dc-chart-checkbox {
    display: flex;
    width: 100%;
    align-items: flex-start;
}

.dc-chart-checkbox :deep(.ant-checkbox + span) {
    flex: 1;
    width: 100%;
    padding-right: 0;
}
</style>
