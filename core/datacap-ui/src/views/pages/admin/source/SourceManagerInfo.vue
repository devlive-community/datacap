<template>
  <div class="w-full">
    <CircularLoading v-if="loading" :show="loading"/>
    <div v-else class="hidden space-y-6 w-full md:block">
      <div class="flex flex-col space-y-8 lg:flex-row lg:space-x-6 lg:space-y-0">
        <div class="flex-1">
          <Card v-if="!applyValue.node" :body-class="'p-8'" :hidden-title="true">
            <Alert :description="$t('source.tip.notSelectedNode')"/>
          </Card>
          <Tabs v-else v-model="applyValue.tabType" :default-value="applyValue.tabType" class="w-full">
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
              <TabsContent value="info" class="p-3">
                <TableInfo v-if="applyValue.node" :info="applyValue.node"/>
              </TabsContent>
              <TabsContent value="structure">
                <TableStructure v-if="applyValue.node" :info="applyValue.node"/>
              </TabsContent>
              <TabsContent value="data" class="mt-0">
                <TableData v-if="applyValue.node" :info="applyValue.node"/>
              </TabsContent>
              <TabsContent value="statement">
                <TableStatement v-if="applyValue.node" :info="applyValue.node"/>
              </TabsContent>
              <TabsContent value="erDiagram">
                <TableErDiagram v-if="applyValue.node" :info="applyValue.node"/>
              </TabsContent>
            </Card>
          </Tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import Card from '@/views/ui/card'
import Alert from '@/views/ui/alert'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { ArrowUpFromLine, Columns, Delete, Info, LayoutPanelTop, Pencil, SatelliteDish, Table, Trash, Wind } from 'lucide-vue-next'
import TableInfo from '@/views/pages/admin/source/components/TableInfo.vue'
import TableCreate from '@/views/pages/admin/source/components/TableCreate.vue'
import ColumnCreate from '@/views/pages/admin/source/components/ColumnCreate.vue'
import TableExport from '@/views/pages/admin/source/components/TableExport.vue'
import TableTruncate from '@/views/pages/admin/source/components/TableTruncate.vue'
import TableDrop from '@/views/pages/admin/source/components/TableDrop.vue'
import TableStructure from '@/views/pages/admin/source/components/TableStructure.vue'
import ColumnChange from '@/views/pages/admin/source/components/ColumnChange.vue'
import ColumnDrop from '@/views/pages/admin/source/components/ColumnDrop.vue'
import TableData from '@/views/pages/admin/source/components/TableData.vue'
import TableStatement from '@/views/pages/admin/source/components/TableStatement.vue'
import TableErDiagram from '@/views/pages/admin/source/components/TableErDiagram.vue'

export default defineComponent({
  name: 'SourceManagerInfo',
  components: {
    TableErDiagram,
    TableStatement,
    TableData,
    ColumnDrop,
    ColumnChange,
    TableStructure,
    TableDrop,
    TableTruncate,
    TableExport,
    ColumnCreate,
    TableCreate,
    TableInfo,
    Alert,
    Card,
    CircularLoading,
    Tabs, TabsContent, TabsList, TabsTrigger,
    Info, Table, Columns, ArrowUpFromLine, Trash, Delete, LayoutPanelTop, Pencil, SatelliteDish, Wind
  },

  data()
  {
    return {
      loading: false,
      applyValue: {
        database: undefined,
        tabType: 'info'
      },
      dataTreeLoading: false
    }
  },
  methods: {}
})
</script>
