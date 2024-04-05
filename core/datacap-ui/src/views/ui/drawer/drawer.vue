<template>
  <Sheet :open="visible" class="w-full" @update:open="handlerCancel">
    <SheetContent :class="`min-w-[${width}]`">
      <SheetHeader class="border-b pb-3">
        <SheetTitle class="-mt-3">
          <span v-if="title">{{ title }}</span>
          <slot v-else name="title"/>
        </SheetTitle>
      </SheetHeader>
      <slot/>
      <SheetFooter class="absolute bottom-0 left-0 right-0 mb-3 mr-3 pt-3 border-t">
        <SheetClose as-child>
          <slot name="footer"/>
        </SheetClose>
      </SheetFooter>
    </SheetContent>
  </Sheet>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Sheet, SheetClose, SheetContent, SheetDescription, SheetFooter, SheetHeader, SheetTitle, SheetTrigger } from '@/components/ui/sheet'

export default defineComponent({
  name: 'DcDrawer',
  components: {
    Sheet, SheetClose, SheetContent, SheetDescription, SheetFooter, SheetHeader, SheetTitle, SheetTrigger
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
