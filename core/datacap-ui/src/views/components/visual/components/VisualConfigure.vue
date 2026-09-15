<template>
  <a-drawer v-model:open="visible"
            width="40%"
            style="margin-right: -1rem;"
            :title="$t('common.configure')"
            @close="onCancel">
    <a-form v-if="formState" :model="formState" class="mt-2" layout="vertical">
      <a-tabs v-model:activeKey="activeGroup" tab-position="right">
        <a-tab-pane v-for="group in fieldGroup"
                    :key="group.label"
                    :tab="group.label">
          <a-form-item v-for="item in fieldGroup.find(value => value.label === activeGroup)?.fields"
                       :key="item.field"
                       :name="item.field"
                       :label="item.label">
            <a-switch v-if="item.type === 'SWITCH'" v-model:checked="formState[Object(item.field)]"/>

            <a-slider v-else-if="item.type === 'SLIDER'"
                      v-model:value="formState[Object(item.field)]"
                      :min="item.min"
                      :max="item.max"
                      :step="item.step"/>

            <a-input v-else-if="item.type === 'TEXT'"
                     v-model:value="formState[Object(item.field)]"
                     :placeholder="item.label"
                     :disabled="item.disabled?.field ? formState[Object(item.disabled?.field)] === item.disabled?.value : false"/>

            <a-select v-else
                      v-model:value="formState[Object(item.field)]"
                      :disabled="item.disabled?.field ? formState[Object(item.disabled?.field)] === item.disabled?.value : false">
              <template v-if="item.values">
                <a-select-option v-for="data in item.values" :key="data.value" :value="data.value">
                  {{ data.label }}
                </a-select-option>
              </template>

              <template v-else>
                <a-select-option v-for="header in configuration.headers" :key="header" :value="header">
                  {{ header }}
                </a-select-option>
              </template>
            </a-select>
          </a-form-item>
        </a-tab-pane>
      </a-tabs>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onCancel">
          {{ $t('common.cancel') }}
        </a-button>
        <a-button type="primary" @click="onSubmit">
          {{ $t('common.apply') }}
        </a-button>
      </a-space>
    </template>
  </a-drawer>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { ChartFieldGroup, Configuration, IChart } from '@/views/components/visual/Configuration.ts'
import { cloneDeep, keys } from 'lodash'

defineOptions({ name: 'VisualConfigure' })

const props = withDefaults(defineProps<{
  configuration?: Configuration
  fieldGroup?: ChartFieldGroup[]
  isVisible?: boolean
}>(), {
  fieldGroup: () => [],
  isVisible: false
})
const emit = defineEmits<{
  (e: 'close', value: boolean): void
  (e: 'change', value: IChart | null): void
}>()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const activeGroup = ref(props.fieldGroup[0]?.label)
const formState = ref<IChart | null>(null)

if (props.configuration && keys(props.configuration.chartConfigure).length > 0) {
  formState.value = cloneDeep(props.configuration.chartConfigure) as IChart
}
else {
  const obj = {} as any
  props.fieldGroup.forEach(group => {
    group?.fields?.forEach(field => {
      if (field.field) {
        obj[field.field] = undefined
      }
    })
  })
  formState.value = obj
}

const onCancel = () => {
  visible.value = false
}

const onSubmit = () => {
  emit('change', formState.value)
  onCancel()
}
</script>
