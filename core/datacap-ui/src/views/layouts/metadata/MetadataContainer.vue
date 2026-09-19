<template>
  <BaseLayout>
    <a-layout>
      <div class="container my-2 min-h-screen">
        <a-layout has-sider>
          <a-layout-sider class="mr-4" theme="light" :width="250">
            <MetadataSidebar/>
          </a-layout-sider>
          <a-layout-content class="ml-2">
            <MetadataContent/>
          </a-layout-content>
        </a-layout>
      </div>
    </a-layout>
  </BaseLayout>
</template>

<script setup lang="ts">
import MetadataSidebar from '@/views/layouts/metadata/components/MetadataSidebar.vue'
import MetadataContent from '@/views/layouts/metadata/components/MetadataContent.vue'
import BaseLayout from '@/views/layouts/base/BaseLayout.vue'
</script>
