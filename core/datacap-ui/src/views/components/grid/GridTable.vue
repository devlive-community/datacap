<template>
  <a-card>
    <template #title>
      <a-space>
        <a-button>
          <router-link :to="`/admin/dataset/info/source/${configure.code}?tempId=${tempId}`" target="_blank">
              <span class="flex items-center">
                <ShadcnIcon icon="Plus" :size="15"/>
                <span>{{ $t('common.dataset') }}</span>
              </span>
          </router-link>
        </a-button>

        <a-button @click="visualVisible = true">
          <ShadcnIcon icon="BarChart" :size="15"/>
          <span>{{ $t('dataset.common.visual') }}</span>
        </a-button>

        <a-tooltip class="mt-1" :title="$t('query.tip.pageShow')">
          <a-switch v-model:checked="isPage" @change="onChange"/>
        </a-tooltip>

        <a-tooltip :title="$t('query.tip.smallTips')">
          <a-button shape="circle">
            <template #icon>
              <ShadcnIcon icon="CircleHelp" :size="15"/>
            </template>
          </a-button>
        </a-tooltip>
      </a-space>
    </template>

    <ag-grid-vue v-if="type === 'table'"
                 class="ag-theme-datacap"
                 :key="timestamp"
                 :style="{width: configure.width + 'px', height: configure.height + 'px', 'margin-top': '2px'}"
                 :pagination="isPage"
                 :columnDefs="columnDefs"
                 :rowData="configure.columns"
                 :gridOptions="gridOptions as any"/>
  </a-card>

  <GridVisual :is-visible="visualVisible" :configure="configure" @close="visualVisible = $event"/>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { AgGridVue } from 'ag-grid-vue3'
import 'ag-grid-community/styles/ag-grid.css'
import './ag-theme-datacap.css'
import { useI18n } from 'vue-i18n'
import { GridConfigure } from '@/views/components/grid/GridConfigure'
import GridOptions from '@/views/components/grid/GridOptions'
import { GridColumn } from '@/views/components/grid/GridColumn'
import { ObjectUtils } from '@/utils/object'
import GridVisual from '@/views/components/grid/GridVisual.vue'

defineOptions({ name: 'GridTable' })

const props = withDefaults(defineProps<{ configure?: GridConfigure }>(), {
  configure: () => null as unknown as GridConfigure
})

const gridOptions = GridOptions.createDefaultOptions(useI18n())
const timestamp = ref(ObjectUtils.getTimestamp())

const columnDefs = ref<GridColumn[]>([])
const isPage = ref(true)
const type = ref('table')
const visualVisible = ref(false)
const tempId = ref<string | null>(null)

const handleInitialize = () => {
  if (props.configure) {
    const id = Math.random().toString(36).substring(2)
    tempId.value = id
    localStorage.setItem(`QueryContent_${ id }`, props.configure.query)
    props.configure.headers!.forEach((header: string) => {
      const columnDef: GridColumn = { headerName: header, field: header }
      columnDefs.value.push(columnDef)
    })
  }
}

const onChange = (value: boolean) => {
  timestamp.value = ObjectUtils.getTimestamp()
  isPage.value = value
}

handleInitialize()
</script>
