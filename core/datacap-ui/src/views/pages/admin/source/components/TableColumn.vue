<template>
  <a-drawer v-model:open="visible" width="20%" :title="$t('source.common.visibleColumn')">
    <a-checkbox-group v-model:value="value">
      <a-space direction="vertical">
        <a-checkbox v-for="item in columns" :key="item.field" :value="item.field">{{ item.field }}</a-checkbox>
      </a-space>
    </a-checkbox-group>

    <template #footer>
      <a-button type="primary" @click="onCancel">
        {{ $t('common.apply') }}
      </a-button>
    </template>
  </a-drawer>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'

defineOptions({ name: 'TableColumn' })

const props = withDefaults(defineProps<{ isVisible?: boolean; columns?: any[] }>(), {
  isVisible: false,
  columns: () => []
})
const emit = defineEmits<{
  (e: 'close', value: boolean): void
  (e: 'change', value: string[]): void
}>()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const value = ref<string[]>(props.columns.map((item) => item.field))

const onCancel = () => {
  emit('change', value.value)
  visible.value = false
}
</script>
