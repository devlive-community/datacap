<template>
  <Badge class="cursor-pointer">
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
    }
  }
})
</script>
