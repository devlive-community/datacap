<template>
  <a-card>
    <template #title>
      <div class="ml-2">{{ $t('workflow.text.list') }}</div>
    </template>

    <DataTable :columns="headers"
               :data-source="data"
               :loading="loading"
               :page-index="pageIndex"
               :page-size="pageSize"
               :total="dataCount"
               @refresh="handleInitialize"
               @page-change="onPageChange"
               @size-change="onSizeChange">
      <template #actions>
        <a-tooltip :title="$t('workflow.text.create')">
          <router-link to="/admin/workflow/info" target="_blank">
            <a-button type="primary" shape="circle" size="small">
              <template #icon>
                <ShadcnIcon icon="Plus" :size="15"/>
              </template>
            </a-button>
          </router-link>
        </a-tooltip>
      </template>

      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'executor'">
          <a-tooltip :title="record.executor">
            <a-avatar size="small" :src="`/static/images/executor/${record.executor.replace('Executor', '').toLowerCase()}.svg`"/>
          </a-tooltip>
        </template>

        <template v-else-if="column.key === 'state'">
          <a-tag class="w-20" :color="Common.getColor(record.state)">
            {{ getText(record.state) }}
          </a-tag>
        </template>

        <template v-else-if="column.key === 'action'">
          <a-space>
            <a-tooltip :title="$t('common.error')">
              <a-button shape="circle"
                        size="small"
                        danger
                        :disabled="record.state !== 'FAILURE' && !(record.state == 'STOPPED' && record.message)"
                        @click="visibleShowMessage(true, record)">
                <template #icon>
                  <ShadcnIcon icon="TriangleAlert" :size="15"/>
                </template>
              </a-button>
            </a-tooltip>

            <a-dropdown trigger="click" placement="bottomRight">
              <a-button shape="circle" size="small">
                <template #icon>
                  <ShadcnIcon icon="Cog" :size="15"/>
                </template>
              </a-button>

              <template #overlay>
                <a-menu>
                  <a-menu-item :disabled="record.state === 'RUNNING'">
                    <template v-if="record.state !== 'RUNNING'">
                      <router-link :to="`/admin/workflow/info/${record.code}`" target="_blank" class="flex items-center">
                        <ShadcnIcon icon="Info" :size="15"/>
                        <span class="ml-2">{{ $t('workflow.text.modify') }}</span>
                      </router-link>
                    </template>
                    <div v-else class="flex items-center">
                      <ShadcnIcon icon="Info" :size="15"/>
                      <span class="ml-2">{{ $t('workflow.text.modify') }}</span>
                    </div>
                  </a-menu-item>

                  <a-menu-item :disabled="record.state !== 'RUNNING'" @click="visibleStop(true, record)">
                    <div class="flex items-center space-x-2">
                      <ShadcnIcon icon="CircleStop" :size="15"/>
                      <span>{{ $t('workflow.text.stop') }}</span>
                    </div>
                  </a-menu-item>

                  <a-menu-item :disabled="record.state === 'RUNNING'" @click="visibleRestart(true, record)">
                    <div class="flex items-center space-x-2">
                      <ShadcnIcon icon="CirclePlay" :size="15"/>
                      <span>{{ $t('workflow.text.restart') }}</span>
                    </div>
                  </a-menu-item>

                  <a-menu-item @click="visibleLogger(true, record)">
                    <div class="flex items-center space-x-2">
                      <ShadcnIcon icon="Rss" :size="15"/>
                      <span>{{ $t('workflow.text.logger') }}</span>
                    </div>
                  </a-menu-item>

                  <a-menu-item :disabled="record.state === 'RUNNING'" @click="visibleDelete(true, record)">
                    <div class="flex items-center space-x-2">
                      <ShadcnIcon icon="Delete" :size="15"/>
                      <span>{{ $t('workflow.text.delete') }}</span>
                    </div>
                  </a-menu-item>

                  <a-menu-item @click="visibleFlow(true, record)">
                    <div class="flex items-center space-x-2">
                      <ShadcnIcon icon="Flower" :size="15"/>
                      <span>{{ $t('workflow.text.flow') }}</span>
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

  <MarkdownPreview v-if="dataMessageVisible && dataInfo"
                   :is-visible="dataMessageVisible"
                   :content="dataInfo.message"
                   @close="visibleShowMessage(false, null)"/>

  <WorkflowFlow v-if="dataFlowVisible && dataInfo"
                :is-visible="dataFlowVisible"
                :code="dataInfo.code"
                @close="visibleFlow(false, null)"/>

  <WorkflowDelete v-if="dataDeleteVisible && dataInfo"
                  :is-visible="dataDeleteVisible"
                  :info="dataInfo"
                  @close="visibleDelete(false, null)"/>

  <WorkflowStop v-if="dataStopVisible && dataInfo"
                :is-visible="dataStopVisible"
                :info="dataInfo"
                @close="visibleStop(false, null)"/>

  <WorkflowLogger v-if="dataLoggerVisible && dataInfo"
                  :is-visible="dataLoggerVisible"
                  :info="dataInfo"
                  @close="visibleLogger(false, null)"/>

  <WorkflowRestart v-if="dataRestartVisible && dataInfo"
                   :is-visible="dataRestartVisible"
                   :info="dataInfo"
                   @close="visibleRestart(false, null)"/>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { FilterModel } from '@/model/filter'
import { useHeaders } from './Utils'
import WorkflowService from '@/services/workflow'
import Common, { useUtil } from '@/utils/common.ts'
import MarkdownPreview from '@/views/components/markdown/MarkdownView.vue'
import { WorkflowModel } from '@/model/workflow.ts'
import DataTable from '@/views/components/table/DataTable.vue'
import WorkflowFlow from '@/views/pages/admin/wofkflow/WorkflowFlow.vue'
import WorkflowDelete from '@/views/pages/admin/wofkflow/WorkflowDelete.vue'
import WorkflowStop from '@/views/pages/admin/wofkflow/WorkflowStop.vue'
import WorkflowLogger from '@/views/pages/admin/wofkflow/WorkflowLogger.vue'
import WorkflowRestart from '@/views/pages/admin/wofkflow/WorkflowRestart.vue'

defineOptions({ name: 'PipelineHome' })

const filter: FilterModel = new FilterModel()
const { headers } = useHeaders()
const { getText } = useUtil()

const loading = ref(false)
const data = ref<any[]>([])
const pageIndex = ref(1)
const pageSize = ref(10)
const dataCount = ref(0)
const dataInfo = ref<WorkflowModel | null>(null)
const dataMessageVisible = ref(false)
const dataLoggerVisible = ref(false)
const dataDeleteVisible = ref(false)
const dataStopVisible = ref(false)
const dataFlowVisible = ref(false)
const dataRestartVisible = ref(false)

const handleInitialize = () => {
  loading.value = true
  WorkflowService.getAll(filter)
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

const visibleStop = (opened: boolean, value: null | WorkflowModel) => {
  dataStopVisible.value = opened
  dataInfo.value = value
  if (!opened) {
    handleInitialize()
  }
}

const visibleShowMessage = (opened: boolean, value: null | WorkflowModel) => {
  dataMessageVisible.value = opened
  dataInfo.value = value
}

const visibleLogger = (opened: boolean, value: null | WorkflowModel) => {
  dataLoggerVisible.value = opened
  dataInfo.value = value
}

const visibleDelete = (opened: boolean, value: null | WorkflowModel) => {
  dataDeleteVisible.value = opened
  dataInfo.value = value
  if (!opened) {
    handleInitialize()
  }
}

const visibleFlow = (opened: boolean, value: null | WorkflowModel) => {
  dataFlowVisible.value = opened
  dataInfo.value = value
}

const visibleRestart = (opened: boolean, value: null | WorkflowModel) => {
  dataRestartVisible.value = opened
  dataInfo.value = value
  if (!opened) {
    handleInitialize()
  }
}

handleInitialize()
</script>
