<template>
  <div class='h-svh'>
    <div class='m-auto flex h-full w-full flex-col items-center justify-center gap-2'>
      <h1 class='text-[7rem] font-bold leading-tight'>500</h1>
      <span class='font-medium'>{{ $t('common.pageNotNetwork') }}</span>
      <p class='text-center text-muted-foreground mt-6'>{{ $t('common.tip.pageNotNetwork') }}</p>
      <div class='mt-6 flex gap-4'>
        <DcLink :link="redirect as string" target="_self">
          <Button variant='outline'>{{ $t('common.backTo') }}</Button>
        </DcLink>
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
import DcLink from '@/views/components/link/DcLink.vue'

export default defineComponent({
  name: 'NotNetwork',
  components: { DcLink, Button },
  data()
  {
    return {
      redirect: null as string | null
    }
  },
  created()
  {
    if (this.$route.query) {
      this.redirect = this.$route.query.redirect as string
    }
  }
})
</script>
