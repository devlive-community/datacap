<template>
  <a-modal v-model:open="visible"
           width="60%"
           :title="$t('source.common.syncHistory')"
           :footer="null">
    <a-spin :spinning="loading">
      <a-table size="small"
               :columns="headers"
               :data-source="data"
               :pagination="false"
               row-key="id"
               :scroll="{ x: 'max-content' }">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'elapsed'">
            {{ (getTime(record.updateTime) - getTime(record.createTime)) / 1000 }}
          </template>

          <template v-else-if="column.key === 'state'">
            <a-tooltip v-if="record?.state === 'FAILURE'" :title="record.message">
              <a-badge :color="Common.getColor(record?.state)" :text="getStateText(record.state)"/>
            </a-tooltip>
            <a-badge v-else :color="Common.getColor(record?.state)" :text="getStateText(record.state)"/>
          </template>

          <template v-else-if="column.key === 'result'">
            <a-tooltip>
              <template #title>
                <MdPreview :modelValue="toMarkdown(record.info)" style="padding: 0"/>
              </template>
              <a-button shape="circle" size="small">
                <template #icon>
                  <ShadcnIcon icon="Eye" :size="20"/>
                </template>
              </a-button>
            </a-tooltip>
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
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { SourceModel } from '@/model/source'
import { FilterModel } from '@/model/filter'
import { useHeaders } from '@/views/pages/admin/source/SourceUtils'
import Common, { useUtil } from '@/utils/common'
import SourceService from '@/services/source'
import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

defineOptions({ name: 'SourceHistory' })

const props = withDefaults(defineProps<{ isVisible?: boolean; info?: SourceModel | null }>(), {
  isVisible: false,
  info: null
})
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const filter: FilterModel = new FilterModel()
const { historyHeaders: headers } = useHeaders()
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

const handleInitialize = () => {
  loading.value = true
  SourceService.getHistory(props.info?.code as string, filter)
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

const getTime = (time: any) => (time ? new Date(time).getTime() : 0)

const getStateText = (origin: string): string => getText(origin)

const toMarkdown = (content: string) => '```json\n' + JSON.stringify(content, null, 4) + '\n```'

handleInitialize()
</script>
