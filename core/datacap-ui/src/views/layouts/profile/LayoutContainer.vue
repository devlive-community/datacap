<template>
  <div>
    <LayoutHeader/>
    <div class="hidden flex-col md:flex justify-center items-center mt-5">
      <div class="flex flex-1 flex-col space-y-8 lg:flex-row lg:space-x-12 lg:space-y-0">
        <aside class="w-full top-0 lg:w-1/5">
          <LayoutSidebar/>
        </aside>
        <div class="flex-1 lg:max-w-6xl">
          <div class="p-1 pr-4">
            <RouterView/>
          </div>
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
