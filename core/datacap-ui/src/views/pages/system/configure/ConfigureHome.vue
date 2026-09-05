<template>
  <a-card>
    <template #title>
      <div class="ml-2 font-normal text-sm">{{ $t('configure.runtime.title') }}</div>
    </template>

    <div class="grid grid-cols-12 gap-3">
      <!-- left: category + rows -->
      <div class="col-span-3 border-r pr-2 space-y-2">
        <div v-for="cat in categories" :key="cat.value">
          <div class="text-xs font-medium text-gray-500 mb-1">{{ cat.label }}</div>
          <div v-for="row in (groupedRows[cat.value] || [])" :key="row.id"
               class="px-2 py-1 rounded text-xs cursor-pointer"
               :class="selected && selected.name === row.name && selected.category === cat.value
                       ? 'bg-blue-100 dark:bg-blue-900'
                       : 'hover:bg-gray-100 dark:hover:bg-gray-800'"
               @click="onSelect(cat.value, row.name)">
            {{ row.name }}
          </div>
          <div v-if="!(groupedRows[cat.value] || []).length" class="text-xs text-gray-400 italic ml-1">
            {{ $t('configure.runtime.empty') }}
          </div>
        </div>
      </div>

      <!-- right: detail form -->
      <div class="col-span-9 pl-2">
        <a-spin :spinning="loading">
          <div v-if="!selected" class="text-xs text-gray-500 italic">
            {{ $t('configure.runtime.selectHint') }}
          </div>

          <div v-else class="space-y-3">
            <div class="text-sm font-medium">{{ selected.category }} / {{ selected.name }}</div>

            <div v-for="field in schema" :key="field.name" class="space-y-1">
              <label class="text-xs">
                <span>{{ field.name }}</span>
                <span v-if="!field.tunable" class="ml-1 text-xs text-orange-500">[{{ $t('configure.runtime.adminOnly') }}]</span>
              </label>
              <div v-if="field.description" class="text-xs text-gray-500">{{ field.description }}</div>

              <a-input v-if="field.type === 'STRING'" v-model:value="form[field.name]"/>
              <a-input-number v-else-if="field.type === 'NUMBER'" v-model:value="form[field.name]" :style="{ width: '100%' }"/>
              <a-switch v-else-if="field.type === 'BOOLEAN'"
                        v-model:checked="booleanProxies[field.name]"
                        @change="(v: boolean) => onBoolChange(field.name, v)"/>
              <a-input-password v-else-if="field.type === 'PASSWORD'" v-model:value="form[field.name]"/>
            </div>

            <div class="pt-2">
              <a-button type="primary" :loading="saving" @click="onSave">
                {{ $t('common.save') }}
              </a-button>
            </div>
          </div>
        </a-spin>
      </div>
    </div>
  </a-card>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import RuntimeConfigureService from '@/services/runtimeConfigure'

interface PluginConfigureField
{
  name: string
  type: 'STRING' | 'NUMBER' | 'BOOLEAN' | 'PASSWORD'
  defaultValue: string
  description: string
  tunable: boolean
}

interface ConfigureRow
{
  id: number
  name: string
  category: string
}

defineOptions({ name: 'ConfigureHome' })

const { t } = useI18n()

const CATEGORY_KEYS = ['EXECUTOR', 'DATASET']

const categories = computed(() => [
  { value: 'EXECUTOR', label: t('configure.runtime.categoryExecutor') as string },
  { value: 'DATASET', label: t('configure.runtime.categoryDataset') as string }
])

const loading = ref(false)
const saving = ref(false)
const groupedRows = ref<Record<string, ConfigureRow[]>>({})
const selected = ref<{ category: string; name: string } | null>(null)
const schema = ref<PluginConfigureField[]>([])
const form = ref<Record<string, string>>({})
const booleanProxies = ref<Record<string, boolean>>({})

const loadAll = async () => {
  loading.value = true
  try {
    for (const c of CATEGORY_KEYS) {
      const response = await RuntimeConfigureService.list(c)
      groupedRows.value[c] = response.status ? (response.data || []) : []
    }
  }
  finally {
    loading.value = false
  }
}

const onSelect = (category: string, name: string) => {
  selected.value = { category, name }
  loading.value = true
  RuntimeConfigureService.detail(category, name)
                         .then(response => {
                           if (response.status && response.data) {
                             schema.value = response.data.schema || []
                             form.value = {}
                             booleanProxies.value = {}
                             const values = response.data.values || {}
                             for (const f of schema.value) {
                               form.value[f.name] = values[f.name] ?? f.defaultValue ?? ''
                               if (f.type === 'BOOLEAN') {
                                 booleanProxies.value[f.name] = (form.value[f.name] + '').toLowerCase() === 'true'
                               }
                             }
                           }
                           else {
                             message.error(response.message)
                           }
                         })
                         .finally(() => (loading.value = false))
}

const onBoolChange = (name: string, value: boolean) => {
  form.value[name] = value ? 'true' : 'false'
}

const onSave = () => {
  if (!selected.value) {
    return
  }
  // sync booleans
  for (const f of schema.value) {
    if (f.type === 'BOOLEAN') {
      form.value[f.name] = booleanProxies.value[f.name] ? 'true' : 'false'
    }
  }
  saving.value = true
  RuntimeConfigureService.save(selected.value.category, selected.value.name, form.value)
                         .then(response => {
                           if (response.status) {
                             message.success(t('common.successfully'))
                             loadAll()
                           }
                           else {
                             message.error(response.message)
                           }
                         })
                         .finally(() => (saving.value = false))
}

loadAll()
</script>
