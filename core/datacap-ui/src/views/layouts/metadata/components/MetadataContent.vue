<template>
  <Tabs v-model="selectTab" :default-value="selectTab" class="w-full">
    <Card :title-class="'p-0'" :body-class="'p-0'">
      <template #title>
        <TabsList>
          <TabsTrigger value="info" class="cursor-pointer">
            <Info :size="18" class="mr-2"/>
            {{ $t('source.common.info') }}
          </TabsTrigger>
          <TabsTrigger value="structure" class="cursor-pointer">
            <LayoutPanelTop :size="18" class="mr-2"/>
            {{ $t('source.common.structure') }}
          </TabsTrigger>
          <TabsTrigger value="data" class="cursor-pointer">
            <Table :size="18" class="mr-2"/>
            {{ $t('source.common.tableData') }}
          </TabsTrigger>
          <TabsTrigger value="statement" class="cursor-pointer">
            <SatelliteDish :size="18" class="mr-2"/>
            {{ $t('source.common.statement') }}
          </TabsTrigger>
          <TabsTrigger value="erDiagram" class="cursor-pointer">
            <Wind :size="18" class="mr-2"/>
            {{ $t('source.common.erDiagram') }}
          </TabsTrigger>
        </TabsList>
      </template>
      <TabsContent :value="selectTab">
        <div class="h-[695px]">
          <RouterView/>
        </div>
      </TabsContent>
    </Card>
  </Tabs>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Card from '@/views/ui/card'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Info, LayoutPanelTop, SatelliteDish, Table, Wind } from 'lucide-vue-next'

export default defineComponent({
  name: 'MetadataContent',
  components: {
    Tabs, TabsContent, TabsList, TabsTrigger,
    Card,
    Info, LayoutPanelTop, Table, SatelliteDish, Wind
  },
  data()
  {
    return {
      selectTab: 'info'
    }
  }
})
</script>
