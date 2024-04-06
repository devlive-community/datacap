<template>
  <div class="w-full">
    <Card>
      <template #title>{{ $t('source.common.list') }}</template>
    </Card>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Card from '@/views/ui/card'

export default defineComponent({
  name: 'SourceHome',
  components: { Card }
})
</script>
