<template>
  <div class="flex items-center space-x-4 text-sm flex-row w-full overflow-hidden">
    <Separator :class="cn(position === Position.left && 'w-10')" :orientation="orientation as any"/>
    <div class="flex items-center flex-grow">
      <slot name="content"/>
    </div>
    <Separator :class="cn(position === Position.right && 'w-10')" :orientation="orientation as any"/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { cn } from '@/lib/utils'
import { Separator } from '@/components/ui/separator'

enum Position
{
  left = 'left',
  right = 'right'
}

export default defineComponent({
  name: 'DcDivider',
  components: { Separator },
  props: {
    position: {
      type: String
    },
    orientation: {
      type: String
    }
  },
  computed: {
    Position()
    {
      return Position
    }
  },
  setup()
  {
    return {
      cn
    }
  }
})
</script>
