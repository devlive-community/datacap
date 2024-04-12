<template>
  <Carousel :items="carouselItems" :delay="3000"/>
  <header class="sticky z-40 top-0 bg-background/80 backdrop-blur-lg border-b border-border">
    <div class="container flex h-14 max-w-screen-2xl items-center">
      <div class="container flex h-14 items-center justify-between">
        <!-- Logo -->
        <RouterLink to="/">
          <Avatar>
            <AvatarImage src="/static/images/logo.png"></AvatarImage>
            <AvatarFallback>DataCap</AvatarFallback>
          </Avatar>
        </RouterLink>
        <!-- Menu -->
        <div class="container flex h-14 max-w-screen-2xl items-center">
          <NavigationMenu>
            <NavigationMenuList>
              <div v-for="item in activeMenus" :key="item.id">
                <NavigationMenuItem v-if="item.children">
                  <NavigationMenuTrigger :class="`${item.children.map(i => i.url).includes($route.path) ? 'bg-muted hover:bg-muted' : ''}`">
                    {{ $t(item.i18nKey) }}
                  </NavigationMenuTrigger>
                  <NavigationMenuContent>
                    <ul class="grid gap-3 p-6 md:w-[400px] lg:w-[500px] lg:grid-cols-[minmax(0,.75fr)_minmax(0,1fr)]">
                      <NavigationMenuListItem v-for="children in item.children" :title="$t(children.i18nKey)" :href="children.url">
                        {{ children.description }}
                      </NavigationMenuListItem>
                    </ul>
                  </NavigationMenuContent>
                </NavigationMenuItem>
                <NavigationMenuItem v-else>
                  <NavigationMenuLink :class="cn(navigationMenuTriggerStyle(), $route.path === `${item.url}` && 'bg-muted hover:bg-muted')">
                    <RouterLink :to="item.url">
                      {{ $t(item.i18nKey) }}
                    </RouterLink>
                  </NavigationMenuLink>
                </NavigationMenuItem>
              </div>
            </NavigationMenuList>
          </NavigationMenu>
        </div>
        <!-- Controller -->
        <div class="flex items-center">
          <div class="mr-3 mt-1.5">
            <TooltipProvider>
              <Tooltip>
                <TooltipTrigger>
                  <a target="_blank" href="https://github.com/devlive-community/datacap/issues/new/choose">
                    <CircleHelp :size="20"/>
                  </a>
                </TooltipTrigger>
                <TooltipContent>{{ $t('common.feedback') }}</TooltipContent>
              </Tooltip>
            </TooltipProvider>
          </div>
          <div class="ml-3 mr-5 mt-0.5">
            <LanguageSwitcher @changeLanguage="handlerChangeLanguage($event)"/>
          </div>
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
                <Button variant="ghost" class="relative h-8 w-8 rounded-full mt-2">
                  <Avatar class="h-8 w-8">
                    <AvatarImage :src="userInfo.avatar" :alt="userInfo.username"></AvatarImage>
                    <AvatarFallback>{{ userInfo.username }}</AvatarFallback>
                  </Avatar>
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent class="w-32" align="end">
                <DropdownMenuLabel class="font-normal flex">
                  <div class="flex flex-col space-y-1">
                    <p class="text-sm font-medium leading-none text-center">{{ userInfo.username }}</p>
                    <p class="text-xs leading-none text-muted-foreground"></p>
                  </div>
                </DropdownMenuLabel>
                <DropdownMenuSeparator/>
                <DropdownMenuGroup>
                  <DropdownMenuItem style="cursor: pointer;">
                    <RouterLink to="/admin/user">
                      <div class="flex items-center">
                        <Settings absoluteStrokeWidth :size="18" class="mr-2"/>
                        {{ $t('user.common.setting') }}
                      </div>
                    </RouterLink>
                  </DropdownMenuItem>
                </DropdownMenuGroup>
                <DropdownMenuSeparator/>
                <DropdownMenuItem style="cursor: pointer;" @click="logout">
                  <LogOut absoluteStrokeWidth :size="15" class="mr-2"></LogOut>
                  {{ $t('user.common.signout') }}
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
import CommonUtils from '@/utils/common'
import router from '@/router'
import { createDefaultRouter } from '@/router/default'
import { AuthResponse } from '@/model/user/response/auth'
import {
  NavigationMenu,
  NavigationMenuContent,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
  NavigationMenuTrigger,
  navigationMenuTriggerStyle
} from '@/components/ui/navigation-menu'
import NavigationMenuListItem from '@/views/layouts/common/components/components/NavigationMenuListItem.vue'
import { CircleHelp, LogOut, Settings } from 'lucide-vue-next'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'
import LanguageSwitcher from '@/views/layouts/common/components/components/LanguageSwitcher.vue'
import Carousel from '@/views/ui/carousel'

interface NavigationItem
{
  id: number
  i18nKey: string
  url: string
  description: string
  children: NavigationItem[] | undefined
}

export default defineComponent({
  name: 'LayoutHeader',
  setup()
  {
    const user = TokenUtils.getAuthUser()
    let userInfo = ref<AuthResponse>({} as AuthResponse)
    if (user) {
      userInfo.value = user
    }
    const isLoggedIn = ref(ObjectUtils.isEmpty(userInfo.value))

    const menu = TokenUtils.getUserMenu()
    let activeMenus = ref(Array<NavigationItem>())
    if (ObjectUtils.isNotEmpty(menu)) {
      activeMenus.value = menu
    }

    const logout = () => {
      localStorage.removeItem(CommonUtils.token)
      localStorage.removeItem(CommonUtils.menu)
      localStorage.removeItem(CommonUtils.userEditorConfigure)
      createDefaultRouter(router)
      router.push('/auth/signin')
    }

    const carouselItems = [
      {
        title: 'Support ChatGPT',
        link: '/admin/chat',
        external: false,
        isAlert: true
      }
    ]

    return {
      userInfo,
      isLoggedIn,
      activeMenus,
      logout,
      carouselItems
    }
  },
  components: {
    Carousel,
    LanguageSwitcher,
    TooltipContent, Tooltip, TooltipTrigger, TooltipProvider,
    NavigationMenuLink, NavigationMenuContent, NavigationMenuTrigger, NavigationMenuItem, NavigationMenuList, NavigationMenu,
    DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuGroup, DropdownMenuItem, DropdownMenuShortcut, DropdownMenuContent, DropdownMenuTrigger, DropdownMenu,
    AvatarFallback, AvatarImage, Avatar,
    NavigationMenuListItem,
    Button,
    CircleHelp, LogOut, Settings
  },
  methods: {
    navigationMenuTriggerStyle,
    cn,
    handlerChangeLanguage(language: string)
    {
      this.$emit('changeLanguage', language)
    }
  }
})
</script>

