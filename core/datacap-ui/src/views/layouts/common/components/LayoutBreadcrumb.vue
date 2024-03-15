<template>
  <div class="flex-1 space-y-4 pl-8 pr-8 pt-3 pb-3">
    <Breadcrumb>
      <BreadcrumbList>
        <BreadcrumbItem>
          <BreadcrumbLink>
            <RouterLink to="/">{{ $t('common.home') }}</RouterLink>
          </BreadcrumbLink>
        </BreadcrumbItem>
        <BreadcrumbSeparator v-if="breadcrumbs.length > 0"/>
        <BreadcrumbItem v-if="breadcrumbs.length > 0" v-for="(item, index) in breadcrumbs">
          <BreadcrumbLink>
            <span v-if="item.meta.isRoot">{{ $t(`${item.meta.title}`) }}</span>
            <RouterLink v-else :to="item.path">{{ $t(`${item.meta.title}`) }}</RouterLink>
          </BreadcrumbLink>
          <BreadcrumbSeparator v-if="item.meta.isRoot && index < breadcrumbs.length - 1"/>
        </BreadcrumbItem>
      </BreadcrumbList>
    </Breadcrumb>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Breadcrumb, BreadcrumbItem, BreadcrumbLink, BreadcrumbList, BreadcrumbSeparator } from '@/components/ui/breadcrumb'

interface BreadcrumbModel
{
  path: string
  meta: {
    title: string
    isRoot: boolean
  }
}

export default defineComponent({
  name: 'LayoutBreadcrumb',
  components: {
    BreadcrumbSeparator, BreadcrumbLink, BreadcrumbItem, BreadcrumbList, Breadcrumb
  },
  watch: {
    $route()
    {
      this.getBreadcrumb()
    }
  },
  data()
  {
    return {
      breadcrumbs: [] as BreadcrumbModel[]
    }
  },
  created()
  {
    this.getBreadcrumb()
  },
  methods: {
    getBreadcrumb()
    {
      this.breadcrumbs = []
      this.$route['matched']
          .filter(route => route.meta.title)
          .forEach(item => {
            const breadcrumb: BreadcrumbModel = {
              path: item.path,
              meta: {
                title: item.meta.title as string,
                isRoot: item.meta.isRoot as boolean
              }
            }
            this.breadcrumbs?.push(breadcrumb)
          })
    }
  }
})
</script>
