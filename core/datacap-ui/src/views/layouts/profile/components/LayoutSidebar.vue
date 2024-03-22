<template>
  <nav class="flex space-x-2 lg:flex-col lg:space-x-0 lg:space-y-1">
    <div v-for="item in items">
      <div v-if="item.isDriver" class="mt-2 mb-2">
        <Separator class="right"/>
      </div>
      <Button v-else :key="item.title" variant="ghost" :class="cn('w-full text-left justify-start', $route.path === `${item.href}` && 'bg-muted hover:bg-muted',)">
        <RouterLink :to="item.href as string" class="w-full">{{ item.title }}</RouterLink>
      </Button>
    </div>
  </nav>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { cn } from '@/lib/utils'
import { Button } from '@/components/ui/button'
import { Separator } from '@/components/ui/separator'

interface NavigationItem
{
  title?: string
  href?: string
  isDriver?: boolean
}

export default defineComponent({
  name: 'LayoutSidebar',
  components: {Separator, Button},
  setup()
  {
    return {
      cn
    }
  },
  data()
  {
    return {
      items: [] as NavigationItem[]
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      const items: NavigationItem[] = [
        {title: this.$t('user.common.profile'), href: '/admin/user/profile'},
        {isDriver: true},
        {title: this.$t('user.common.info'), href: '/admin/user/info'},
        {title: this.$t('user.common.log'), href: '/admin/user/log'},
        {title: this.$t('user.common.editor'), href: '/admin/user/editor'},
        {title: this.$t('user.common.assistant'), href: '/admin/user/assistant'},
        {isDriver: true},
        {title: this.$t('user.common.modifyPassword'), href: '/admin/user/password'},
        {title: this.$t('user.common.modifyUsername'), href: '/admin/user/username'}
      ]
      this.items = [...items]
    }
  }
});
</script>
