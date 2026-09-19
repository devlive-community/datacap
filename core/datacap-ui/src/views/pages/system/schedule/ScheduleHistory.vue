<template>
  <a-modal v-model:open="visible"
           width="60%"
           :title="$t('schedule.common.history')"
           :footer="null"
           @cancel="onCancel">
    <a-spin :spinning="loading">
      <a-table size="small"
               :columns="headers"
               :data-source="data"
               :pagination="false"
               row-key="id"
               :scroll="{ x: 'max-content' }"/>

      <div v-if="data?.length > 0" class="py-2 flex justify-end">
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
import { ScheduleModel } from '@/model/schedule'
import { FilterModel } from '@/model/filter'
import { useHeaders } from '@/views/pages/system/schedule/ScheduleUtils'
import ScheduleService from '@/services/schedule'

defineOptions({ name: 'ScheduleHistory' })

const props = withDefaults(defineProps<{ isVisible?: boolean; info?: ScheduleModel | null }>(), {
  isVisible: false,
  info: null
})
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const filter: FilterModel = new FilterModel()
const { historyHeaders: headers } = useHeaders()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const loading = ref(false)
const data = ref<any[]>([])
const pageIndex = ref(1)
const pageSize = ref(10)
const dataCount = ref(0)

const handlerInitialize = () => {
  loading.value = true
  ScheduleService.getScheduleHistory(filter, props.info?.id as number)
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
  handlerInitialize()
}

const onPageChange = (value: number) => fetchData(value)

const onSizeChange = (_current: number, size: number) => {
  pageSize.value = size
  fetchData(pageIndex.value)
}

const onCancel = () => {
  visible.value = false
}

handlerInitialize()
</script>
