<template>
  <TooltipProvider :delay-duration="duration">
    <Tooltip>
      <TooltipTrigger as-child>
        <slot/>
      </TooltipTrigger>
      <TooltipContent>
        <p v-if="content">{{ content }}</p>
        <slot v-else name="content"/>
      </TooltipContent>
    </Tooltip>
  </TooltipProvider>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'

export default defineComponent({
  name: 'DcTooltip',
  components: {
    Tooltip, TooltipContent, TooltipProvider, TooltipTrigger
  },
  props: {
    content: {
      type: String
    },
    duration: {
      type: Number,
      default: 0
    }
  }
})
</script>
