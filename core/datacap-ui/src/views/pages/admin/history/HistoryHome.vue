<template>
  <a-card>
    <template #title>
      <div class="ml-2 font-normal text-sm">{{ $t('source.common.list') }}</div>
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
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'source'">
          <span>{{ record?.source?.name }}</span>
        </template>
        <template v-else-if="column.key === 'type'">
          <a-tooltip :title="record?.source?.type">
            <a-avatar size="small"
                      :src="'/static/images/plugin/' + record?.source?.type.toLowerCase() + '.svg'"
                      :alt="record?.source?.type"
                      class="cursor-pointer"/>
          </a-tooltip>
        </template>
        <template v-else-if="column.key === 'mode'">
          <a-tag v-if="record.mode === 'ADHOC'">{{ $t('common.adhoc') }}</a-tag>
          <a-tag v-else-if="record.mode === 'HISTORY'">{{ $t('common.history') }}</a-tag>
          <a-tag v-else-if="record.mode === 'REPORT'">{{ $t('common.report') }}</a-tag>
          <a-tag v-else-if="record.mode === 'SNIPPET'">{{ $t('common.snippet') }}</a-tag>
          <a-tag v-else-if="record.mode === 'DATASET'">{{ $t('common.dataset') }}</a-tag>
          <a-tag v-else>{{ record.mode }}</a-tag>
        </template>
        <template v-else-if="column.key === 'state'">
          <a-tag :color="record.state === 'SUCCESS' ? 'success' : 'error'">{{ record.state }}</a-tag>
        </template>
        <template v-else-if="column.key === 'format'">
          <a-tooltip :title="record?.format">
            <a-avatar size="small"
                      :src="'/static/images/convert/' + record?.format.replace('Convert', '').toLowerCase() + '.svg'"
                      :alt="record?.source?.type"
                      class="cursor-pointer"/>
          </a-tooltip>
        </template>
        <template v-else-if="column.key === 'action'">
          <a-space>
            <a-tooltip :title="$t('common.error')">
              <a-button danger
                        shape="circle"
                        size="small"
                        :disabled="record.state === 'SUCCESS'"
                        @click="handlerShowContent(true, record?.message)">
                <ShadcnIcon icon="TriangleAlert" size="15"/>
              </a-button>
            </a-tooltip>

            <a-dropdown trigger="click" placement="bottomRight">
              <a-button shape="circle" size="small">
                <ShadcnIcon icon="EllipsisVertical" size="15"/>
              </a-button>

              <template #overlay>
                <a-menu>
                  <a-menu-item>
                    <router-link :to="`/admin/query/history/${ record?.code }`" target="_blank">
                      <div class="flex items-center space-x-2">
                        <ShadcnIcon icon="Quote" size="15"/>
                        <span>{{ $t('query.common.quoteRecord') }}</span>
                      </div>
                    </router-link>
                  </a-menu-item>

                  <a-menu-item @click="handlerShowContent(true, record?.content)">
                    <div class="flex items-center space-x-2">
                      <ShadcnIcon icon="SquareChevronRight" size="15"/>
                      <span>{{ $t('query.common.showSql') }}</span>
                    </div>
                  </a-menu-item>

                  <a-menu-item @click="handlerShowData(true, record)">
                    <div class="flex items-center space-x-2">
                      <ShadcnIcon icon="Table" size="15"/>
                      <span>{{ $t('query.common.historyData') }}</span>
                    </div>
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </a-space>
        </template>
      </template>
    </DataTable>
  </a-card>

  <SqlInfo v-if="contentVisible && content"
           :is-visible="contentVisible"
           :content="content"
           @close="handlerShowContent(false, null)"/>

  <HistoryData v-if="dataVisible && dataInfo"
               :is-visible="dataVisible"
               :info="dataInfo"
               @close="handlerShowData(false, null)"/>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { FilterModel } from '@/model/filter.ts'
import AuditService from '@/services/audit'
import { useHeaders } from '@/views/pages/admin/history/HistoryUtils'
import { HistoryModel } from '@/model/history'
import DataTable from '@/views/components/table/DataTable.vue'
import SqlInfo from '@/views/components/sql/SqlInfo.vue'
import HistoryData from '@/views/pages/admin/history/HistoryData.vue'

defineOptions({ name: 'HistoryHome' })

const filter: FilterModel = new FilterModel()
const { headers } = useHeaders()

const loading = ref(false)
const data = ref<any[]>([])
const pageIndex = ref(1)
const pageSize = ref(10)
const dataCount = ref(0)
const contentVisible = ref(false)
const dataInfo = ref<HistoryModel | null>(null)
const dataVisible = ref(false)
const content = ref<string | null>(null)

const handlerInitialize = () => {
  loading.value = true
  AuditService.getAll(filter)
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

const handlerShowContent = (opened: boolean, value: string | null) => {
  contentVisible.value = opened
  content.value = value
}

const handlerShowData = (opened: boolean, value: HistoryModel | null) => {
  dataVisible.value = opened
  dataInfo.value = value
}

onMounted(() => handlerInitialize())
</script>
