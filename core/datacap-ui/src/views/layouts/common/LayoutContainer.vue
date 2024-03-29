<template>
  <div>
    <div class="hidden flex-col md:flex">
      <LayoutHeader/>
      <LayoutBreadcrumb/>
      <div class="flex-1 space-y-4 pl-8 pr-8">
        <RouterView />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import LayoutHeader from '@/views/layouts/common/components/LayoutHeader.vue'
import { Button } from '@/components/ui/button'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import LayoutBreadcrumb from '@/views/layouts/common/components/LayoutBreadcrumb.vue'
import { TokenUtils } from '@/utils/token'
import { ObjectUtils } from '@/utils/object'
import { HttpUtils } from '@/utils/http'
import UserService from '@/services/user'
import CommonUtils from '@/utils/common'

export default defineComponent({
  name: 'LayoutContainer',
  components: {LayoutBreadcrumb, AvatarFallback, AvatarImage, Avatar, Button, LayoutHeader},
  beforeUnmount()
  {
    clearInterval(this.timer)
  },
  data()
  {
    return {
      timer: null as any
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      const user = TokenUtils.getAuthUser()
      if (ObjectUtils.isNotEmpty(user)) {
        this.timer = setInterval(() => {
          const runTime = new Date().toLocaleTimeString()
          console.log(`[DataCap] refresh on time ${runTime}`)
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
    }
  }
});
</script>
