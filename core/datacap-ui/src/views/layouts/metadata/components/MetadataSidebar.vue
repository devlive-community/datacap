<template>
  <a-card :bordered="false">
    <a-select v-model:value="selectDatabase" :loading="loading" :style="{ width: '100%' }" @change="onChangeDatabase">
      <a-select-option v-for="item in databaseArray" :key="item.code" :value="item.code">{{ item.title }}</a-select-option>
    </a-select>

    <div class="relative h-screen overflow-x-auto overflow-y-auto">
      <a-skeleton v-if="loading" active class="mt-2"/>

      <a-tree v-else-if="!loading && dataTreeArray.length > 0"
              v-model:selectedKeys="selectedKeys"
              :tree-data="dataTreeArray"
              :field-names="{ key: 'value', title: 'title', children: 'children' }"
              :load-data="onLoadData"
              @select="onSelect">
        <template #title="node">
          <div class="flex items-center space-x-1" @contextmenu.prevent="visibleContextMenu($event, node)">
            <TableOutlined v-if="node.level === StructureEnum.TYPE && node.type === 'table'" class="text-xs font-semibold text-gray-500" :style="{ fontSize: '16px' }"/>
            <EyeOutlined v-else-if="node.level === StructureEnum.TYPE && node.type === 'view'" class="text-xs font-semibold text-gray-500" :style="{ fontSize: '16px' }"/>
            <FunctionOutlined v-else-if="node.level === StructureEnum.TYPE && node.type === 'function'" class="text-xs font-semibold text-gray-500" :style="{ fontSize: '16px' }"/>
            <ApiOutlined v-else-if="node.level === StructureEnum.TYPE && node.type === 'procedure'" class="text-xs font-semibold text-gray-500" :style="{ fontSize: '16px' }"/>
            <InsertRowRightOutlined v-else-if="node.level === StructureEnum.TYPE && node.type === 'column'" class="text-xs font-semibold text-gray-500" :style="{ fontSize: '16px' }"/>
            <PartitionOutlined v-else-if="node.level === StructureEnum.TYPE && node.type === 'index'" class="text-xs font-semibold text-gray-500" :style="{ fontSize: '16px' }"/>
            <ThunderboltOutlined v-else-if="node.level === StructureEnum.TYPE && node.type === 'trigger'" class="text-xs font-semibold text-gray-500" :style="{ fontSize: '16px' }"/>
            <KeyOutlined v-else-if="node.level === StructureEnum.TYPE && node.type === 'primary'" class="text-xs font-semibold text-gray-500" :style="{ fontSize: '16px' }"/>

            <span class="text-sm font-normal text-gray-500">
              {{ node.title }}
            </span>

            <span v-if="node.level === StructureEnum.COLUMN" class="text-xs font-normal text-gray-400 ml-1">
              {{ node.typeName === 'column' ? node.type : node.definition }}
            </span>
          </div>
        </template>
      </a-tree>

      <div v-if="contextmenu.visible && dataInfo"
           class="dc-ctxmenu"
           :style="{ left: contextmenu.position.x + 'px', top: contextmenu.position.y + 'px' }"
           @click.stop>
        <a-menu mode="vertical" :selectable="false" style="min-width: 180px;">
          <a-sub-menu v-if="dataInfo.level === StructureEnum.TABLE || dataInfo.level === StructureEnum.COLUMN || dataInfo.type === 'table' || dataInfo.type === 'column'"
                      key="new"
                      :title="$t('source.common.menuNew')">
            <a-menu-item v-if="dataInfo.level === StructureEnum.TABLE || dataInfo.type === 'table'" key="new-table" @click="visibleCreateTable(true)">
              <div class="flex items-center space-x-1">
                <TableOutlined :style="{ fontSize: '16px' }"/>
                <span>{{ $t('source.common.menuNewTable') }}</span>
              </div>
            </a-menu-item>

            <a-menu-item v-if="dataInfo.level === StructureEnum.COLUMN || dataInfo.type === 'column' || dataInfo.level === StructureEnum.TABLE" key="new-column" @click="visibleCreateColumn(true)">
              <div class="flex items-center space-x-1">
                <InsertRowRightOutlined :style="{ fontSize: '16px' }"/>
                <span>{{ $t('source.common.newColumn') }}</span>
              </div>
            </a-menu-item>
          </a-sub-menu>

          <a-sub-menu v-if="dataInfo.level === StructureEnum.TABLE" key="export" :title="$t('source.common.menuExport')">
            <a-menu-item key="export-data" @click="visibleExportData(true)">
              <div class="flex items-center space-x-1">
                <VerticalAlignTopOutlined :style="{ fontSize: '16px' }"/>
                <span>{{ $t('source.common.exportData') }}</span>
              </div>
            </a-menu-item>
          </a-sub-menu>

          <a-menu-item v-if="dataInfo.level === StructureEnum.TABLE" key="truncate" @click="visibleTruncateTable(true)">
            <div class="flex items-center space-x-1">
              <DeleteOutlined :style="{ fontSize: '16px' }"/>
              <span>{{ $t('source.common.truncateTable') }}</span>
            </div>
          </a-menu-item>

          <a-menu-item v-if="dataInfo.level === StructureEnum.TABLE" key="drop-table" @click="visibleDropTable(true)">
            <div class="flex items-center space-x-1">
              <DeleteOutlined :style="{ fontSize: '16px' }"/>
              <span>{{ $t('source.common.dropTable') }}</span>
            </div>
          </a-menu-item>

          <a-menu-item v-if="dataInfo.level === StructureEnum.COLUMN" key="change-column" @click="visibleChangeColumn(true)">
            <div class="flex items-center space-x-1">
              <EditOutlined :style="{ fontSize: '16px' }"/>
              <span>{{ $t('source.common.changeColumn') }}</span>
            </div>
          </a-menu-item>

          <a-menu-item v-if="dataInfo.level === StructureEnum.COLUMN" key="drop-column" @click="visibleDropColumn(true)">
            <div class="flex items-center space-x-1">
              <DeleteOutlined :style="{ fontSize: '16px' }"/>
              <span>{{ $t('source.common.dropColumn') }}</span>
            </div>
          </a-menu-item>
        </a-menu>
      </div>
    </div>
  </a-card>

  <TableCreate v-if="tableCreateVisible" :is-visible="tableCreateVisible" @close="visibleCreateTable(false)"/>

  <ColumnCreate v-if="columnCreateVisible" :is-visible="columnCreateVisible" @close="visibleCreateColumn(false)"/>

  <TableExport v-if="tableExportVisible"
               :isVisible="tableExportVisible"
               :info="dataInfo as any"
               @close="visibleExportData(false)"/>

  <TableTruncate v-if="tableTruncateVisible" :is-visible="tableTruncateVisible" @close="visibleTruncateTable(false)"/>

  <TableDrop v-if="tableDropVisible" :is-visible="tableDropVisible" @close="visibleDropTable(false)"/>

  <ColumnChange v-if="columnChangeVisible && dataInfo"
                :is-visible="columnChangeVisible"
                :column="dataInfo.code"
                @close="visibleChangeColumn(false)">
  </ColumnChange>

  <ColumnDrop v-if="columnDropVisible && dataInfo"
              :is-visible="columnDropVisible"
              :column="dataInfo.code"
              @close="visibleDropColumn(false)">
  </ColumnDrop>
</template>

<script lang="ts" setup>
import { onMounted, onUnmounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import MetadataService from '@/services/metadata.ts'
import { StructureEnum, StructureModel } from '@/model/structure.ts'
import ColumnCreate from '@/views/pages/admin/source/components/ColumnCreate.vue'
import ColumnDrop from '@/views/pages/admin/source/components/ColumnDrop.vue'
import TableExport from '@/views/pages/admin/source/components/TableExport.vue'
import ColumnChange from '@/views/pages/admin/source/components/ColumnChange.vue'
import TableTruncate from '@/views/pages/admin/source/components/TableTruncate.vue'
import TableDrop from '@/views/pages/admin/source/components/TableDrop.vue'
import TableCreate from '@/views/pages/admin/source/components/TableCreate.vue'
import { ApiOutlined, DeleteOutlined, EditOutlined, EyeOutlined, FunctionOutlined, InsertRowRightOutlined, KeyOutlined, PartitionOutlined, TableOutlined, ThunderboltOutlined, VerticalAlignTopOutlined } from '@ant-design/icons-vue'

interface MenuItem
{
  type: string
  title: string
  comment?: string
  isLeaf?: boolean
  level?: StructureEnum
  code?: string
  children?: MenuItem[]
  value?: string
  dataType?: string
  nullable?: string
  defaultValue?: string
  position?: number
  definition?: string
  typeName?: string
  disabled?: boolean
}

interface SourceData
{
  type_name: string
  object_name: string
  object_comment: string
  object_data_type: string
  object_nullable: string
  object_default_value: string
  object_position: number
  object_definition: string
}

defineOptions({ name: 'MetadataSidebar' })

const route = useRoute()
const router = useRouter()
const { t } = useI18n()

const loading = ref(false)
const selectDatabase = ref<any>(undefined)
const selectedKeys = ref<any[]>([])
const originalSource = ref<string | null>(null)
const databaseArray = ref<StructureModel[]>([])
const dataTreeArray = ref<any[]>([])
const dataInfo = ref<StructureModel | null>(null)
const tableCreateVisible = ref(false)
const tableExportVisible = ref(false)
const tableTruncateVisible = ref(false)
const tableDropVisible = ref(false)
const columnCreateVisible = ref(false)
const columnChangeVisible = ref(false)
const columnDropVisible = ref(false)
const contextmenu = reactive({
  visible: false,
  position: { x: 0, y: 0 }
})

const convertToTreeData = (flatData: SourceData[], level: StructureEnum = StructureEnum.DATABASE): MenuItem[] => {
  const groupedData = flatData.reduce((acc, curr) => {
    if (!acc[curr.type_name]) {
      acc[curr.type_name] = []
    }
    acc[curr.type_name].push(curr)
    return acc
  }, {} as Record<string, SourceData[]>)

  return Object.entries(groupedData).map(([type, items]) => ({
    type,
    title: `${ t('common.' + type) } (${ items.length })`,
    level: StructureEnum.TYPE,
    value: `${ type }_${ new Date().getTime() }`,
    children: items.map(item => ({
      type: item.object_data_type || item.type_name || '',
      title: item.object_name,
      level: level,
      isLeaf: false,
      code: item.object_name,
      value: `${ item.object_name }_${ item.type_name }`,
      comment: item.object_comment,
      dataType: item.object_data_type,
      nullable: item.object_nullable,
      defaultValue: item.object_default_value,
      position: item.object_position,
      definition: item.object_definition,
      typeName: item.type_name
    }))
  }))
}

const updateTreeChildren = (list: any[], key: string, children: any[]): any[] =>
  list.map((node) => {
    if (node.value === key) {
      return { ...node, children }
    }
    if (node.children) {
      return { ...node, children: updateTreeChildren(node.children, key, children) }
    }
    return node
  })

const handlerSelectNode = (nodes: any[]) => {
  if (nodes && nodes.length > 0) {
    selectedKeys.value = [nodes[0].value]
  }
}

const onChangeDatabase = () => {
  loading.value = true
  dataTreeArray.value = []
  MetadataService.getTablesByDatabase(originalSource.value, selectDatabase.value as string)
                 .then((response) => {
                   if (response.status) {
                     dataTreeArray.value = [...convertToTreeData(response.data.columns, StructureEnum.TABLE)]
                   }
                   else {
                     message.error(response.message)
                   }
                 })
                 .finally(() => {
                   loading.value = false
                   const table = route.params?.table
                   if (table) {
                     const node = dataTreeArray.value.find((item) => item.code === table)
                     if (node) {
                       handlerSelectNode([node])
                     }
                   }
                   else {
                     router.push(`/admin/source/${ originalSource.value }/d/${ selectDatabase.value }`)
                   }
                 })
}

const handleInitialize = () => {
  const source = route.params?.source as string
  const database = route.params?.database as string
  if (source) {
    originalSource.value = source
    loading.value = true
    const table = route.params?.table
    if (table) {
      selectedKeys.value = [`${ table }_table`]
    }
    MetadataService.getDatabaseBySource(source)
                   .then((response) => {
                     if (response.status) {
                       response.data.columns.forEach((item: any) => {
                         const name = item.object_name || item.schema_name || item.SCHEMA_NAME
                         databaseArray.value.push({ title: name, catalog: name, code: name } as StructureModel)
                       })
                       if (database) {
                         selectDatabase.value = database as any
                         onChangeDatabase()
                       }
                     }
                     else {
                       message.error(response.message)
                     }
                   })
                   .finally(() => (loading.value = false))
  }
}

const onSelect = (_keys: any[], info: any) => {
  const node = info.node.dataRef as any
  if (node.level === StructureEnum.TYPE || node.level === StructureEnum.COLUMN) {
    return
  }

  const type = route.meta.type
  router.push(`/admin/source/${ originalSource.value }/d/${ selectDatabase.value }/t/${ type ? type : 'info' }/${ node.code }`)
}

const onLoadData = (treeNode: any): Promise<void> => {
  const item = treeNode.dataRef as any
  return new Promise<void>((resolve) => {
    if (item.children && item.children.length > 0 && item.level === StructureEnum.COLUMN) {
      resolve()
      return
    }
    if (item.level === StructureEnum.COLUMN) {
      resolve()
      return
    }

    MetadataService.getColumnsByTable(originalSource.value, selectDatabase.value as string, item.code as string)
                   .then((response) => {
                     if (response.status) {
                       const children = [...convertToTreeData(response.data.columns, StructureEnum.COLUMN)]
                       dataTreeArray.value = updateTreeChildren(dataTreeArray.value, item.value, children)
                     }
                     else {
                       message.error(response.message)
                     }
                   })
                   .finally(() => resolve())
  })
}

const visibleContextMenu = (event: any, node: any) => {
  contextmenu.position = { x: event.clientX, y: event.clientY }
  dataInfo.value = node
  contextmenu.visible = true
}

const closeContextMenu = () => {
  contextmenu.visible = false
}

const visibleCreateTable = (opened: boolean) => {
  tableCreateVisible.value = opened
  closeContextMenu()
  if (!opened) {
    onChangeDatabase()
  }
}

const visibleCreateColumn = (opened: boolean) => {
  columnCreateVisible.value = opened
  closeContextMenu()
}

const visibleExportData = (opened: boolean) => {
  tableExportVisible.value = opened
  closeContextMenu()
}

const visibleTruncateTable = (opened: boolean) => {
  tableTruncateVisible.value = opened
  closeContextMenu()
}

const visibleDropTable = (opened: boolean) => {
  tableDropVisible.value = opened
  closeContextMenu()
  if (!opened) {
    onChangeDatabase()
  }
}

const visibleChangeColumn = (opened: boolean) => {
  columnChangeVisible.value = opened
  closeContextMenu()
}

const visibleDropColumn = (opened: boolean) => {
  columnDropVisible.value = opened
  closeContextMenu()
  if (!opened) {
    onChangeDatabase()
  }
}

onMounted(() => {
  originalSource.value = route.params?.source as string
  handleInitialize()
  window.addEventListener('click', closeContextMenu)
})

onUnmounted(() => {
  window.removeEventListener('click', closeContextMenu)
})
</script>

<style scoped>
.dc-ctxmenu {
    position: fixed;
    z-index: 1050;
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
    border-radius: 8px;
    background: var(--dc-bg, #fff);
}
</style>
