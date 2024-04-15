<template>
  <AlertDialog :default-open="visible">
    <AlertDialogContent>
      <AlertDialogHeader>
        <AlertDialogTitle class="border-b -mt-4 pb-2">SQL</AlertDialogTitle>
      </AlertDialogHeader>
      <AceEditor :value="content as string" read-only/>
      <AlertDialogFooter class="-mb-4 border-t pt-2">
        <Button @click="handlerCancel">{{ $t('common.cancel') }}</Button>
      </AlertDialogFooter>
    </AlertDialogContent>
  </AlertDialog>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger
} from '@/components/ui/alert-dialog'
import { Button } from '@/components/ui/button'
import AceEditor from '@/views/components/editor/AceEditor.vue'

export default defineComponent({
  name: 'SqlInfo',
  components: {
    AceEditor,
    Button,
    AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger
  },
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    content: {
      type: String as PropType<string | null>
    }
  },
  methods: {
    handlerCancel()
    {
      this.visible = false
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
  }
})
</script>
