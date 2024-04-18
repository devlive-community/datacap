<template>
  <div class="w-full h-full">
    <div class="hidden space-y-6 w-full md:block">
      <div class="flex flex-col space-y-8 lg:flex-row lg:space-x-6 lg:space-y-0">
        <aside class="-mx-4 w-[200px]">
          <MetadataSidebar/>
        </aside>
        <div class="flex-1">
          <MetadataContent/>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import MetadataSidebar from '@/views/layouts/metadata/components/MetadataSidebar.vue'
import MetadataContent from '@/views/layouts/metadata/components/MetadataContent.vue'

export default defineComponent({
  name: 'MetadataContainer',
  components: {
    MetadataContent,
    MetadataSidebar
  }
})
</script>
