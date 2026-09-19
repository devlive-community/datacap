<template>
  <div class="relative overflow-auto" style="height: 500px; max-height: 500px; max-width: 300px;">
    <a-skeleton v-if="loading" active class="mt-2"/>

    <a-alert v-else-if="data.length === 0 && errorMessage" class="mt-2" type="error" :message="errorMessage"/>

    <a-tree v-else-if="data.length > 0"
            v-model:selectedKeys="selectedKeys"
            :tree-data="data"
            :load-data="onLoadData"
            @select="onSelect">
      <template #title="node">
        <div class="flex items-center space-x-1">
          <component :is="node.level === 2 ? DatabaseOutlined :
                          node.level === 3 ? TableOutlined :
                          node.level === 4 ? InsertRowRightOutlined :
                          DatabaseOutlined
                     " class="text-xs font-semibold text-gray-500" :style="{ fontSize: '13px' }"/>
          <span class="text-sm font-normal text-gray-500">{{ node.title }}</span>
        </div>
      </template>
    </a-tree>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import { StructureEnum, StructureModel } from '@/model/structure'
import MetadataService from '@/services/metadata.ts'
import { ObjectUtils } from '@/utils/object'
import { DatabaseOutlined, InsertRowRightOutlined, TableOutlined } from '@ant-design/icons-vue'

defineOptions({ name: 'MetadataTree' })

const props = defineProps<{ code?: string }>()

const loading = ref(false)
const selectedKeys = ref<any[]>([])
const data = ref<StructureModel[]>([])
const errorMessage = ref<string | null>(null)

const updateTreeChildren = (list: any[], key: string, children: any[]): any[] =>
  list.map((node) => {
    if (node.key === key) {
      return { ...node, children }
    }
    if (node.children) {
      return { ...node, children: updateTreeChildren(node.children, key, children) }
    }
    return node
  })

const handleInitialize = () => {
  if (props.code) {
    data.value = []
    errorMessage.value = null
    loading.value = true
    MetadataService.getDatabaseBySource(props.code)
                   .then((response) => {
                     if (response.status && response.data && response.data.isSuccessful) {
                       response.data.columns.forEach((item: any) => {
                         const name = item.object_name || item.schema_name || item.SCHEMA_NAME
                         data.value.push({
                           key: name,
                           title: name,
                           catalog: name,
                           code: name,
                           level: StructureEnum.DATABASE,
                           value: name,
                           isLeaf: false
                         } as any)
                       })
                     }
                     else {
                       errorMessage.value = response.data.message
                     }
                   })
                   .finally(() => (loading.value = false))
  }
}

const onLoadData = (treeNode: any): Promise<void> => {
  const item = treeNode.dataRef as any
  return new Promise<void>((resolve) => {
    if (item.children) {
      resolve()
      return
    }

    const children = [] as any[]
    if (item.level === StructureEnum.DATABASE) {
      MetadataService.getTablesByDatabase(props.code, item.code)
                     .then((response) => {
                       if (response.status && response.data && response.data.isSuccessful) {
                         response.data.columns.forEach((value: any) => children.push({
                           key: `${ item.key }.${ value.object_name }`,
                           title: value.object_name,
                           value: value.object_name,
                           code: value.object_name,
                           database: item.code,
                           catalog: value.object_name,
                           level: StructureEnum.TABLE,
                           isLeaf: false
                         }))
                       }
                       else {
                         message.error(response.data.message)
                       }
                     })
                     .finally(() => {
                       data.value = updateTreeChildren(data.value, item.key, children)
                       resolve()
                     })
    }
    else if (item.level === StructureEnum.TABLE) {
      MetadataService.getColumnsByTable(props.code, item.database, item.code)
                     .then((response) => {
                       if (response.status && response.data && response.data.isSuccessful) {
                         response.data.columns
                                 .filter((value: any) => value.type_name === 'column')
                                 .forEach((value: any) => children.push({
                                   key: `${ item.key }.${ value.object_name }`,
                                   title: value.object_name,
                                   value: value.object_name,
                                   database: item.database,
                                   table: item.code,
                                   catalog: value.object_name,
                                   code: value.object_name,
                                   level: StructureEnum.COLUMN,
                                   isLeaf: true
                                 }))
                       }
                       else {
                         message.error(response.data.message)
                       }
                     })
                     .finally(() => {
                       data.value = updateTreeChildren(data.value, item.key, children)
                       resolve()
                     })
    }
    else {
      resolve()
    }
  })
}

const onSelect = (_keys: any[], info: any) => {
  const item = info.node.dataRef as StructureModel
  let text: string = item.title as string
  switch (item.level) {
    case StructureEnum.TABLE:
      text = item.database + '.' + text
      break
    case StructureEnum.COLUMN:
      text = item.database + '.' + item.table + '.' + text
      break
  }
  ObjectUtils.copy(text)
}

watch(() => props.code, () => handleInitialize())

handleInitialize()
</script>
