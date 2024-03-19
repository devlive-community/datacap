<template>
  <AlertDialog :default-open="visible">
    <AlertDialogContent>
      <AlertDialogHeader>
        <AlertDialogTitle class="border-b -mt-4 pb-2">SQL</AlertDialogTitle>
      </AlertDialogHeader>
      <VAceEditor v-if="configure" lang="mysql" :theme="configure.theme" :style="{height: '200px', fontSize: configure.fontSize + 'px'}" :value="localContent"
                  :options="editorOptions"/>
      <AlertDialogFooter class="-mb-4 border-t pt-2">
        <Button @click="handlerCancel">{{ $t('common.cancel') }}</Button>
      </AlertDialogFooter>
    </AlertDialogContent>
  </AlertDialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
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
import CommonUtils from '@/views/components/echarts/utils/CommonUtils'
import { UserEditor } from '@/model/user'
import { Button } from '@/components/ui/button'
import { VAceEditor } from 'vue3-ace-editor'
import '@/ace-editor-theme'

export default defineComponent({
  name: 'SqlInfo',
  components: {
    Button,
    AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger,
    VAceEditor
  },
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
  data()
  {
    return {
      localContent: this.content,
      configure: null as UserEditor | null,
      editorOptions: {readOnly: true}
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      const localEditorConfigure = localStorage.getItem(CommonUtils.userEditorConfigure)
      const defaultEditorConfigure: UserEditor = {fontSize: 12, theme: 'chrome'}
      this.configure = localEditorConfigure ? JSON.parse(localEditorConfigure) : defaultEditorConfigure
    },
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
