<template>
  <a-modal v-model:open="visible"
           width="60%"
           :title="`[ ${ info?.name } ] ${ $t('dataset.common.history') }`"
           :footer="null">
    <a-spin :spinning="loading">
      <a-table size="small"
               :columns="historyHeaders"
               :data-source="data"
               :pagination="false"
               row-key="id"
               :scroll="{ x: 'max-content' }">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'state'">
            <a-popover v-if="hasMessage(record)">
              <template #content>
                <div class="p-2 w-full overflow-x-auto">{{ record?.message }}</div>
              </template>
              <a-tag :color="Common.getColor(record?.state)">{{ getStateText(record?.state) }}</a-tag>
            </a-popover>
            <a-tag v-else :color="Common.getColor(record?.state)">{{ getStateText(record?.state) }}</a-tag>
          </template>

          <template v-else-if="column.key === 'progress'">
            <a-progress :percent="toPercent(record?.progress)" size="small" style="min-width: 120px;"/>
          </template>

          <template v-else-if="column.key === 'action'">
            <div class="flex gap-1">
              <a-button size="small" type="primary" @click="onViewLog(record)">
                {{ $t('dataset.history.viewLog') }}
              </a-button>
              <a-button v-if="record?.executorConfigure" size="small" @click="onViewConfigure(record)">
                {{ $t('dataset.history.viewConfigure') }}
              </a-button>
              <a-button v-if="isStoppable(record)"
                        size="small"
                        danger
                        :loading="stoppingId === record.id"
                        @click="onStop(record)">
                {{ $t('dataset.history.stop') }}
              </a-button>
            </div>
          </template>
        </template>
      </a-table>

      <div v-if="data.length > 0" class="py-2 flex justify-end">
        <a-pagination :current="pageIndex"
                      :page-size="pageSize"
                      :total="dataCount"
                      show-size-changer
                      :page-size-options="['10', '20', '50']"
                      @change="onPageChange"
                      @show-size-change="onSizeChange"/>
      </div>
    </a-spin>

    <DatasetHistoryLogger v-if="loggerVisible"
                          :is-visible="loggerVisible"
                          :info="loggerInfo"
                          @close="onLoggerClose"/>

    <a-modal v-model:open="configureVisible"
             width="50%"
             :title="$t('dataset.history.configureTitle')"
             :footer="null">
      <pre class="text-xs whitespace-pre-wrap break-all p-2 bg-gray-50 dark:bg-gray-900 rounded">{{ formatJson(configureInfo) }}</pre>
    </a-modal>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { FilterModel } from '@/model/filter'
import { useDatasetHeaders } from './DatasetUtils'
import { DatasetModel } from '@/model/dataset'
import Common, { useUtil } from '@/utils/common'
import DatasetService from '@/services/dataset'
import DatasetHistoryLogger from './DatasetHistoryLogger.vue'

defineOptions({ name: 'DatasetHistory' })

const props = withDefaults(defineProps<{ isVisible?: boolean; info?: DatasetModel | null }>(), {
  isVisible: false,
  info: null
})
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const { t } = useI18n()
const filter: FilterModel = new FilterModel()
const { historyHeaders } = useDatasetHeaders()
const { getText } = useUtil()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const loading = ref(false)
const data = ref<any[]>([])
const pageIndex = ref(1)
const pageSize = ref(10)
const dataCount = ref(0)
const loggerVisible = ref(false)
const loggerInfo = ref<any>(null)
const stoppingId = ref<number | null>(null)
const configureVisible = ref(false)
const configureInfo = ref<string | null>(null)

const handleInitialize = () => {
  loading.value = true
  DatasetService.getHistory(props.info?.code as string, filter)
                .then((response: any) => {
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

const getStateText = (origin: string): string => getText(origin)

const toPercent = (value: number | string | null | undefined): number => {
  if (value === null || value === undefined || value === '') return 0
  const v = typeof value === 'string' ? parseFloat(value) : value
  if (isNaN(v) || v < 0) return 0
  return Math.min(v, 100)
}

const onViewLog = (row: any) => {
  loggerInfo.value = row
  loggerVisible.value = true
}

const onLoggerClose = () => {
  loggerVisible.value = false
  loggerInfo.value = null
}

const isStoppable = (row: any): boolean => row?.state === 'RUNNING' || row?.state === 'CREATED'

const hasMessage = (row: any): boolean => !!row?.message && (row?.state === 'FAILURE' || row?.state === 'INTERRUPTED')

const onViewConfigure = (row: any) => {
  configureInfo.value = row?.executorConfigure || ''
  configureVisible.value = true
}

const formatJson = (raw: string | null | undefined): string => {
  if (!raw) return ''
  try {
    return JSON.stringify(JSON.parse(raw), null, 2)
  }
  catch {
    return raw
  }
}

const onStop = (row: any) => {
  if (!row?.id) {
    return
  }
  stoppingId.value = row.id
  DatasetService.stopHistory(row.id)
                .then((response: any) => {
                  if (response.status) {
                    message.success(t('dataset.history.stopRequested'))
                    handleInitialize()
                  }
                  else {
                    message.error(response.message)
                  }
                })
                .finally(() => (stoppingId.value = null))
}

handleInitialize()
</script>
