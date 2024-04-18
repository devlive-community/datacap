<template>
  <Tabs v-model="selectTab as string" :default-value="selectTab as string" class="w-full">
    <Card :title-class="'p-0'" :body-class="'p-0'">
      <template #title>
        <TabsList>
          <TabsTrigger value="info" class="cursor-pointer" @click="handlerChange">
            <div class="flex space-x-2">
              <Info :size="18"/>
              <span>{{ $t('source.common.info') }}</span>
            </div>
          </TabsTrigger>
          <TabsTrigger value="structure" class="cursor-pointer" @click="handlerChange">
            <div class="flex space-x-2">
              <LayoutPanelTop :size="18"/>
              <span>{{ $t('source.common.structure') }}</span>
            </div>
          </TabsTrigger>
          <TabsTrigger value="data" class="cursor-pointer" @click="handlerChange">
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
      <TabsContent :value="selectTab as string">
        <div class="h-[695px] overflow-x-auto overflow-y-auto">
          <RouterView/>
        </div>
      </TabsContent>
    </Card>
  </Tabs>
</template>

<script lang="ts">
import { defineComponent, watch } from 'vue'
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
      selectTab: null as string | null,
      originalSource: null as string | null,
      originalDatabase: null as string | null,
      originalTable: null as string | null
    }
  },
  created()
  {
    this.handlerInitialize()
    this.watchChange()
  },
  methods: {
    handlerInitialize()
    {
      const source = this.$route.params?.source as string
      const database = this.$route.params?.database as string
      const table = this.$route.params?.table as string
      const type = this.$route.meta.type as string
      this.originalSource = source
      this.originalDatabase = database
      this.originalTable = table
      this.selectTab = type
    },
    handlerChange()
    {
      this.$router.push(`/admin/source/${ this.originalSource }/d/${ this.originalDatabase }/t/${ this.selectTab }/${ this.originalTable }`)
    },
    watchChange()
    {
      watch(
          () => this.$route?.params.table,
          () => this.handlerInitialize()
      )
    }
  }
})
</script>
