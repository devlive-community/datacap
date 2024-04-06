<template>
  <Card>
    <CardHeader class="flex flex-row items-center border-b p-4">
      <div class="grid gap-2">
        <CardTitle>
          <span v-if="title">{{ title }}</span>
          <slot v-else name="title"/>
        </CardTitle>
      </div>
      <slot name="extra"/>
    </CardHeader>
    <CardContent>
      <slot/>
    </CardContent>
  </Card>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'

export default defineComponent({
  name: 'DcCard',
  components: {
    Card, CardContent, CardHeader, CardTitle
  },
  props: {
    title: {
      type: String
    }
  }
})
</script>
