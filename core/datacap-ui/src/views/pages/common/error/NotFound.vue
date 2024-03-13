<template>
  <div class='h-svh'>
    <div class='m-auto flex h-full w-full flex-col items-center justify-center gap-2'>
      <h1 class='text-[7rem] font-bold leading-tight'>404</h1>
      <span class='font-medium'>{{ $t('common.pageNotFound') }}</span>
      <p class='text-center text-muted-foreground mt-6'>{{ $t('common.pageNotFoundTip') }}</p>
      <div class='mt-6 flex gap-4'>
        <RouterLink to="/">
          <Button variant='outline'>{{ $t('common.backToHome') }}</Button>
        </RouterLink>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Button } from '@/components/ui/button'

export default defineComponent({
  name: 'NotFound',
  components: {Button}
})
</script>
