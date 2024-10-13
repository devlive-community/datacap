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
    <CardContent v-else class="p-0">
      <slot/>
    </CardContent>
    <CardFooter v-if="$slots.footer" class="border-t p-4">
      <slot name="footer"/>
    </CardFooter>
  </Card>
</template>

<script setup lang="ts">
import { ref, watchEffect } from 'vue'
import { cn } from '@/lib/utils'
import { Shadow } from '@/enums/shadow.ts'
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '@/components/ui/card'

const props = defineProps<{
  title?: string
  shadow?: keyof typeof Shadow
}>()

const computedShadow = ref<string>('never')

watchEffect(() => {
  computedShadow.value = Shadow[props.shadow || 'never']
})
</script>
