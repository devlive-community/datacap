<template>

    <DataTable :title="$t('report.common.list')"
               :columns="headers"
               :data-source="data"
               :loading="loading"
               :page-index="pageIndex"
               :page-size="pageSize"
               :total="dataCount"
               @refresh="handleInitialize"
               @page-change="onPageChange"
               @size-change="onSizeChange">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'realtime'">
          <a-switch v-model:checked="record.realtime" size="small" :disabled="record.realtime"/>
        </template>
        <template v-else-if="column.key === 'source'">
          <a-tooltip v-if="record.source" :title="record.source?.type">
            <a-avatar size="small" :src="'/static/images/plugin/' + record.source?.type.toLowerCase() + '.svg'" :alt="record.source?.type"/>
          </a-tooltip>
          <a-tooltip v-else :title="record.dataset?.name">
            <a-tag>{{ $t('common.dataset') }}</a-tag>
          </a-tooltip>
        </template>
        <template v-else-if="column.key === 'action'">
          <a-space>
            <a-tooltip :title="$t('report.common.view').replace('$VALUE', record.name)">
              <a-button type="text" shape="circle" size="small" @click="visibleView(true, record)">
                <EyeOutlined :style="{ fontSize: '15px' }"/>
              </a-button>
            </a-tooltip>

            <a-dropdown trigger="click">
              <a-button type="text" shape="circle" size="small">
                <SettingOutlined :style="{ fontSize: '15px' }"/>
              </a-button>

              <template #overlay>
                <a-menu>
                  <a-menu-item :disabled="record.type === 'QUERY'">
                    <router-link v-if="record.type !== 'QUERY'"
                                 :to="`/admin/dataset/adhoc/${ record.dataset?.code }/${ record.code }`"
                                 target="_blank">
                      <div class="flex items-center space-x-2">
                        <EditOutlined :style="{ fontSize: '15px' }"/>
                        <span>{{ $t('report.common.modify') }}</span>
                      </div>
                    </router-link>
                    <div v-else class="flex items-center space-x-2">
                      <EditOutlined :style="{ fontSize: '15px' }"/>
                      <span>{{ $t('report.common.modify') }}</span>
                    </div>
                  </a-menu-item>

                  <a-menu-item @click="visibleDelete(true, record)">
                    <div class="flex items-center space-x-2">
                      <DeleteOutlined :style="{ fontSize: '15px' }"/>
                      <span>{{ $t('report.common.delete') }}</span>
                    </div>
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </a-space>
        </template>
      </template>
    </DataTable>

  <ReportView v-if="dataViewVisible"
              :is-visible="dataViewVisible"
              :info="dataInfo"
              @close="visibleView(false, null)"/>

  <ReportDelete v-if="dataDeleteVisible"
                :is-visible="dataDeleteVisible"
                :info="dataInfo"
                @close="visibleDelete(false, null)"/>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { message } from 'ant-design-vue'
import { FilterModel } from '@/model/filter'
import { useHeaders } from '@/views/pages/admin/report/ReportUtils'
import { ReportModel } from '@/model/report'
import DataTable from '@/views/components/table/DataTable.vue'
import ReportService from '@/services/report'
import ReportView from '@/views/pages/admin/report/ReportView.vue'
import ReportDelete from '@/views/pages/admin/report/ReportDelete.vue'
import { DeleteOutlined, EditOutlined, EyeOutlined, SettingOutlined } from '@ant-design/icons-vue'

defineOptions({ name: 'ReportHome' })

const filter: FilterModel = new FilterModel()
const { headers } = useHeaders()

const loading = ref(false)
const data = ref<any[]>([])
const pageIndex = ref(1)
const pageSize = ref(10)
const dataCount = ref(0)
const dataInfo = ref<ReportModel | null>(null)
const dataViewVisible = ref(false)
const dataDeleteVisible = ref(false)

const handleInitialize = () => {
  loading.value = true
  ReportService.getAll(filter)
               .then(response => {
                 if (response.status) {
                   data.value = response.data.content
                   dataCount.value = response.data.total
                   pageSize.value = response.data.size
                   pageIndex.value = response.data.page
                 }
                 else {
                   message.error(response.message)
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

const visibleView = (opened: boolean, value: ReportModel | null) => {
  dataViewVisible.value = opened
  dataInfo.value = value
}

const visibleDelete = (opened: boolean, value: ReportModel | null) => {
  dataDeleteVisible.value = opened
  dataInfo.value = value
  if (!opened) {
    handleInitialize()
  }
}

onMounted(() => handleInitialize())
</script>
