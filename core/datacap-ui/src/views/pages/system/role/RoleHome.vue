<template>
  <a-card>
    <template #title>
      <div class="ml-2 font-normal text-sm">{{ $t('role.common.list') }}</div>
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
        <a-button type="primary" @click="handlerChangeInfo(true, null)">
          <template #icon>
            <PlusOutlined :style="{ fontSize: '16px' }"/>
          </template>
          {{ $t('role.common.create') }}
        </a-button>
      </template>

      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <a-space>
            <a-tooltip :title="$t('common.editData')">
              <a-button type="text" size="small" shape="circle" @click="handlerChangeInfo(true, record)">
                <EditOutlined :style="{ fontSize: '15px' }"/>
              </a-button>
            </a-tooltip>

            <a-tooltip :title="$t('role.common.assignMenu').replace('$NAME', record?.name)">
              <a-button type="text" size="small" shape="circle" @click="handlerAssignMenu(true, record)">
                <MenuOutlined :style="{ fontSize: '15px' }"/>
              </a-button>
            </a-tooltip>
          </a-space>
        </template>
      </template>
    </DataTable>
  </a-card>

  <RoleInfo v-if="dataInfoVisible"
            :is-visible="dataInfoVisible"
            :info="dataInfo"
            @close="handlerChangeInfo(false, null)"/>
  <RoleMenu v-if="dataAllocationVisible"
            :is-visible="dataAllocationVisible"
            :info="dataInfo"
            @close="handlerAssignMenu(false, null)"/>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { FilterModel } from '@/model/filter'
import { useHeaders } from '@/views/pages/system/role/RoleUtils'
import { RoleModel } from '@/model/role'
import DataTable from '@/views/components/table/DataTable.vue'
import RoleInfo from '@/views/pages/system/role/RoleInfo.vue'
import RoleMenu from '@/views/pages/system/role/RoleMenu.vue'
import RoleService from '@/services/role'
import { EditOutlined, MenuOutlined, PlusOutlined } from '@ant-design/icons-vue'

defineOptions({ name: 'RoleHome' })

const filter: FilterModel = new FilterModel()
const { headers } = useHeaders()

const loading = ref(false)
const dataInfoVisible = ref(false)
const dataAllocationVisible = ref(false)
const data = ref<any[]>([])
const pageIndex = ref(1)
const pageSize = ref(10)
const dataCount = ref(0)
const dataInfo = ref<RoleModel | null>(null)

const handlerInitialize = () => {
  loading.value = true
  RoleService.getAll(filter)
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

const handlerChangeInfo = (isOpen: boolean, info: RoleModel | null) => {
  dataInfoVisible.value = isOpen
  dataInfo.value = info
  if (!isOpen) {
    handlerInitialize()
  }
}

const handlerAssignMenu = (opened: boolean, info: RoleModel | null) => {
  dataAllocationVisible.value = opened
  dataInfo.value = info
}

onMounted(() => handlerInitialize())
</script>
