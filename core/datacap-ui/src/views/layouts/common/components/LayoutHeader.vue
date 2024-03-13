<template>
  <header class="sticky z-40 top-0 bg-background/80 backdrop-blur-lg border-b border-border">
    <div class="container flex h-14 max-w-screen-2xl items-center">
      <div class="container flex h-14 items-center justify-between">
        <!-- Logo -->
        <RouterLink to="1">
          <Avatar>
            <AvatarImage src="/static/images/logo.png"></AvatarImage>
            <AvatarFallback>DataCap</AvatarFallback>
          </Avatar>
        </RouterLink>
        <!-- Menu -->
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
        <!-- Controller -->
        <div class="flex items-center">
          <Search/>
          <div v-if="isLoggedIn" class="flex gap-x-2">
            <RouterLink to="/auth/signin">
              <Button size="sm" variant="outline">{{ $t('user.common.signin') }}</Button>
            </RouterLink>
            <RouterLink to="/auth/signup" as-child>
              <Button size="sm" variant="link">{{ $t('user.common.signup') }}</Button>
            </RouterLink>
          </div>
          <div v-else>
            <DropdownMenu>
              <DropdownMenuTrigger as-child>
                <Button variant="ghost" class="relative h-8 w-8 rounded-full">
                  <Avatar class="h-8 w-8">
                    <AvatarImage src="/avatars/01.png" alt="@shadcn"/>
                    <AvatarFallback>SC</AvatarFallback>
                  </Avatar>
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent class="w-56" align="end">
                <DropdownMenuLabel class="font-normal flex">
                  <div class="flex flex-col space-y-1">
                    <p class="text-sm font-medium leading-none">
                      shadcn
                    </p>
                    <p class="text-xs leading-none text-muted-foreground">
                      m@example.com
                    </p>
                  </div>
                </DropdownMenuLabel>
                <DropdownMenuSeparator/>
                <DropdownMenuGroup>
                  <DropdownMenuItem>
                    Profile
                    <DropdownMenuShortcut>⇧⌘P</DropdownMenuShortcut>
                  </DropdownMenuItem>
                  <DropdownMenuItem>
                    Billing
                    <DropdownMenuShortcut>⌘B</DropdownMenuShortcut>
                  </DropdownMenuItem>
                  <DropdownMenuItem>
                    Settings
                    <DropdownMenuShortcut>⌘S</DropdownMenuShortcut>
                  </DropdownMenuItem>
                  <DropdownMenuItem>New Team</DropdownMenuItem>
                </DropdownMenuGroup>
                <DropdownMenuSeparator/>
                <DropdownMenuItem>
                  Log out
                  <DropdownMenuShortcut>⇧⌘Q</DropdownMenuShortcut>
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { cn } from '@/lib/utils'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuShortcut,
  DropdownMenuTrigger
} from '@/components/ui/dropdown-menu'
import { Button } from '@/components/ui/button'
import { TokenUtils } from '@/utils/token'
import { ObjectUtils } from '@/utils/object'

export default defineComponent({
  name: 'LayoutHeader',
  setup()
  {
    const isLoggedIn = ref(ObjectUtils.isEmpty(TokenUtils.getAuthUser()))

    return {
      isLoggedIn
    }
  },
  methods: {cn},
  components: {
    DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuGroup, DropdownMenuItem, DropdownMenuShortcut, DropdownMenuContent, DropdownMenuTrigger, DropdownMenu,
    AvatarFallback, AvatarImage, Avatar,
    Button, cn
  }
});
</script>

