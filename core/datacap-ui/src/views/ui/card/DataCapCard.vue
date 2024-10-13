<template>
  <Card :class="cn('rounded-sm', computedShadow)">
    <CardHeader v-if="$slots.title || title" :class="cn('flex flex-row items-center justify-between border-b p-3')">
      <div class="grid gap-2">
        <CardTitle>
          <span v-if="title">{{ title }}</span>
          <slot v-else name="title"/>
        </CardTitle>
      </div>
      <div v-if="$slots.extra">
        <slot name="extra"/>
      </div>
    </CardHeader>
    <CardContent v-if="$slots.content" class="p-0">
      <slot name="content"/>
    </CardContent>
    <CardFooter v-if="$slots.footer" class="border-t p-4">
      <slot name="footer"/>
    </CardFooter>
  </Card>
</template>

<script lang="ts">
import { defineComponent, ref, watchEffect } from 'vue'
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '@/components/ui/card'
import { Shadow } from '@/enums/shadow.ts'
import { cn } from '@/lib/utils.ts'

export default defineComponent({
  name: 'DataCapCard',
  components: {
    Card, CardHeader, CardTitle, CardContent, CardFooter
  },
  props: {
    title: {
      type: String
    },
    shadow: {
      type: String,
      default: 'never'
    }
  },
  setup(props)
  {
    const computedShadow = ref<string>('never')

    watchEffect(() => {
      computedShadow.value = Shadow[props.shadow as keyof typeof Shadow]
    })

    return {
      cn,
      computedShadow
    }
  }
})
</script>
