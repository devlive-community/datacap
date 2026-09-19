<template>
  <BaseLayout>
    <a-layout>
      <LayoutHeader/>
      <div class="container min-h-screen" style="margin-top: 20px; margin-bottom: 24px;">
        <a-layout has-sider :gutter="16">
          <LayoutSidebar/>
          <a-layout-content class="ml-6 min-w-0">
            <LayoutBreadcrumb style="margin-bottom: 16px;"/>
            <RouterView/>
          </a-layout-content>
        </a-layout>
      </div>
      <LayoutFooter/>
    </a-layout>
  </BaseLayout>
</template>

<script setup lang="ts">
import LayoutHeader from '@/views/layouts/common/components/LayoutHeader.vue'
import LayoutSidebar from '@/views/layouts/profile/components/LayoutSidebar.vue'
import LayoutFooter from '@/views/layouts/common/components/LayoutFooter.vue'
import LayoutBreadcrumb from '@/views/layouts/common/components/LayoutBreadcrumb.vue'
import BaseLayout from '@/views/layouts/base/BaseLayout.vue'
</script>
