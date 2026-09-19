<template>
  <a-card>
    <template #title>
      <div class="ml-2 font-normal text-sm">{{ $t('menu.common.list') }}</div>
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
        <a-tooltip :title="$t('menu.common.create')">
          <a-button type="primary" @click="handlerChangeInfo(true, null)">
            <template #icon>
              <PlusOutlined :style="{ fontSize: '16px' }"/>
            </template>
            {{ $t('menu.common.create') }}
          </a-button>
        </a-tooltip>
      </template>

      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'active'">
          <a-switch v-model:checked="record.active" :disabled="record.active"/>
        </template>
        <template v-else-if="column.key === 'action'">
          <a-space>
            <a-tooltip :title="$t('common.editData')">
              <a-button type="text" size="small" shape="circle" @click="handlerChangeInfo(true, record)">
                <EditOutlined :style="{ fontSize: '15px' }"/>
              </a-button>
            </a-tooltip>
          </a-space>
        </template>
      </template>
    </DataTable>
  </a-card>

  <MenuInfo v-if="dataInfoVisible"
            :is-visible="dataInfoVisible"
            :info="dataInfo"
            @close="handlerChangeInfo(false, null)"/>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { FilterModel } from '@/model/filter'
import { useHeaders } from '@/views/pages/system/menu/MenuUtils'
import { MenuModel } from '@/model/menu'
import DataTable from '@/views/components/table/DataTable.vue'
import MenuInfo from '@/views/pages/system/menu/MenuInfo.vue'
import MenuService from '@/services/menu'
import { EditOutlined, PlusOutlined } from '@ant-design/icons-vue'

defineOptions({ name: 'MenuHome' })

const filter: FilterModel = new FilterModel()
const { headers } = useHeaders()

const loading = ref(false)
const dataInfoVisible = ref(false)
const data = ref<any[]>([])
const pageIndex = ref(1)
const pageSize = ref(10)
const dataCount = ref(0)
const dataInfo = ref<MenuModel | null>(null)

const handlerInitialize = () => {
  loading.value = true
  MenuService.getAll(filter)
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

const handlerChangeInfo = (isOpen: boolean, info: MenuModel | null) => {
  dataInfoVisible.value = isOpen
  dataInfo.value = info
  if (!isOpen) {
    handlerInitialize()
  }
}

onMounted(() => handlerInitialize())
</script>
