<template>
  <header class="sticky z-40 top-0 bg-background/80 backdrop-blur-lg border-b border-border">
    <div class="container flex h-14 max-w-screen-2xl items-center">
      <div class="flex h-16 items-center px-4">
        <Avatar>
          <AvatarImage src="/static/images/logo.png"></AvatarImage>
          <AvatarFallback>DataCap</AvatarFallback>
        </Avatar>

        <div class="container flex h-14 max-w-screen-2xl items-center">
          <nav :class="cn('flex items-center space-x-4 lg:space-x-6', $attrs.class ?? '')">
            <a href="/#auth/signin" class="text-sm font-medium transition-colors hover:text-primary">
              Overview
            </a>
            <a href="/#auth/signin" class="text-sm font-medium text-muted-foreground transition-colors hover:text-primary">
              Customers
            </a>
          </nav>
        </div>


        <MainNav class="mx-6"/>
        <div class="ml-auto flex items-center space-x-4">
          <Search/>
          <UserNav/>
        </div>
      </div>
    </div>
  </header>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { cn } from '@/lib/utils'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';

export default defineComponent({
  name: 'LayoutHeader',
  methods: {cn},
  components: {AvatarFallback, AvatarImage, Avatar, cn}
});
</script>

