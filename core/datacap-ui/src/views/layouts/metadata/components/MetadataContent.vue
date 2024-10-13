<template>
  <Tabs v-model="selectTab as string" :default-value="selectTab as string" class="w-full">
    <DataCapCard>
      <template #title>
        <TabsList class="rounded-none">
          <TabsTrigger value="info" class="cursor-pointer" :disabled="!originalTable" @click="handlerChange">
            <div class="flex space-x-2">
              <Info :size="18"/>
              <span>{{ $t('source.common.info') }}</span>
            </div>
          </TabsTrigger>
          <TabsTrigger value="structure" class="cursor-pointer" :disabled="!originalTable" @click="handlerChange">
            <div class="flex space-x-2">
              <LayoutPanelTop :size="18"/>
              <span>{{ $t('source.common.structure') }}</span>
            </div>
          </TabsTrigger>
          <TabsTrigger value="data" class="cursor-pointer" :disabled="!originalTable" @click="handlerChange">
            <div class="flex space-x-2">
              <Table :size="18"/>
              <span>{{ $t('source.common.tableData') }}</span>
            </div>
          </TabsTrigger>
          <TabsTrigger value="statement" class="cursor-pointer" :disabled="!originalTable" @click="handlerChange">
            <div class="flex space-x-2">
              <SatelliteDish :size="18"/>
              <span>{{ $t('source.common.statement') }}</span>
            </div>
          </TabsTrigger>
          <TabsTrigger value="erDiagram" class="cursor-pointer" :disabled="!originalTable" @click="handlerChange">
            <div class="flex space-x-2">
              <Wind :size="18"/>
              <span>{{ $t('source.common.erDiagram') }}</span>
            </div>
          </TabsTrigger>
        </TabsList>
      </template>
      <template #content>
        <TabsContent :value="selectTab as string">
          <div class="h-[695px] overflow-x-auto overflow-y-auto">
            <RouterView/>
          </div>
        </TabsContent>
      </template>
    </DataCapCard>
  </Tabs>
</template>

<script lang="ts">
import { defineComponent, watch } from 'vue'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Info, LayoutPanelTop, SatelliteDish, Table, Wind } from 'lucide-vue-next'
import { DataCapCard } from '@/views/ui/card'

export default defineComponent({
  name: 'MetadataContent',
  components: {
    DataCapCard,
    Tabs, TabsContent, TabsList, TabsTrigger,
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
