<template>
  <a-modal v-model:open="visible" :title="$t('source.common.jumpPage')" :footer="null" @cancel="onCancel">
    <a-form v-if="formState" :model="formState" layout="vertical" @finish="onSubmit">
      <a-form-item name="showPageSize" :label="$t('source.common.showPageSize')">
        <a-input-number v-model:value="formState.size" :min="1" :max="10000" :style="{ width: '100%' }"/>
      </a-form-item>

      <a-button type="primary" html-type="submit">
        {{ $t('common.apply') }}
      </a-button>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'

defineOptions({ name: 'TablePagination' })

const props = withDefaults(defineProps<{ isVisible?: boolean; pagination?: any }>(), { isVisible: false })
const emit = defineEmits<{ (e: 'close', value: boolean): void; (e: 'change', value: any): void }>()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const formState = ref<any>(props.pagination ? props.pagination : { size: 500 })

const onCancel = () => {
  visible.value = false
}

const onSubmit = () => {
  visible.value = false
  emit('change', formState.value)
}
</script>
