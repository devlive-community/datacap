<template>
  <Card class="rounded-sm">
    <CardHeader v-if="!hiddenTitle" :class="`flex flex-row items-center justify-between border-b p-4 ${titleClass}`">
      <div class="grid gap-2">
        <CardTitle>
          <span v-if="title">{{ title }}</span>
          <slot v-else name="title"/>
        </CardTitle>
      </div>
      <div>
        <slot name="extra"/>
      </div>
    </CardHeader>
    <CardContent :class="`${bodyClass}`">
      <slot/>
    </CardContent>
    <CardFooter v-if="$slots.footer">
      <slot name="footer"/>
    </CardFooter>
  </Card>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '@/components/ui/card'

export default defineComponent({
  name: 'DcCard',
  components: {
    CardFooter, Card, CardContent, CardHeader, CardTitle
  },
  props: {
    title: {
      type: String
    },
    titleClass: {
      type: String
    },
    bodyClass: {
      type: String
    },
    hiddenTitle: {
      type: Boolean,
      default: false
    }
  }
})
</script>
