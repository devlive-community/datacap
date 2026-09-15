<template>
  <a-modal v-model:open="visible" title="Show Content" width="40%" :footer="null">
    <AceEditor v-if="content" :value="content" :read-only="true"/>

    <template #footer>
      <a-button danger @click="onCancel">
        {{ $t('common.cancel') }}
      </a-button>
    </template>
  </a-modal>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import AceEditor from '@/views/components/editor/AceEditor.vue'

defineOptions({ name: 'SqlInfo' })

const props = withDefaults(defineProps<{ isVisible?: boolean; content?: string | null }>(), {
  isVisible: false,
  content: null
})
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const onCancel = () => {
  visible.value = false
}
</script>
