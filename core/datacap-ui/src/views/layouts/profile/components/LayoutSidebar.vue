<template>
  <nav class="flex space-x-2 lg:flex-col lg:space-x-0 lg:space-y-1">
    <Button v-for="item in items" :key="item.title" variant="ghost" :class="cn('w-full text-left justify-start', $route.path === `${item.href}` && 'bg-muted hover:bg-muted',)">
      <RouterLink :to="item.href" class="w-full">{{ item.title }}</RouterLink>
    </Button>
  </nav>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { cn } from '@/lib/utils'
import { Button } from '@/components/ui/button'

export default defineComponent({
  name: 'LayoutSidebar',
  components: {Button},
  setup()
  {
    return {
      cn
    }
  },
  data()
  {
    return {
      items: [
        {title: this.$t('user.common.info'), href: '/admin/user/info'},
        {title: this.$t('user.common.profile'), href: '/admin/user/profile'},
        {title: this.$t('user.common.log'), href: '/admin/user/log'},
        {title: this.$t('user.common.editor'), href: '/admin/user/editor'},
      ]
    }
  }
});
</script>
