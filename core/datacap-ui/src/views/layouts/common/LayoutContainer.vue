<template>
  <div>
    <div class="hidden flex-col md:flex">
      <LayoutHeader/>
      <LayoutBreadcrumb/>
      <div class="flex-1 space-y-4 pl-8 pr-8 min-h-[700px]">
        <RouterView/>
      </div>
      <LayoutFooter :data="footers"/>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import LayoutHeader from '@/views/layouts/common/components/LayoutHeader.vue'
import { Button } from '@/components/ui/button'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import LayoutBreadcrumb from '@/views/layouts/common/components/LayoutBreadcrumb.vue'
import LayoutFooter from '@/views/layouts/common/components/LayoutFooter.vue'
import { TokenUtils } from '@/utils/token'
import { ObjectUtils } from '@/utils/object'
import { HttpUtils } from '@/utils/http'
import UserService from '@/services/user'
import CommonUtils from '@/utils/common'
import { FooterModel } from '@/views/layouts/common/components/model/footer.ts'

export default defineComponent({
  name: 'LayoutContainer',
  components: {
    LayoutBreadcrumb,
    AvatarFallback, AvatarImage, Avatar,
    Button,
    LayoutHeader,
    LayoutFooter
  },
  beforeUnmount()
  {
    clearInterval(this.timer)
  },
  data()
  {
    return {
      timer: null as any,
      footers: [] as Array<FooterModel>
    }
  },
  created()
  {
    this.handlerInitialize()
    this.handlerInitializeFooter()
  },
  methods: {
    handlerInitialize()
    {
      const user = TokenUtils.getAuthUser()
      if (ObjectUtils.isNotEmpty(user)) {
        this.timer = setInterval(() => {
          const runTime = new Date().toLocaleTimeString()
          console.log(`[DataCap] refresh on time ${ runTime }`)
          const client = new HttpUtils().getAxios()
          client.all([UserService.getMenus(), UserService.getInfo()])
                .then(client.spread((fetchMenu, fetchInfo) => {
                  if (fetchMenu.status && fetchInfo.status) {
                    localStorage.setItem(CommonUtils.menu, JSON.stringify(fetchMenu.data))
                    localStorage.setItem(CommonUtils.userEditorConfigure, JSON.stringify(fetchInfo.data.editorConfigure))
                  }
                }))
        }, 1000 * 60)
      }
    },
    handlerInitializeFooter()
    {
      const footers = new Array<FooterModel>()
      footers.push({
        title: 'Resources',
        children: [
          {
            title: 'Blog',
            link: 'https://datacap.devlive.org/',
            external: true,
            blank: '_blank'
          },
          {
            title: 'Gitee',
            link: 'https://gitee.com/devlive-community/datacap',
            external: true,
            blank: '_blank'
          },
          {
            title: 'Github',
            link: 'https://github.com/devlive-community/datacap',
            external: true,
            blank: '_blank'
          },
          {
            title: 'Documentation',
            link: 'https://datacap.devlive.org/',
            external: true,
            blank: '_blank'
          }
        ]
      })
      footers.push({
        title: 'Community',
        children: [
          {
            title: 'Website',
            link: 'https://datacap.devlive.org/',
            external: true,
            blank: '_blank'
          },
          {
            title: 'Issues',
            link: 'https://github.com/devlive-community/datacap/issues',
            external: true,
            blank: '_blank'
          },
          {
            title: 'Discussions',
            link: 'https://github.com/devlive-community/datacap/discussions',
            external: true,
            blank: '_blank'
          }
        ]
      })
      footers.push({
        title: 'About',
        children: [
          {
            title: 'DataCap',
            link: 'https://datacap.devlive.org/',
            external: true,
            blank: '_blank'
          }
        ]
      })
      footers.push({
        title: 'Projects',
        children: [
          {
            title: 'Database Tools',
            link: 'https://github.com/devlive-community/dbm',
            external: true,
            blank: '_blank'
          },
          {
            title: 'Open AI Java SDK',
            link: 'https://github.com/devlive-community/openai-java-sdk',
            external: true,
            blank: '_blank'
          },
          {
            title: 'Shadcn UI Vue Admin',
            link: 'https://github.com/devlive-community/shadcn-ui-vue-admin',
            external: true,
            blank: '_blank'
          }
        ]
      })
      this.footers = footers
    }
  }
})
</script>
