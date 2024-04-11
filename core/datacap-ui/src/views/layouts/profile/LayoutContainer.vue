<template>
  <div>
    <LayoutHeader/>
    <div class="flex flex-col space-y-8 lg:flex-row justify-center lg:space-x-12 lg:space-y-0 mt-5 min-h-[700px]">
      <aside class="-mx-4 lg:w-1/12">
        <LayoutSidebar/>
      </aside>
      <div class="flex-1 lg:max-w-5xl">
        <div class="space-y-6">
          <RouterView/>
        </div>
      </div>
    </div>
    <LayoutFooter :data="footers"/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import LayoutHeader from '@/views/layouts/common/components/LayoutHeader.vue'
import LayoutBreadcrumb from '@/views/layouts/common/components/LayoutBreadcrumb.vue'
import LayoutSidebar from '@/views/layouts/profile/components/LayoutSidebar.vue'
import LayoutFooter from '@/views/layouts/common/components/LayoutFooter.vue'
import { Separator } from '@/components/ui/separator'
import { FooterModel } from '@/views/layouts/common/components/model/footer.ts'

export default defineComponent({
  name: 'ProfileContainer',
  components: {
    Separator,
    LayoutSidebar, LayoutBreadcrumb, LayoutHeader, LayoutFooter
  },
  data()
  {
    return {
      footers: [] as Array<FooterModel>
    }
  },
  created()
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
})
</script>
