<template>
  <div class="dc-workflow-configure flex flex-col h-full bg-white border-l" :style="{ width: `${ width }px` }">
    <template v-if="selectedNode">
      <div class="p-3 border-b">
        <div class="text-sm font-semibold">{{ selectedNode.label }}</div>
        <div class="text-xs text-gray-400 mt-1">{{ selectedNode.description }}</div>
      </div>

      <div class="flex-1 overflow-y-auto p-3">
        <div v-for="item in selectedNode.configure || []"
             :key="item.field"
             class="mb-3">
          <div class="text-xs text-gray-600 mb-1">
            <span v-if="item.required" class="text-red-500">*</span>
            {{ item.label }}
          </div>

          <a-input v-if="item.type === 'input'"
                   :value="item.value"
                   :disabled="item.disabled"
                   :placeholder="item.placeholder"
                   :class="{ 'border-red-500': item.validated && !item.validated.valid }"
                   @change="onChange(item, $event.target.value)"/>

          <a-input-password v-else-if="item.type === 'password'"
                            :value="item.value"
                            :disabled="item.disabled"
                            :placeholder="item.placeholder"
                            :class="{ 'border-red-500': item.validated && !item.validated.valid }"
                            @change="onChange(item, $event.target.value)"/>

          <a-textarea v-else-if="item.type === 'textarea'"
                      :value="item.value"
                      :disabled="item.disabled"
                      :rows="3"
                      :placeholder="item.placeholder"
                      :class="{ 'border-red-500': item.validated && !item.validated.valid }"
                      @change="onChange(item, $event.target.value)"/>

          <a-input-number v-else-if="item.type === 'number'"
                          :value="item.value"
                          :disabled="item.disabled"
                          :placeholder="item.placeholder"
                          class="w-full"
                          :class="{ 'border-red-500': item.validated && !item.validated.valid }"
                          @change="(value) => onChange(item, value)"/>

          <a-switch v-else-if="item.type === 'switch'"
                    :checked="item.value === true || item.value === 'true'"
                    :disabled="item.disabled"
                    @change="(value) => onChange(item, value)"/>

          <a-select v-else-if="item.type === 'select'"
                    :value="item.value"
                    :disabled="item.disabled"
                    :placeholder="item.placeholder"
                    allow-clear
                    class="w-full"
                    @change="(value) => onChange(item, value)">
            <a-select-option v-for="option in item.options || []"
                             :key="option.value"
                             :value="option.value">
              {{ option.label }}
            </a-select-option>
          </a-select>

          <a-select v-else-if="item.type === 'array'"
                    :value="toArrayValue(item.value)"
                    :disabled="item.disabled"
                    mode="tags"
                    :placeholder="item.placeholder"
                    class="w-full"
                    @change="(value) => onChange(item, value)"/>

          <a-textarea v-else-if="item.type === 'map'"
                      :value="toMapText(item.value)"
                      :disabled="item.disabled"
                      :rows="3"
                      placeholder="key=value"
                      :class="{ 'border-red-500': item.validated && !item.validated.valid }"
                      @change="onChange(item, fromMapText($event.target.value))"/>

          <a-input v-else
                   :value="item.value"
                   :disabled="item.disabled"
                   :placeholder="item.placeholder"
                   @change="onChange(item, $event.target.value)"/>

          <div v-if="item.description" class="text-[10px] text-gray-400 mt-1">{{ item.description }}</div>
          <div v-if="item.validated && !item.validated.valid" class="text-xs text-red-500 mt-1">
            {{ item.validated.message }}
          </div>
        </div>
      </div>
    </template>

    <div v-else class="flex-1 flex items-center justify-center text-sm text-gray-400 p-4 text-center">
      {{ $t('workflow.placeholder.selectNode') }}
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import { ConfigureItem, NodeInstance } from './types'

export default defineComponent({
  name: 'WorkflowConfigure',
  props: {
    selectedNode: {
      type: Object as PropType<NodeInstance | null>,
      default: null
    },
    width: {
      type: Number,
      default: 300
    }
  },
  emits: ['update:node'],
  setup(props, { emit })
  {
    const validateItem = (item: ConfigureItem): { valid: boolean; message?: string } => {
      if (!item.required) {
        return { valid: true }
      }
      const empty = item.value === undefined || item.value === null || item.value === ''
          || (Array.isArray(item.value) && item.value.length === 0)
      return empty ? { valid: false, message: item.message || `${ item.label } is required` } : { valid: true }
    }

    const onChange = (item: ConfigureItem, value: any) => {
      item.value = value
      item.validated = validateItem(item)
      if (props.selectedNode) {
        emit('update:node', props.selectedNode)
      }
    }

    const toArrayValue = (value: any): string[] => {
      if (Array.isArray(value)) {
        return value.map((v) => String(v))
      }
      if (value !== undefined && value !== null && value !== '') {
        return [String(value)]
      }
      return []
    }

    const toMapText = (value: any): string => {
      if (!value || typeof value !== 'object') {
        return ''
      }
      return Object.entries(value)
          .map(([key, val]) => `${ key }=${ val }`)
          .join('\n')
    }

    const fromMapText = (text: string): Record<string, string> => {
      const result: Record<string, string> = {}
      text.split('\n')
          .map((line) => line.trim())
          .filter((line) => line.includes('='))
          .forEach((line) => {
            const index = line.indexOf('=')
            result[line.slice(0, index).trim()] = line.slice(index + 1).trim()
          })
      return result
    }

    return { onChange, toArrayValue, toMapText, fromMapText }
  }
})
</script>
