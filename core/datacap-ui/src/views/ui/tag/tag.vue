<template>
  <Badge class="cursor-pointer" :style="{backgroundColor: color}">
    <span v-if="content">{{ content }}</span>
    <slot v-else/>
  </Badge>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Badge } from '@/components/ui/badge'

export default defineComponent({
  name: 'DcBadge',
  components: { Badge },
  props: {
    content: {
      type: String
    },
    color: {
      type: String
    }
  }
})
</script>
