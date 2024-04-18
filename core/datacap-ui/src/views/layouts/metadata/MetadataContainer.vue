<template>
  <div class="w-full h-full">
    <div class="hidden space-y-6 w-full md:block">
      <div class="flex flex-col space-y-8 lg:flex-row lg:space-x-6 lg:space-y-0">
        <aside class="-mx-4 w-[200px]">
          <MetadataSidebar :code="code" @change="handlerChange"/>
        </aside>
        <div class="flex-1">
          <Card v-if="!dataInfo" :body-class="'p-8'" :hidden-title="true">
            <Alert :description="$t('source.tip.notSelectedNode')"/>
          </Card>
          <MetadataContent v-else/>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import MetadataSidebar from '@/views/layouts/metadata/components/MetadataSidebar.vue'
import MetadataContent from '@/views/layouts/metadata/components/MetadataContent.vue'
import { StructureModel } from '@/model/structure.ts'
import router from '@/router'
import Card from '@/views/ui/card'
import Alert from '@/views/ui/alert'

export default defineComponent({
  name: 'MetadataContainer',
  components: {
    MetadataContent,
    MetadataSidebar,
    Card,
    Alert
  },
  data()
  {
    return {
      code: null as string | null,
      dataInfo: null as StructureModel | null
    }
  },
  created()
  {
    this.code = this.$route.params?.source as string
  },
  methods: {
    handlerChange(node: StructureModel)
    {
      this.dataInfo = node
      router.push(`/admin/source/manager/${ this.code }/info/${ node.code }`)
    }
  }
})
</script>
