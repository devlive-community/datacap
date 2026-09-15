<template>
  <BaseLayout>
    <a-layout>
      <LayoutHeader/>
      <div class="container my-2 min-h-screen">
        <a-layout has-sider>
          <LayoutSidebar/>
          <a-layout-content class="ml-20">
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
import BaseLayout from '@/views/layouts/base/BaseLayout.vue'
</script>
