<template>
  <a-modal v-model:open="visible" :title="$t('common.content')" :footer="null" @cancel="onCancel">
    <MdPreview v-if="content" :modelValue="content" style="padding: 0"/>

    <template #footer>
      <a-button @click="onCancel">
        {{ $t('common.cancel') }}
      </a-button>
    </template>
  </a-modal>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

defineOptions({ name: 'MarkdownPreview' })

const props = withDefaults(defineProps<{ isVisible?: boolean; content?: string }>(), {
  isVisible: false,
  content: ''
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
