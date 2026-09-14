<template>
  <a-modal v-model:open="visible"
           width="50%"
           :title="$t('source.common.filterData')"
           :footer="null"
           @cancel="onCancel">
    <a-form :model="formState" layout="vertical">
      <div v-for="(item, index) in formState.filters" :key="index" class="flex items-center space-x-2">
        <a-form-item class="w-full">
          <a-select v-model:value="item.index" @change="onFetchOperations(item.index, item)">
            <a-select-option v-for="column in columns" :key="column" :value="column">
              {{ column }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item class="w-full">
          <a-select v-model:value="item.operator" :disabled="!item.index">
            <a-select-option v-for="operation in item.operations" :key="operation" :value="operation">
              {{ operation }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item class="w-full">
          <a-input v-model:value="item.value" :disabled="!item.operator"/>
        </a-form-item>

        <a-form-item>
          <a-button shape="circle" size="small" danger @click="onRemoveFilter(index)">
            <template #icon>
              <ShadcnIcon icon="Minus"/>
            </template>
          </a-button>
        </a-form-item>
      </div>

      <a-form-item class="space-y-1" :label="$t('source.common.filterCondition')">
        <a-button size="small" @click="onAddFilter">
          {{ $t('source.common.addFilter') }}
        </a-button>
      </a-form-item>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onCancel">
          {{ $t('common.cancel') }}
        </a-button>
        <a-button type="primary" @click="onFilter">
          {{ $t('common.apply') }}
        </a-button>
      </a-space>
    </template>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { cloneDeep } from 'lodash'

defineOptions({ name: 'TableRowFilter' })

const props = withDefaults(defineProps<{
  isVisible?: boolean
  columns?: any[]
  types?: any[]
  configure?: any
}>(), {
  isVisible: false,
  columns: () => [],
  types: () => []
})
const emit = defineEmits<{
  (e: 'close', value: boolean): void
  (e: 'apply', value: any[]): void
}>()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const formState = ref<{ filters: any[] }>({
  filters: props.configure ? cloneDeep(props.configure) : []
})

const onAddFilter = () => {
  formState.value.filters.push({})
}

const onRemoveFilter = (index: number) => {
  formState.value.filters.splice(index, 1)
}

const onFetchOperations = (value: any, filter: any) => {
  filter.name = value
  filter.operations = ['EQ', 'NE', 'GT', 'GE', 'LT', 'LE', 'LIKE', 'IN', 'NOT IN', 'IS NULL', 'IS NOT NULL']
}

const onCancel = () => {
  visible.value = false
}

const onFilter = () => {
  emit('apply', formState.value.filters)
  onCancel()
}
</script>
