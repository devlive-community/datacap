<template>
  <div>
    <a-drawer v-model:open="visible"
              width="25%"
              :title="`[ ${ data?.name } ] ${ $t('common.configure') }`"
              @close="onCancel">
      <a-form :model="formState" layout="vertical">
        <div v-if="data" v-for="item in data.configure" :key="item.field">
          <a-form-item v-if="item.input"
                       :name="item.field"
                       :label="item.field"
                       :extra="item.description">
            <div v-if="item.type === 'INPUT'" class="flex flex-row items-start font-normal space-x-1">
              <a-input v-model:value="item.value" :disabled="item.override && item.tooltip"/>
              <a-tooltip v-if="item.tooltip" :title="item.tooltip">
                <a-checkbox v-model:checked="item.override"/>
              </a-tooltip>
            </div>

            <div v-if="item.type === 'TEXT'" class="flex flex-row items-start font-normal space-x-1">
              <a-textarea v-model:value="item.value" :disabled="item.override && item.tooltip"/>
              <a-tooltip v-if="item.tooltip" :title="item.tooltip">
                <a-checkbox v-model:checked="item.override"/>
              </a-tooltip>
            </div>

            <div v-if="item.type === 'SWITCH'" class="flex flex-row items-start font-normal space-x-1">
              <a-switch v-model:checked="item.value" :disabled="item.override && item.tooltip"/>
              <a-tooltip v-if="item.tooltip" :title="item.tooltip">
                <a-checkbox v-model:checked="item.override" class="ml-2"/>
              </a-tooltip>
            </div>

            <div v-if="item.type === 'SELECT'" class="flex flex-row items-start font-normal space-x-1">
              <a-select v-model:value="item.value" :disabled="item.override && item.tooltip" :style="{ width: '100%' }">
                <a-select-option v-for="option in item.defaultValues" :key="option" :value="option">
                  {{ option }}
                </a-select-option>
              </a-select>
              <a-tooltip v-if="item.tooltip" :title="item.tooltip">
                <a-checkbox v-model:checked="item.override" class="ml-2"/>
              </a-tooltip>
            </div>

            <div v-if="item.type === 'NUMBER'" class="flex flex-row items-start font-normal space-x-1">
              <a-input-number v-model:value="item.value" :disabled="item.override && item.tooltip" :style="{width: item.width + 'px'}"/>
              <a-tooltip v-if="item.tooltip" :title="item.tooltip">
                <a-checkbox v-model:checked="item.override" class="ml-2"/>
              </a-tooltip>
            </div>
          </a-form-item>
        </div>
      </a-form>

      <template #footer>
        <a-button type="primary" @click="onSubmit()">
          <ShadcnIcon icon="Save"/>
          <span>{{ $t('common.save') }}</span>
        </a-button>
      </template>
    </a-drawer>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { Configuration } from '@/views/components/editor/flow/Configuration.ts'

defineOptions({ name: 'FlowConfigure' })

const props = withDefaults(defineProps<{ isVisible?: boolean; data?: Configuration | null }>(), {
  isVisible: false
})
const emit = defineEmits<{
  (e: 'close', value: boolean): void
  (e: 'onChange', value: any): void
}>()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const formState = ref({
  items: props.data?.configure || []
})

const filterConfigure = (meta: any) => {
  const result = {} as any
  meta.filter((item: { input: any }) => item.input)
      .forEach((item: { origin: any; value: any }) => {
        if (item.value !== null) {
          result[item.origin] = item.value
        }
      })
  return result
}

const onCancel = () => {
  visible.value = false
}

const onSubmit = () => {
  if (props.data) {
    const configure = {
      code: props.data.code,
      protocol: props.data.protocol,
      configures: filterConfigure(formState.value.items),
      type: props.data.nodeType
    }
    emit('onChange', configure)
  }
}
</script>
