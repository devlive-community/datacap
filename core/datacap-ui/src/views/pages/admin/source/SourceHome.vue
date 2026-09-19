<template>

    <DataTable :title="$t('source.common.list')"
               :columns="headers"
               :data-source="data"
               :loading="loading"
               :page-index="pageIndex"
               :page-size="pageSize"
               :total="dataCount"
               @refresh="handlerInitialize"
               @page-change="onPageChange"
               @size-change="onSizeChange">
      <template #actions>
        <a-button type="primary" @click="visibleInfo(true, null)">
          <template #icon>
            <PlusOutlined :style="{ fontSize: '16px' }"/>
          </template>
          {{ $t('source.common.create') }}
        </a-button>
      </template>

      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'type'">
          <a-tooltip :title="record.type">
            <a-avatar class="cursor-pointer" size="small" :src="'/static/images/plugin/' + record.type.toLowerCase() + '.svg'" :alt="record.type"/>
          </a-tooltip>
        </template>
        <template v-else-if="column.key === 'public'">
          <a-switch v-model:checked="record.public" disabled size="small"/>
        </template>
        <template v-else-if="column.key === 'version'">
          <a-tooltip v-if="record.version" :title="record.version">
            <a-tag color="blue" class="cursor-pointer">{{ formatVersionText(record.version) }}</a-tag>
          </a-tooltip>
        </template>
        <template v-else-if="column.key === 'available'">
          <a-tooltip v-if="!record.available" :title="record.message">
            <CloseCircleOutlined class="cursor-pointer text-red-500" :style="{ fontSize: '20px' }"/>
          </a-tooltip>
          <PlayCircleOutlined v-else="" class="text-green-500" :style="{ fontSize: '20px' }"/>
        </template>
        <template v-else-if="column.key === 'action'">
          <a-space>
            <a-tooltip :title="$t('source.common.modify').replace('$NAME', record.name)">
              <a-button type="text" shape="circle" size="small" :disabled="loginUserCode !== record.user.code" @click="visibleInfo(true, record)">
                <EditOutlined :style="{ fontSize: '15px' }"/>
              </a-button>
            </a-tooltip>

            <a-dropdown trigger="click" placement="bottomRight">
              <a-button type="text" shape="circle" size="small">
                <SettingOutlined :style="{ fontSize: '15px' }"/>
              </a-button>

              <template #overlay>
                <a-menu>
                  <a-menu-item v-if="(loginUserCode === record.user.code) && record.available && record.isSupportMeta">
                    <router-link :to="`/admin/source/${ record?.code }`" target="_blank">
                      <div class="flex items-center space-x-2">
                        <SettingOutlined :style="{ fontSize: '15px' }"/>
                        <span>{{ $t('source.common.manager') }}</span>
                      </div>
                    </router-link>
                  </a-menu-item>
                  <a-menu-item v-else disabled>
                    <div class="flex items-center space-x-2">
                      <SettingOutlined :style="{ fontSize: '15px' }"/>
                      <span>{{ $t('source.common.manager') }}</span>
                    </div>
                  </a-menu-item>

                  <a-menu-item :disabled="loginUserCode !== record.user.code" @click="visibleDelete(true, record)">
                    <div class="flex items-center space-x-2">
                      <DeleteOutlined :style="{ fontSize: '15px' }"/>
                      <span>{{ $t('common.deleteData') }}</span>
                    </div>
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </a-space>
        </template>
      </template>
    </DataTable>

  <SourceInfo v-if="dataInfoVisible"
              :is-visible="dataInfoVisible"
              :info="dataInfo"
              @close="visibleInfo(false, null)"/>

  <SourceDelete v-if="dataDeleteVisible"
                :is-visible="dataDeleteVisible"
                :info="dataInfo"
                @close="visibleDelete(false, null)"/>

  <SourceMetadata v-if="dataSyncMetadataVisible"
                  :is-visible="dataSyncMetadataVisible"
                  :info="dataInfo"
                  @close="visibleSyncMetadata(false, null)"/>

  <SourceHistory v-if="dataHistoryVisible"
                 :is-visible="dataHistoryVisible"
                 :info="dataInfo"
                 @close="visibleHistory(false, null)"/>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import Common from '@/utils/common'
import { useHeaders } from '@/views/pages/admin/source/SourceUtils'
import { FilterModel } from '@/model/filter'
import { SourceModel } from '@/model/source'
import DataTable from '@/views/components/table/DataTable.vue'
import SourceService from '@/services/source'
import SourceInfo from '@/views/pages/admin/source/SourceInfo.vue'
import SourceHistory from '@/views/pages/admin/source/SourceHistory.vue'
import SourceDelete from '@/views/pages/admin/source/SourceDelete.vue'
import SourceMetadata from '@/views/pages/admin/source/SourceMetadata.vue'
import { CloseCircleOutlined, DeleteOutlined, EditOutlined, PlayCircleOutlined, PlusOutlined, SettingOutlined } from '@ant-design/icons-vue'

defineOptions({ name: 'SourceHome' })

const filter: FilterModel = new FilterModel()
const { headers } = useHeaders()
const loginUserCode = Common.getCurrentUserCode()

const loading = ref(false)
const data = ref<any[]>([])
const pageIndex = ref(1)
const pageSize = ref(10)
const dataCount = ref(0)
const dataInfoVisible = ref(false)
const dataInfo = ref<SourceModel | null>(null)
const dataDeleteVisible = ref(false)
const dataSyncMetadataVisible = ref(false)
const dataHistoryVisible = ref(false)

const handlerInitialize = () => {
  loading.value = true
  SourceService.getAll(filter)
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

const visibleInfo = (opened: boolean, value: SourceModel | null) => {
  dataInfoVisible.value = opened
  dataInfo.value = value
  if (!opened) {
    handlerInitialize()
  }
}

const visibleDelete = (opened: boolean, value: SourceModel | null) => {
  dataDeleteVisible.value = opened
  dataInfo.value = value
  if (!opened) {
    handlerInitialize()
  }
}

const visibleSyncMetadata = (opened: boolean, value: SourceModel | null) => {
  dataSyncMetadataVisible.value = opened
  dataInfo.value = value
}

const visibleHistory = (opened: boolean, value: SourceModel | null) => {
  dataHistoryVisible.value = opened
  dataInfo.value = value
}

const formatVersionText = (version: string, maxLength = 20) => {
  if (!version) {
    return '-'
  }
  if (version.length <= maxLength) {
    return version
  }
  const frontLength = Math.ceil((maxLength - 3) / 2)
  const backLength = Math.floor((maxLength - 3) / 2)
  return `${ version.substring(0, frontLength) }...${ version.substring(version.length - backLength) }`
}

onMounted(() => handlerInitialize())
</script>
