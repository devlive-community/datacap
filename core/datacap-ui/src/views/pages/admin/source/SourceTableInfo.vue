<template>
  <div class="relative min-h-screen">
    <a-skeleton v-if="loading" active/>

    <div v-else-if="dataInfo">
      <a-row class="mt-4" :gutter="20">
        <a-col :span="8">
          <div class="flex items-center space-x-2">
            <FontSizeOutlined :style="{ fontSize: '16px' }"/>
            <span>{{ $t('common.'+ dataInfo.type_name) }}</span>
          </div>
        </a-col>

        <a-col :span="8">
          <div class="flex items-center space-x-2">
            <TableOutlined :style="{ fontSize: '16px' }"/>
            <span>{{ dataInfo.object_name }}</span>
          </div>
        </a-col>

        <a-col :span="8">
          <a-tooltip :title="$t('common.createTime')">
            <div class="flex items-center space-x-2">
              <ClockCircleOutlined :style="{ fontSize: '16px' }"/>
              <span>{{ dataInfo?.object_create_time === null ? $t('source.common.notSpecified') : dataInfo.object_create_time }}</span>
            </div>
          </a-tooltip>
        </a-col>

        <a-col :span="8">
          <a-tooltip :title="$t('common.updateTime')">
            <div class="flex items-center space-x-2">
              <ClockCircleOutlined :style="{ fontSize: '16px' }"/>
              <span>{{ dataInfo?.object_update_time === null ? $t('source.common.notUpdated') : dataInfo.object_update_time }}</span>
            </div>
          </a-tooltip>
        </a-col>

        <a-col :span="8">
          <a-tooltip :title="$t('source.common.engine')">
            <div class="flex items-center space-x-2">
              <CalendarOutlined :style="{ fontSize: '16px' }"/>
              <span>{{ dataInfo.object_engine === null ? $t('source.common.notSpecifiedEngine') : dataInfo.object_engine }}</span>
            </div>
          </a-tooltip>
        </a-col>

        <a-col :span="8">
          <a-tooltip :title="$t('source.common.collation')">
            <div class="flex items-center space-x-2">
              <ColumnHeightOutlined :style="{ fontSize: '16px' }"/>
              <span>{{ dataInfo.object_collation === null ? $t('source.common.notSpecifiedCollation') : dataInfo.object_collation }}</span>
            </div>
          </a-tooltip>
        </a-col>

        <a-col :span="8">
          <a-tooltip :title="$t('source.common.totalRows')">
            <div class="flex items-center space-x-2">
              <SplitCellsOutlined :style="{ fontSize: '16px' }"/>
              <span>{{ dataInfo.object_rows }}</span>
            </div>
          </a-tooltip>
        </a-col>

        <a-col :span="8">
          <a-tooltip :title="$t('source.common.format')">
            <div class="flex items-center space-x-2">
              <ClearOutlined :style="{ fontSize: '16px' }"/>
              <span>{{ dataInfo.object_format === null ? $t('source.common.notSpecifiedFormat') : dataInfo.object_format }}</span>
            </div>
          </a-tooltip>
        </a-col>

        <a-col :span="8">
          <a-tooltip :title="$t('source.common.avgRowLength')">
            <div class="flex items-center space-x-2">
              <SortAscendingOutlined :style="{ fontSize: '16px' }"/>
              <span>{{ dataInfo.object_avg_row_length === null ? 0 : dataInfo.object_avg_row_length }}</span>
            </div>
          </a-tooltip>
        </a-col>

        <a-col :span="8">
          <a-tooltip :title="$t('source.common.dataSize')">
            <div class="flex items-center space-x-2">
              <ColumnHeightOutlined :style="{ fontSize: '16px' }"/>
              <span>{{ dataInfo.object_data_size === null ? 0 : dataInfo.object_data_size }} MB</span>
            </div>
          </a-tooltip>
        </a-col>

        <a-col :span="8">
          <a-tooltip :title="$t('source.common.indexSize')">
            <div class="flex items-center space-x-2">
              <SearchOutlined :style="{ fontSize: '16px' }"/>
              <span>{{ dataInfo.object_index_size === null ? $t('source.common.notSpecifiedIndex') : dataInfo.object_index_size }}</span>
            </div>
          </a-tooltip>
        </a-col>

        <a-col :span="8">
          <a-tooltip :title="$t('common.columnCount')">
            <div class="flex items-center space-x-2">
              <InsertRowRightOutlined :style="{ fontSize: '16px' }"/>
              <span>{{ dataInfo.object_column_count === null ? $t('source.common.notSpecifiedIndex') : dataInfo.object_column_count }}</span>
            </div>
          </a-tooltip>
        </a-col>

        <a-col :span="8">
          <a-tooltip :title="$t('common.indexCount')">
            <div class="flex items-center space-x-2">
              <SearchOutlined :style="{ fontSize: '16px' }"/>
              <span>{{ dataInfo.object_index_count === null ? $t('source.common.notSpecifiedIndex') : dataInfo.object_index_count }}</span>
            </div>
          </a-tooltip>
        </a-col>

        <a-col :span="8">
          <div class="flex items-center space-x-4 justify-between">
            <a-tooltip :title="$t('source.common.autoIncrement')">
              <div class="flex items-center space-x-2">
                <ColumnHeightOutlined :style="{ fontSize: '16px' }"/>
                <span>{{ dataInfo.object_auto_increment === null ? $t('source.common.notSpecifiedPrimaryKey') : dataInfo.object_auto_increment }}</span>
              </div>
            </a-tooltip>
            <div>
              <a-tooltip :title="$t('source.common.resetAutoIncrement')">
                <a-button type="text" shape="circle" size="small" :disabled="dataInfo.object_auto_increment === null" @click="autoIncrement = true">
                  <template #icon>
                    <SettingOutlined :style="{ fontSize: '16px' }"/>
                  </template>
                </a-button>
              </a-tooltip>
            </div>
          </div>
        </a-col>

        <a-col :span="24">
          <a-divider class="mt-4 mb-4"/>

          <div class="mb-2">{{ $t('source.common.comment') }}</div>

          <a-textarea v-model:value="dataInfo.object_comment"/>
        </a-col>
      </a-row>
    </div>
  </div>

  <TableAutoIncrement v-if="autoIncrement"
                      :visible="autoIncrement"
                      :info="dataInfo"
                      @close="autoIncrement = false"/>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import MetadataService from '@/services/metadata.ts'
import TableAutoIncrement from '@/views/pages/admin/source/components/TableAutoIncrement.vue'
import { CalendarOutlined, ClearOutlined, ClockCircleOutlined, ColumnHeightOutlined, FontSizeOutlined, InsertRowRightOutlined, SearchOutlined, SettingOutlined, SortAscendingOutlined, SplitCellsOutlined, TableOutlined } from '@ant-design/icons-vue'

defineOptions({ name: 'SourceTableInfo' })

const route = useRoute()

const loading = ref(false)
const autoIncrement = ref(false)
const dataInfo = ref<any | null>(null)

const handleInitialize = () => {
  const code = route?.params.source as string
  const database = route?.params.database as string
  const table = route?.params.table as string
  if (code && database && table) {
    loading.value = true
    MetadataService.getTable(code, database, table)
                   .then((response) => {
                     if (response.status && response.data && response.data.isSuccessful) {
                       dataInfo.value = response.data.columns[0] || null
                     }
                     else {
                       message.error(response.message)
                     }
                   })
                   .finally(() => (loading.value = false))
  }
}

watch(() => route?.params.table, () => handleInitialize())

handleInitialize()
</script>
