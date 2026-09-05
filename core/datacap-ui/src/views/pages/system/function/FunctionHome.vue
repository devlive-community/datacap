<template>
  <a-card>
    <template #title>
      <div class="ml-2 font-normal text-sm">{{ $t('function.common.list') }}</div>
    </template>

    <DataTable :columns="headers"
               :data-source="data"
               :loading="loading"
               :page-index="pageIndex"
               :page-size="pageSize"
               :total="dataCount"
               @refresh="handlerInitialize"
               @page-change="onPageChange"
               @size-change="onSizeChange">
      <template #actions>
        <a-button type="primary" @click="handlerInfo(true, null)">
          <template #icon>
            <ShadcnIcon icon="Plus"/>
          </template>
          {{ $t('function.common.create') }}
        </a-button>
        <a-button @click="handlerImport(true)">
          <template #icon>
            <ShadcnIcon icon="Import" size="16"/>
          </template>
          {{ $t('function.common.import') }}
        </a-button>
      </template>

      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'type'">
          <a-tag>{{ $t('function.common.' + record.type.toLowerCase()) }}</a-tag>
        </template>
        <template v-else-if="column.key === 'plugin'">
          <a-avatar-group :max-count="3" size="small">
            <a-avatar v-for="item in extractItem(record?.plugin)" :key="item.name" :src="item.src"/>
          </a-avatar-group>
        </template>
        <template v-else-if="column.key === 'action'">
          <a-tooltip :title="$t('common.editData')">
            <a-button size="small" shape="circle" @click="handlerInfo(true, record)">
              <ShadcnIcon icon="Pencil" size="15"/>
            </a-button>
          </a-tooltip>
        </template>
      </template>
    </DataTable>
  </a-card>

  <FunctionInfo v-if="dataInfoVisible"
                :is-visible="dataInfoVisible"
                :info="dataInfo"
                @close="handlerInfo(false, null)"/>

  <FunctionImport v-if="dataImportVisible" :is-visible="dataImportVisible" @close="handlerImport(false)"/>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { FilterModel } from '@/model/filter'
import { useHeaders } from '@/views/pages/system/function/FunctionUtils'
import { FunctionModel } from '@/model/function'
import DataTable from '@/views/components/table/DataTable.vue'
import FunctionService from '@/services/function'
import FunctionInfo from '@/views/pages/system/function/FunctionInfo.vue'
import FunctionImport from '@/views/pages/system/function/FunctionImport.vue'

defineOptions({ name: 'FunctionHome' })

const filter: FilterModel = new FilterModel()
const { headers } = useHeaders()

const loading = ref(false)
const dataInfoVisible = ref(false)
const dataImportVisible = ref(false)
const data = ref<any[]>([])
const pageIndex = ref(1)
const pageSize = ref(10)
const dataCount = ref(0)
const dataInfo = ref<FunctionModel | null>(null)

const handlerInitialize = () => {
  loading.value = true
  FunctionService.getAll(filter)
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

const extractItem = (plugins: string[]) => {
  return (plugins || []).map((item: string) => ({
    name: item,
    src: `/static/images/plugin/${ item.toLowerCase() }.svg`
  }))
}

const handlerInfo = (opened: boolean, value: FunctionModel | null) => {
  dataInfoVisible.value = opened
  dataInfo.value = value
  if (!opened) {
    handlerInitialize()
  }
}

const handlerImport = (value: boolean) => {
  dataImportVisible.value = value
  handlerInitialize()
}

onMounted(() => handlerInitialize())
</script>
