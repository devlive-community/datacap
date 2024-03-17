<template>
  <Loader2 v-if="show" class="w-full justify-center animate-spin mt-10"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Loader2 } from 'lucide-vue-next'

export default defineComponent({
  name: 'CircularLoading',
  components: {Loader2},
  props: {
    show: {
      type: Boolean,
      default: () => false
    }
  }
})
</script>
