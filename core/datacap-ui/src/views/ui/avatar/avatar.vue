<template>
  <Avatar :size="size as any" class="border-2 border-background w-8 h-8 cursor-pointer">
    <AvatarImage :src="src"/>
    <AvatarFallback>{{ alt }}</AvatarFallback>
  </Avatar>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'

export default defineComponent({
  name: 'DcAvatar',
  components: { Avatar, AvatarFallback, AvatarImage },
  props: {
    size: {
      type: String,
      default: 'sm'
    },
    src: {
      type: String,
      default: null
    },
    alt: {
      type: String
    }
  }
})
</script>
