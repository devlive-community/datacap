<template>
  <Card :body-class="'p-8'" :hidden-title="true">
    <Alert :description="$t('source.tip.notSelectedNode')"/>
  </Card>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Card from '@/views/ui/card'
import Alert from '@/views/ui/alert'

export default defineComponent({
  name: 'SourceDatabase',
  components: {
    Card,
    Alert
  }
})
</script>
