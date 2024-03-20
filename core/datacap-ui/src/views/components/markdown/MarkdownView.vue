<template>
  <div>
    <AlertDialog :default-open="visible">
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle class="border-b -mt-4 pb-2">{{ $t('common.content') }}</AlertDialogTitle>
        </AlertDialogHeader>
        <MdPreview v-if="content" :modelValue="content" style="padding: 0"/>
        <AlertDialogFooter class="-mb-4 border-t pt-2">
          <Button variant="outline" @click="handlerCancel">{{ $t('common.cancel') }}</Button>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue'
import { AlertDialog, AlertDialogContent, AlertDialogFooter, AlertDialogHeader } from '@/components/ui/alert-dialog'
import { Button } from '@/components/ui/button'
import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

export default defineComponent({
  name: 'MarkdownPreview',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    content: {
      type: String,
      default: () => ''
    }
  },
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible
      },
      set(value: boolean)
      {
        this.$emit('close', value)
      }
    }
  },
  components: {
    AlertDialogFooter, Button, AlertDialogHeader, AlertDialogContent, AlertDialog,
    MdPreview
  },
  methods: {
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>

<style scoped>
:deep(.md-editor-preview-wrapper) {
  padding: 0;
}

:deep(.default-theme pre) {
  margin: 0;
}
</style>