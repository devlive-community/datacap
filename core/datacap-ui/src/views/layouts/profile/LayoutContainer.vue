<template>
  <div>
    <LayoutHeader/>
    <div class="flex flex-col space-y-8 lg:flex-row justify-center lg:space-x-12 lg:space-y-0 mt-5">
      <aside class="-mx-4 lg:w-1/12">
        <LayoutSidebar/>
      </aside>
      <div class="flex-1 lg:max-w-5xl">
        <div class="space-y-6">
          <RouterView/>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import LayoutHeader from '@/views/layouts/common/components/LayoutHeader.vue'
import LayoutBreadcrumb from '@/views/layouts/common/components/LayoutBreadcrumb.vue'
import LayoutSidebar from '@/views/layouts/profile/components/LayoutSidebar.vue'
import { Separator } from '@/components/ui/separator'

export default defineComponent({
  name: 'ProfileContainer',
  components: {
    Separator,
    LayoutSidebar, LayoutBreadcrumb, LayoutHeader
  }
});
</script>
