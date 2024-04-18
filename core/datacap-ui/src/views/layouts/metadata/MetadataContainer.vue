<template>
  <div class="w-full h-full">
    <div class="hidden space-y-6 w-full md:block">
      <div class="flex flex-col space-y-8 lg:flex-row lg:space-x-6 lg:space-y-0">
        <aside class="-mx-4 w-[200px]">
          <MetadataSidebar :code="code"/>
        </aside>
        <div class="flex-1">
          <RouterView/>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import MetadataSidebar from '@/views/layouts/metadata/components/MetadataSidebar.vue'
import { useRouter } from 'vue-router'
import DatabaseService from '@/services/database.ts'
import { StructureModel } from '@/model/structure.ts'

export default defineComponent({
  name: 'MetadataContainer',
  components: {
    MetadataSidebar
  },
  data()
  {
    return {
      code: null as string | null
    }
  },
  created()
  {
    this.code = this.$route.params?.code as string
  }
})
</script>
