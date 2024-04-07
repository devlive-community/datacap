<template>
  <div class="flex items-center space-x-4 text-sm flex-row w-full overflow-hidden">
    <Separator :class="cn(orientation === Position.left && 'w-10')"/>
    <div class="flex items-center flex-grow">
      <slot name="content"/>
    </div>
    <Separator :class="cn(orientation === Position.right && 'w-10')"/>
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
