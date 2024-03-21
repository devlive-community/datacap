<template>
  <div style="max-height: 500px; max-width: 200px; overflow: auto;">
    <CircularLoading v-if="loading" :show="loading"/>
    <Tree v-else :data="dataTreeArray" :load-data="handlerLoadChildData" @on-select-change="handlerSelectNode">
    </Tree>
  </div>
</template>
<script lang="ts">
import { defineComponent, resolveComponent, watch } from 'vue'
import { StructureEnum, StructureModel } from '@/model/structure'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import DatabaseService from '@/services/database'
import TableService from '@/services/table'
import ColumnService from '@/services/column'
import { Tree } from 'view-ui-plus'
import './style.css'
import { ObjectUtils } from '@/utils/object'

export default defineComponent({
  name: 'DataStructureLazyTree',
  components: {
    CircularLoading,
    Tree
  },
  props: {
    id: {
      type: String,
    }
  },
  data()
  {
    return {
      loading: false,
      dataTreeArray: Array<StructureModel>()
    }
  },
  created()
  {
    this.handlerInitialize()
    watch(() => this.id, () => this.handlerInitialize())
  },
  methods: {
    handlerInitialize()
    {
      this.dataTreeArray = []
      this.loading = true
      DatabaseService.getAllBySource(this.id as string)
          .then(response => {
            if (response.status) {
              response.data.forEach((item: { name: null; catalog: null; id: null }) => {
                const structure: StructureModel = {
                  title: item.name,
                  catalog: item.catalog,
                  applyId: item.id,
                  level: StructureEnum.DATABASE,
                  loading: false,
                  children: [] as StructureModel[],
                  render: (h: any, {data}: { data: StructureModel }) => {
                    return h('div', [
                      h('span', [
                        h(resolveComponent('FontAwesomeIcon'), {
                          icon: 'database',
                          style: {marginRight: '6px'}
                        }),
                        h('span', data.title)
                      ])
                    ])
                  }
                }
                this.dataTreeArray.push(structure)
              })
            }
          })
          .finally(() => this.loading = false)
    },
    handlerLoadChildData(item: StructureModel, callback: any)
    {
      const dataChildArray = [] as StructureModel[]
      if (item.level === StructureEnum.DATABASE) {
        TableService.getAllByDatabase(item.applyId as number)
            .then(response => {
              if (response.status) {
                response.data.forEach((item: { name: null; title: null; catalog: null; id: null; type: null; engine: null; database: { name: null }; }) => {
                  const structure: StructureModel = {
                    title: item.name,
                    database: item.database.name,
                    catalog: item.catalog,
                    applyId: item.id,
                    level: StructureEnum.TABLE,
                    type: item.type,
                    engine: item.engine,
                    loading: false,
                    children: [] as StructureModel[],
                    render: (h: any, {data}: { data: StructureModel }) => {
                      return h('div', [
                        h('span', [
                          h(resolveComponent('FontAwesomeIcon'), {
                            icon: 'table',
                            style: {marginRight: '6px'}
                          }),
                          h('span', data.title)
                        ])
                      ])
                    }
                  }
                  dataChildArray.push(structure)
                })
              }
            })
            .finally(() => callback(dataChildArray))
      }
      else if (item.level === StructureEnum.TABLE) {
        ColumnService.getAllByTable(item.applyId as number)
            .then(response => {
              if (response.status) {
                response.data.forEach((item: { name: null; title: null; catalog: null; id: null; type: null; engine: null; table: { name: null, database: { name: null } }; }) => {
                  const structure: StructureModel = {
                    title: item.name,
                    database: item.table.database.name,
                    table: item.table.name,
                    catalog: item.catalog,
                    applyId: item.id,
                    level: StructureEnum.COLUMN,
                    type: item.type,
                    engine: item.engine,
                    render: (h: any, {data}: { data: StructureModel }) => {
                      return h('div', [
                        h('span', [
                          h(resolveComponent('FontAwesomeIcon'), {
                            icon: 'columns',
                            style: {marginRight: '6px'}
                          }),
                          h('span', data.title)
                        ])
                      ])
                    }
                  }
                  dataChildArray.push(structure)
                })
              }
            })
            .finally(() => callback(dataChildArray))
      }
      else {
        callback(dataChildArray)
      }
    },
    handlerSelectNode(item: StructureModel[])
    {
      const target = item[0]
      let text: string = target.title as string
      switch (target.level) {
        case StructureEnum.TABLE:
          text = target.database + '.' + text;
          break
        case StructureEnum.COLUMN:
          text = target.database + '.' + target.table + '.' + text
          break
      }
      ObjectUtils.copy(text)
    }
  }
});
</script>
