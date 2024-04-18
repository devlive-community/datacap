<template>
  <Card :title-class="'p-0'" :body-class="'p-0'">
    <template #title>
      <Select v-model="selectDatabase" :default-value="originalDatabase ? originalDatabase : selectDatabase" @update:modelValue="handlerChangeDatabase">
        <SelectTrigger class="border-0 w-[200px]">
          <SelectValue :placeholder="$t('source.tip.selectDatabase')"/>
        </SelectTrigger>
        <SelectContent class="w-full">
          <SelectGroup class="w-full">
            <SelectItem v-for="item in databaseArray" :value="item.code as any" :key="item.title as string" class="cursor-pointer">
              {{ item.title }}
            </SelectItem>
          </SelectGroup>
        </SelectContent>
      </Select>
    </template>
    <div class="h-[700px] overflow-x-auto overflow-y-auto">
      <CircularLoading v-if="loading" :show="loading"/>
      <div v-else>
        <Tree :data="dataTreeArray" :empty-text="$t('source.tip.selectDatabase')" :load-data="handlerLoadChildData" @on-select-change="handlerSelectNode"
              @on-contextmenu="handlerContextMenu">
          <template #contextMenu>
            <DropdownMenu>
              <DropdownMenuTrigger as-child>
                <span id="contextMenu"></span>
              </DropdownMenuTrigger>
              <DropdownMenuContent class="-mt-3">
                <DropdownMenuLabel>{{ $t('common.action') }}</DropdownMenuLabel>
                <DropdownMenuSeparator/>
                <DropdownMenuGroup>
                  <DropdownMenuSub>
                    <DropdownMenuSubTrigger class="cursor-pointer">{{ $t('source.common.menuNew') }}</DropdownMenuSubTrigger>
                    <DropdownMenuPortal>
                      <DropdownMenuSubContent>
                        <DropdownMenuItem v-if="dataInfo?.level === StructureEnum.TABLE" class="cursor-pointer" @click="handlerCreateTable(true)">
                          <Table :size="18" class="mr-2"/>
                          {{ $t('source.common.menuNewTable') }}
                        </DropdownMenuItem>
                        <DropdownMenuItem class="cursor-pointer" @click="handlerCreateColumn(true)">
                          <Columns :size="18" class="mr-2"/>
                          {{ $t('source.common.newColumn') }}
                        </DropdownMenuItem>
                      </DropdownMenuSubContent>
                    </DropdownMenuPortal>
                  </DropdownMenuSub>
                </DropdownMenuGroup>
                <DropdownMenuGroup v-if="dataInfo?.level === StructureEnum.TABLE">
                  <DropdownMenuSub>
                    <DropdownMenuSubTrigger class="cursor-pointer">{{ $t('source.common.menuExport') }}</DropdownMenuSubTrigger>
                    <DropdownMenuPortal>
                      <DropdownMenuSubContent>
                        <DropdownMenuItem v-if="dataInfo?.level === StructureEnum.TABLE" class="cursor-pointer" @click="handlerExportData(true)">
                          <ArrowUpFromLine :size="18" class="mr-2"/>
                          {{ $t('source.common.exportData') }}
                        </DropdownMenuItem>
                      </DropdownMenuSubContent>
                    </DropdownMenuPortal>
                  </DropdownMenuSub>
                </DropdownMenuGroup>
                <DropdownMenuSeparator/>
                <DropdownMenuItem v-if="dataInfo?.level === StructureEnum.TABLE" class="cursor-pointer" @click="handlerTruncateTable(true)">
                  <Trash :size="18" class="mr-2"/>
                  {{ $t('source.common.truncateTable') }}
                </DropdownMenuItem>
                <DropdownMenuItem v-if="dataInfo?.level === StructureEnum.TABLE" class="cursor-pointer" @click="handlerDropTable(true)">
                  <Delete :size="18" class="mr-2"/>
                  {{ $t('source.common.dropTable') }}
                </DropdownMenuItem>
                <DropdownMenuItem v-if="dataInfo?.level === StructureEnum.COLUMN" class="cursor-pointer" @click="handlerChangeColumn(true)">
                  <Pencil :size="18" class="mr-2"/>
                  {{ $t('source.common.changeColumn') }}
                </DropdownMenuItem>
                <DropdownMenuItem v-if="dataInfo?.level === StructureEnum.COLUMN" class="cursor-pointer" @click="handlerDropColumn(true)">
                  <Delete :size="18" class="mr-2"/>
                  {{ $t('source.common.dropColumn') }}
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          </template>
        </Tree>
      </div>
    </div>
  </Card>
  <TableCreate v-if="tableCreateVisible" :isVisible="tableCreateVisible" :info="dataInfo" @close="handlerCreateTable(false)"/>
  <TableExport v-if="tableExportVisible" :isVisible="tableExportVisible" :info="dataInfo" @close="handlerExportData(false)"/>
  <TableTruncate v-if="tableTruncateVisible" :isVisible="tableTruncateVisible" :info="dataInfo" @close="handlerTruncateTable(false)"/>
  <TableDrop v-if="tableDropVisible" :isVisible="tableDropVisible" :info="dataInfo" @close="handlerDropTable(false)"/>
  <ColumnCreate v-if="columnCreateVisible" :isVisible="columnCreateVisible" :info="dataInfo" @close="handlerCreateColumn(false)"/>
  <ColumnChange v-if="columnChangeVisible" :isVisible="columnChangeVisible" :info="dataInfo" @close="handlerChangeColumn(false)"/>
  <ColumnDrop v-if="columnDropVisible" :isVisible="columnDropVisible" :info="dataInfo" @close="handlerDropColumn(false)"/>
</template>

<script lang="ts">
import { defineComponent, resolveComponent } from 'vue'
import { ArrowUpFromLine, Columns, Delete, Pencil, Table, Trash } from 'lucide-vue-next'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import DatabaseService from '@/services/database.ts'
import { StructureEnum, StructureModel } from '@/model/structure.ts'
import { Tree } from 'view-ui-plus'
import '@/views/components/tree/style.css'
import Card from '@/views/ui/card'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '@/components/ui/select'
import TableService from '@/services/table.ts'
import ColumnService from '@/services/column.ts'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuPortal,
  DropdownMenuSeparator,
  DropdownMenuShortcut,
  DropdownMenuSub,
  DropdownMenuSubContent,
  DropdownMenuSubTrigger,
  DropdownMenuTrigger
} from '@/components/ui/dropdown-menu'
import ColumnCreate from '@/views/pages/admin/source/components/ColumnCreate.vue'
import ColumnDrop from '@/views/pages/admin/source/components/ColumnDrop.vue'
import TableExport from '@/views/pages/admin/source/components/TableExport.vue'
import ColumnChange from '@/views/pages/admin/source/components/ColumnChange.vue'
import TableTruncate from '@/views/pages/admin/source/components/TableTruncate.vue'
import TableDrop from '@/views/pages/admin/source/components/TableDrop.vue'
import TableCreate from '@/views/pages/admin/source/components/TableCreate.vue'
import { ToastUtils } from '@/utils/toast.ts'

export default defineComponent({
  name: 'MetadataSidebar',
  components: {
    TableCreate, TableDrop, TableTruncate, ColumnChange, TableExport, ColumnDrop, ColumnCreate,
    Card,
    Tree,
    CircularLoading,
    Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue,
    Columns, Pencil, ArrowUpFromLine, Delete, Trash, Table,
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuPortal,
    DropdownMenuSeparator,
    DropdownMenuShortcut,
    DropdownMenuSub,
    DropdownMenuSubContent,
    DropdownMenuSubTrigger,
    DropdownMenuTrigger
  },
  computed: {
    StructureEnum()
    {
      return StructureEnum
    }
  },
  data()
  {
    return {
      loading: false,
      selectDatabase: undefined,
      originalSource: null as string | null,
      originalDatabase: null as string | null,
      originalTable: null as string | null,
      selectNode: null as StructureModel | null,
      databaseArray: Array<StructureModel>(),
      dataTreeArray: Array<StructureModel>(),
      dataInfo: null as StructureModel | null,
      tableCreateVisible: false,
      tableExportVisible: false,
      tableTruncateVisible: false,
      tableDropVisible: false,
      columnCreateVisible: false,
      columnChangeVisible: false,
      columnDropVisible: false
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      const source = this.$route.params?.source as string
      const database = this.$route.params?.database as string
      if (source) {
        this.originalSource = source
        this.loading = true
        DatabaseService.getAllBySource(source)
                       .then(response => {
                         if (response.status) {
                           response.data
                                   .forEach((item: { name: null; catalog: null; code: undefined }) => {
                                     const structure: StructureModel = {
                                       title: item.name,
                                       catalog: item.catalog,
                                       code: item.code
                                     }
                                     this.databaseArray.push(structure)
                                   })
                           if (database) {
                             this.originalDatabase = database
                             this.selectDatabase = database as any
                             this.handlerChangeDatabase()
                           }
                         }
                         else {
                           ToastUtils.error(response.message)
                         }
                       })
                       .finally(() => this.loading = false)
      }
    },
    handlerChangeDatabase()
    {
      this.loading = true
      this.dataTreeArray = []
      TableService.getAllByDatabase(this.selectDatabase as any)
                  .then(response => {
                    if (response.status) {
                      response.data
                              .forEach((item: {
                                name: null;
                                title: null;
                                catalog: null;
                                code: undefined;
                                type: null;
                                engine: null;
                                comment: null;
                                database: { name: null, id: string };
                              }) => {
                                const structure: StructureModel = {
                                  title: item.name,
                                  database: item.database.name,
                                  databaseId: item.database.id,
                                  catalog: item.catalog,
                                  code: item.code,
                                  type: item.type,
                                  level: StructureEnum.TABLE,
                                  engine: item.engine,
                                  comment: item.comment,
                                  origin: item,
                                  loading: false,
                                  contextmenu: true,
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
                    else {
                      ToastUtils.error(response.message)
                    }
                  })
                  .finally(() => {
                    this.loading = false
                    const table = this.$route.params?.table as string
                    if (table) {
                      const node = this.dataTreeArray.find(item => item.code === table)
                      if (node) {
                        node.selected = true
                        this.handlerSelectNode([node])
                      }
                    }
                    else {
                      this.$router.push(`/admin/source/${ this.originalSource }/d/${ this.selectDatabase }`)
                    }
                  })
    },
    handlerSelectNode(node: Array<StructureModel>)
    {
      if (node.length === 0 && this.selectNode) {
        // Prevent selection clearing after repeated clicks
        this.selectNode.selected = true
        return
      }
      const currentNode = node[0]
      if (currentNode.level === StructureEnum.COLUMN) {
        if (this.selectNode) {
          this.selectNode.selected = true
        }
        currentNode.selected = false
        return
      }
      this.selectNode = currentNode
      const type = this.$route.meta.type as string
      this.$router.push(`/admin/source/${ this.originalSource }/d/${ this.selectDatabase }/t/${ type ? type : 'info' }/${ currentNode.code }`)
    },
    handlerLoadChildData(item: StructureModel, callback: any)
    {
      const dataChildArray = [] as StructureModel[]
      if (item.level === StructureEnum.COLUMN) {
        callback(dataChildArray)
        return
      }
      ColumnService.getAllByTable(item.code as string)
                   .then(response => {
                     if (response.status) {
                       response.data.forEach((item: {
                         name: null;
                         title: null;
                         catalog: null;
                         code: undefined;
                         type: null;
                         dataType: null;
                         extra: null;
                         engine: null;
                         isKey: null;
                         defaultValue: null;
                         table: { name: null, code: undefined, database: { name: null, id: string } };
                       }) => {
                         const structure: StructureModel = {
                           title: item.name,
                           database: item.table.database.name,
                           databaseId: item.table.database.id,
                           table: item.table.name,
                           tableId: item.table.code,
                           catalog: item.catalog,
                           code: item.code,
                           level: StructureEnum.COLUMN,
                           type: item.type,
                           extra: item.extra,
                           dataType: item.dataType,
                           engine: item.engine,
                           isKey: item.isKey,
                           defaultValue: item.defaultValue,
                           contextmenu: true,
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
    handlerContextMenu(node: StructureModel)
    {
      console.log(node)
      this.dataInfo = node
      // Simulate right-click to trigger right-click menu
      const element = document.getElementById('contextMenu') as HTMLElement
      if (element) {
        element.click()
      }
    },
    handlerCreateTable(opened: boolean)
    {
      this.tableCreateVisible = opened
    },
    handlerCreateColumn(opened: boolean)
    {
      this.columnCreateVisible = opened
    },
    handlerExportData(opened: boolean)
    {
      this.tableExportVisible = opened
    },
    handlerTruncateTable(opened: boolean)
    {
      this.tableTruncateVisible = opened
    },
    handlerDropTable(opened: boolean)
    {
      this.tableDropVisible = opened
    },
    handlerChangeColumn(opened: boolean)
    {
      this.columnChangeVisible = opened
    },
    handlerDropColumn(opened: boolean)
    {
      this.columnDropVisible = opened
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
