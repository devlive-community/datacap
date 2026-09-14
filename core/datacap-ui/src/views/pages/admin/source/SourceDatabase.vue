<template>
  <div class="relative min-h-screen">
    <a-spin :spinning="loading">
      <div v-if="dataInfo">
        <a-row class="mt-4" :gutter="20">
          <a-col :span="8">
            <div class="flex items-center space-x-2">
              <ShadcnIcon icon="Database"/>
              <span>{{ dataInfo.object_name || dataInfo.schema_name || dataInfo.SCHEMA_NAME }}</span>
            </div>
          </a-col>

          <a-col :span="8">
            <a-tooltip :title="$t('common.createTime')">
              <div class="flex items-center space-x-2">
                <ShadcnIcon icon="Clock"/>
                <span>{{ dataInfo.object_create_time === null ? $t('source.common.notSpecified') : dataInfo.object_create_time }}</span>
              </div>
            </a-tooltip>
          </a-col>

          <a-col :span="8">
            <a-tooltip :title="$t('common.updateTime')">
              <div class="flex items-center space-x-2">
                <ShadcnIcon icon="Clock"/>
                <span>{{ dataInfo.object_update_time === null ? $t('source.common.notUpdated') : dataInfo.object_update_time }}</span>
              </div>
            </a-tooltip>
          </a-col>

          <a-col :span="8">
            <div class="flex items-center space-x-2">
              <span>{{ $t('common.charset') }}</span>
              <span class="text-gray-500">{{ dataInfo.object_charset }}</span>
            </div>
          </a-col>

          <a-col :span="8">
            <div class="flex items-center space-x-2">
              <span>{{ $t('common.collation') }}</span>
              <span class="text-gray-500">{{ dataInfo.object_collation }}</span>
            </div>
          </a-col>

          <a-col :span="8">
            <div class="flex items-center space-x-2">
              <span>{{ $t('common.dataSize') }}</span>
              <span class="text-gray-500">{{ dataInfo.object_data_size }} MB</span>
            </div>
          </a-col>

          <a-col :span="8">
            <div class="flex items-center space-x-2">
              <span>{{ $t('common.indexSize') }}</span>
              <span class="text-gray-500">{{ dataInfo.object_index_size }} MB</span>
            </div>
          </a-col>

          <a-col :span="8">
            <div class="flex items-center space-x-2">
              <span>{{ $t('common.totalSize') }}</span>
              <span class="text-gray-500">{{ dataInfo.object_total_size }}</span>
            </div>
          </a-col>

          <a-col :span="8">
            <div class="flex items-center space-x-2">
              <span>{{ $t('common.tableCount') }}</span>
              <span class="text-gray-500">{{ dataInfo.object_table_count }}</span>
            </div>
          </a-col>

          <a-col :span="8">
            <div class="flex items-center space-x-2">
              <span>{{ $t('common.columnCount') }}</span>
              <span class="text-gray-500">{{ dataInfo.object_column_count }}</span>
            </div>
          </a-col>

          <a-col :span="8">
            <div class="flex items-center space-x-2">
              <span>{{ $t('common.indexCount') }}</span>
              <span class="text-gray-500">{{ dataInfo.object_index_count }}</span>
            </div>
          </a-col>

          <a-col :span="8">
            <div class="flex items-center space-x-2">
              <span>{{ $t('common.viewCount') }}</span>
              <span class="text-gray-500">{{ dataInfo.object_view_count }}</span>
            </div>
          </a-col>

          <a-col :span="8">
            <div class="flex items-center space-x-2">
              <span>{{ $t('common.procedureCount') }}</span>
              <span class="text-gray-500">{{ dataInfo.object_procedure_count }}</span>
            </div>
          </a-col>

          <a-col :span="8">
            <div class="flex items-center space-x-2">
              <span>{{ $t('common.triggerCount') }}</span>
              <span class="text-gray-500">{{ dataInfo.object_trigger_count }}</span>
            </div>
          </a-col>

          <a-col :span="8">
            <div class="flex items-center space-x-2">
              <span>{{ $t('common.foreignKeyCount') }}</span>
              <span class="text-gray-500">{{ dataInfo.object_foreign_key_count }}</span>
            </div>
          </a-col>

          <a-col :span="8">
            <div class="flex items-center space-x-2">
              <span>{{ $t('common.totalCount') }}</span>
              <span class="text-gray-500">{{ dataInfo.object_total_rows }}</span>
            </div>
          </a-col>
        </a-row>
      </div>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import MetadataService from '@/services/metadata.ts'

defineOptions({ name: 'SourceDatabase' })

const route = useRoute()

const loading = ref(false)
const dataInfo = ref<any | null>(null)

const handleInitialize = () => {
  const code = route?.params.source as string
  const database = route?.params.database as string
  if (code && database) {
    loading.value = true
    MetadataService.getDatabase(code, database)
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

watch(() => route?.params.database, () => handleInitialize())

handleInitialize()
</script>
