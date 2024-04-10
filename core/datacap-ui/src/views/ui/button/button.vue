<template>
  <Button :size="size as any" :disabled="disabled" :style="{backgroundColor: color}">
    <Loader2 v-if="loading" class="w-3.5 h-3.5 mr-2 animate-spin"/>
    <RouterLink v-if="to" :to="to" target="_blank">
      <slot/>
    </RouterLink>
    <slot v-else/>
  </Button>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Button } from '@/components/ui/button'
import { Loader2 } from 'lucide-vue-next'

export default defineComponent({
  name: 'DcButton',
  components: {
    Button,
    Loader2
  },
  props: {
    size: {
      type: String
    },
    color: {
      type: String
    },
    loading: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean
    },
    to: {
      type: String
    }
  }
})
</script>
