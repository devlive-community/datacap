<template>
  <AlertDialog :open="visible" @update:open="handlerCancel">
    <AlertDialogContent :class="`p-0 min-w-[${width}] max-w-[${width}] w-[${width}]`">
      <AlertDialogHeader>
        <AlertDialogTitle class="border-b p-2">
          <span v-if="title">{{ title }}</span>
          <slot v-else name="title"/>
        </AlertDialogTitle>
      </AlertDialogHeader>
      <slot/>
      <AlertDialogFooter class="border-t p-2">
        <slot name="footer"/>
      </AlertDialogFooter>
    </AlertDialogContent>
  </AlertDialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle } from '@/components/ui/alert-dialog'

export default defineComponent({
  name: 'DcDialog',
  components: {
    AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle
  },
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    title: {
      type: String
    },
    width: {
      type: String,
      default: '25%'
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
  methods: {
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>