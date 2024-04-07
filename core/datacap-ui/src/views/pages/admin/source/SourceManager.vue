<template>
  <div class="w-full">
    <CircularLoading v-if="loading" :show="loading"/>
    <div v-else class="hidden space-y-6 pb-16 w-full md:block">
      <div class="flex flex-col space-y-8 lg:flex-row lg:space-x-6 lg:space-y-0">
        <aside class="-mx-4 w-[200px]">
          <Card :title-class="'p-0'" :body-class="'p-0'">
            <template #title>
              <Select v-model="applyValue.database as any" @update:modelValue="handlerChangeDatabase">
                <SelectTrigger class="border-0 w-[200px]">
                  <SelectValue :placeholder="$t('source.tip.selectDatabase')"/>
                </SelectTrigger>
                <SelectContent class="w-full">
                  <SelectGroup class="w-full">
                    <SelectItem v-for="item in databaseArray" :value="item.applyId as any" :key="item.title as string" class="cursor-pointer">
                      {{ item.title }}
                    </SelectItem>
                  </SelectGroup>
                </SelectContent>
              </Select>
            </template>
            <div class="h-[500px] overflow-x-auto overflow-y-auto">
              <CircularLoading v-if="dataTreeLoading" :show="dataTreeLoading"/>
              <div v-else>
                <Alert v-if="dataTreeArray && dataTreeArray.length === 0" :title="$t('source.tip.selectDatabase')"/>
                <Tree v-else :data="dataTreeArray" :load-data="handlerLoadChildData" @on-select-change="handlerSelectNode">
                </Tree>
              </div>
            </div>
          </Card>
        </aside>
        <div class="flex-1">
          <Card v-if="!applyValue.node" :body-class="'p-0'" :hidden-title="true">
            <Alert :description="$t('source.tip.notSelectedNode')"/>
          </Card>
          <Tabs v-else v-model="applyValue.tabType" :default-value="applyValue.tabType" class="w-full">
            <Card :title-class="'p-1'">
              <template #title>
                <TabsList>
                  <TabsTrigger value="info">
                    <Info :size="18" class="mr-2"/>
                    {{ $t('source.common.info') }}
                  </TabsTrigger>
                </TabsList>
              </template>
              <TabsContent value="info">
                <TableInfo v-if="applyValue.node" :id="applyValue.node.applyId as number"/>
              </TabsContent>
            </Card>
          </Tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, resolveComponent } from 'vue'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { StructureEnum, StructureModel } from '@/model/structure'
import { useRouter } from 'vue-router'
import DatabaseService from '@/services/database'
import Card from '@/views/ui/card'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '@/components/ui/select'
import TableService from '@/services/table'
import { Tree } from 'view-ui-plus'
import '@/views/components/tree/style.css'
import ColumnService from '@/services/column'
import Alert from '@/views/ui/alert'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Info } from 'lucide-vue-next'
import TableInfo from '@/views/pages/admin/source/components/TableInfo.vue'

export default defineComponent({
  name: 'SourceManager',
  components: {
    TableInfo,
    Alert,
    Card,
    CircularLoading,
    Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue,
    Tree,
    Tabs, TabsContent, TabsList, TabsTrigger,
    Info
  },
  data()
  {
    return {
      loading: false,
      databaseArray: Array<StructureModel>(),
      applyValue: {
        database: null as number | null,
        node: null as StructureModel | null,
        tabType: 'info'
      },
      dataTreeLoading: false,
      dataTreeArray: Array<StructureModel>()
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      const router = useRouter()
      const id = router.currentRoute?.value?.params['id']
      DatabaseService.getAllBySource(id as unknown as string)
                     .then(response => {
                       if (response.status) {
                         response.data.forEach((item: { name: null; catalog: null; id: null }) => {
                           const structure: StructureModel = {
                             title: item.name,
                             catalog: item.catalog,
                             applyId: item.id
                           }
                           this.databaseArray.push(structure)
                         })
                       }
                     })
                     .finally(() => this.loading = false)
    },
    handlerChangeDatabase()
    {
      this.dataTreeLoading = true
      this.dataTreeArray = []
      TableService.getAllByDatabase(this.applyValue.database as number)
                  .then(response => {
                    if (response.status) {
                      response.data.forEach((item: {
                        name: null;
                        title: null;
                        catalog: null;
                        id: null;
                        type: null;
                        engine: null;
                        comment: null;
                        database: { name: null, id: null };
                      }) => {
                        const structure: StructureModel = {
                          title: item.name,
                          database: item.database.name,
                          databaseId: item.database.id,
                          catalog: item.catalog,
                          applyId: item.id,
                          type: item.type,
                          level: StructureEnum.TABLE,
                          engine: item.engine,
                          comment: item.comment,
                          origin: item,
                          loading: false,
                          children: [] as StructureModel[],
                          render: (h: any, { data }: { data: StructureModel }) => {
                            return h('div', [
                              h('span', [
                                h(resolveComponent('FontAwesomeIcon'), {
                                  icon: 'table',
                                  style: { marginRight: '6px' }
                                }),
                                this.resolveTableComponent(h, data)
                              ])
                            ])
                          }
                        }
                        this.dataTreeArray.push(structure)
                      })
                    }
                  })
                  .finally(() => this.dataTreeLoading = false)
    },
    handlerSelectNode(node: Array<StructureModel>)
    {
      if (node.length === 0 && this.applyValue.node) {
        // Prevent selection clearing after repeated clicks
        this.applyValue.node.selected = true
        return
      }
      const currentNode = node[0]
      if (currentNode.level === StructureEnum.COLUMN) {
        if (this.applyValue.node) {
          this.applyValue.node.selected = true
        }
        currentNode.selected = false
        return
      }
      this.applyValue.node = currentNode
    },
    handlerLoadChildData(item: StructureModel, callback: any)
    {
      const dataChildArray = [] as StructureModel[]
      if (item.level === StructureEnum.COLUMN) {
        callback(dataChildArray)
        return
      }
      ColumnService.getAllByTable(item.applyId as number)
                   .then(response => {
                     if (response.status) {
                       response.data.forEach((item: {
                         name: null;
                         title: null;
                         catalog: null;
                         id: null;
                         type: null;
                         dataType: null;
                         extra: null;
                         engine: null;
                         isKey: null;
                         defaultValue: null;
                         table: { name: null, id: null, database: { name: null, id: null } };
                       }) => {
                         const structure: StructureModel = {
                           title: item.name,
                           database: item.table.database.name,
                           databaseId: item.table.database.id,
                           table: item.table.name,
                           tableId: item.table.id,
                           catalog: item.catalog,
                           applyId: item.id,
                           level: StructureEnum.COLUMN,
                           type: item.type,
                           extra: item.extra,
                           dataType: item.dataType,
                           engine: item.engine,
                           isKey: item.isKey,
                           defaultValue: item.defaultValue,
                           render: (h: any, { data }: { data: StructureModel }) => {
                             return h('div', [
                               h('span', [
                                 h(resolveComponent('FontAwesomeIcon'), {
                                   icon: this.getColumnIcon(data.isKey as unknown as string),
                                   style: { marginRight: '6px' }
                                 }),
                                 h('span', data.title),
                                 h('span', {
                                       style: {
                                         marginLeft: '6px',
                                         color: '#c5c8ce'
                                       }
                                     },
                                     this.getColumnTitle(data.type as unknown as string,
                                         data.extra as unknown as string,
                                         data.isKey as unknown as string,
                                         data.defaultValue as unknown as string))
                               ])
                             ])
                           }
                         }
                         dataChildArray.push(structure)
                       })
                     }
                   })
                   .finally(() => callback(dataChildArray))
    },
    getColumnIcon(type: string)
    {
      if (type === 'PRI') {
        return 'key'
      }
      else if (type === 'MUL') {
        return 'repeat'
      }
      else if (type === 'UNI') {
        return 'circle'
      }
      else {
        return 'columns'
      }
    },
    getColumnTitle(dataType: string, extra: string, isKey: string, defaultValue: string)
    {
      let title = dataType
      if (isKey === 'PRI') {
        if (extra) {
          title = `${ title } (${ extra.replace('_', ' ') })`
        }
        else {
          title = `${ title }`
        }
      }
      if (defaultValue && defaultValue !== 'null') {
        title = `${ title } = ${ defaultValue }`
      }
      return title
    },
    resolveTableComponent(h: any, data: StructureModel)
    {
      if (data.comment) {
        return h(resolveComponent('Tooltip'), {
              content: data.comment,
              placement: 'bottom-start',
              delay: 1000
            },
            h('span', data.title))
      }
      else {
        return h('span', data.title)
      }
    }
  }
})
</script>
