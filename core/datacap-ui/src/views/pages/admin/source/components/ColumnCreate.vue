<template>
  <a-modal v-model:open="visible" width="40%" :title="$t('source.common.newColumn')" :footer="null" @cancel="onCancel">
    <a-form v-if="formState" :model="formState" layout="vertical" @finish="onSubmit">
      <a-row :gutter="16">
        <a-col :span="24" class="mb-2">
          <a-button shape="circle" size="small" @click="onAdd">
            <template #icon>
              <ShadcnIcon icon="Plus" :size="15"/>
            </template>
          </a-button>
        </a-col>

        <a-col v-for="(item, index) in formState.columns" :key="index" :span="24">
          <a-divider orientation="left">{{ item.name }}</a-divider>

          <a-button shape="circle" size="small" danger :disabled="item.removed" class="mb-2" @click="onRemove(index)">
            <template #icon>
              <ShadcnIcon icon="Minus" :size="15"/>
            </template>
          </a-button>

          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item :name="['columns', index, 'name']"
                           :label="`${$t('source.common.columnName')} ${index + 1}`"
                           :rules="[
                               { required: true, message: $t('source.validator.columnName.required') },
                               { pattern: /^[A-Za-z][A-Za-z0-9_-]*$/, message: $t('source.validator.columnName.pattern') }
                           ]">
                <a-input v-model:value="formState.columns[index].name" :placeholder="$t('source.placeholder.columnName')"/>
              </a-form-item>
            </a-col>

            <a-col :span="12">
              <a-form-item :name="['columns', index, 'type']"
                           :label="$t('source.common.columnType')"
                           :rules="[{ required: true, message: $t('source.validator.columnType.required') }]">
                <a-select v-model:value="formState.columns[index].type"
                          :placeholder="$t('source.placeholder.columnType')"
                          :loading="loading">
                  <a-select-option v-for="dataType in dataTypes" :key="dataType" :value="dataType">
                    {{ dataType }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :span="12">
              <a-form-item :name="['columns', index, 'length']"
                           :label="$t('source.common.columnLength')"
                           :rules="[
                               { required: true, message: $t('source.validator.columnLength.required') },
                               { type: 'number', min: 1, message: $t('source.validator.columnLength.min') }
                           ]">
                <a-input-number v-model:value="formState.columns[index].length"
                                :placeholder="$t('source.placeholder.columnLength')"
                                :style="{ width: '100%' }"
                                :min="1"/>
              </a-form-item>
            </a-col>

            <a-col :span="12">
              <a-form-item :name="['columns', index, 'defaultValue']" :label="$t('source.common.columnDefaultValue')">
                <a-input v-model:value="formState.columns[index].defaultValue" :placeholder="$t('source.placeholder.columnDefaultValue')"/>
              </a-form-item>
            </a-col>

            <a-col :span="8">
              <a-form-item :name="['columns', index, 'primaryKey']" :label="$t('source.common.columnPrimaryKey')">
                <a-switch v-model:checked="formState.columns[index].primaryKey"/>
              </a-form-item>
            </a-col>

            <a-col :span="8">
              <a-form-item :name="['columns', index, 'autoIncrement']" :label="$t('source.common.columnAutoIncrement')">
                <a-switch v-model:checked="formState.columns[index].autoIncrement"/>
              </a-form-item>
            </a-col>

            <a-col :span="8">
              <a-form-item :name="['columns', index, 'isNullable']" :label="$t('source.common.columnIsNullable')">
                <a-switch v-model:checked="formState.columns[index].isNullable"/>
              </a-form-item>
            </a-col>

            <a-col :span="24">
              <a-form-item :name="['columns', index, 'comment']" :label="$t('source.common.columnComment')">
                <a-textarea v-model:value="formState.columns[index].comment" :placeholder="$t('source.placeholder.columnComment')"/>
              </a-form-item>
            </a-col>
          </a-row>
        </a-col>
      </a-row>

      <a-space>
        <a-button @click="onCancel">{{ $t('common.cancel') }}</a-button>

        <a-button type="primary" html-type="submit" :loading="saving" :disabled="saving">
          {{ $t('common.save') }}
        </a-button>
      </a-space>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import MetadataService from '@/services/metadata'

defineOptions({ name: 'ColumnCreate' })

const props = withDefaults(defineProps<{ isVisible?: boolean }>(), { isVisible: false })
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const route = useRoute()
const { t } = useI18n()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const loading = ref(false)
const saving = ref(false)
const dataTypes = ref<any[]>([])
const formState = ref<any>({ columns: [] })

const onAdd = () => {
  const newColumn = { removed: false }
  if (formState.value.columns.length === 0) {
    newColumn.removed = true
  }
  formState.value.columns.push(newColumn)
}

const onRemove = (index: number) => {
  if (formState.value.columns) {
    formState.value.columns.splice(index, 1)
  }
}

const onCancel = () => {
  visible.value = false
}

const onSubmit = () => {
  const code = route.params.source as string
  const database = route.params.database as string
  const table = route.params.table as string

  if (code && database && table) {
    saving.value = true
    MetadataService.createColumn(code, database, table, formState.value)
                   .then((response) => {
                     if (response.status && response.data && response.data.isSuccessful) {
                       const columns = String(formState.value?.columns?.map((item: any) => item.name).join(', '))
                       message.success(t('source.tip.createColumnSuccess').replace('$VALUE', columns))
                       onCancel()
                     }
                     else {
                       message.error(response.data.message)
                     }
                   })
                   .finally(() => (saving.value = false))
  }
}

const code = route.params.source as string
loading.value = true
MetadataService.getDataTypes(code)
               .then((response) => {
                 if (response.status && response.data && response.data.isSuccessful) {
                   dataTypes.value = response.data.columns
                 }
               })
               .finally(() => (loading.value = false))

onAdd()
</script>
