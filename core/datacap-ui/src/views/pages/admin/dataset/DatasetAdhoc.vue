<template>
  <div class="hidden space-y-6 pb-16 w-full h-full md:block">
    <div class="flex flex-col space-y-8 lg:flex-row lg:space-x-12 lg:space-y-0">
      <aside class="-mx-4 w-[200px] space-y-5">
        <DataCapCard>
          <template #title>{{ $t('dataset.common.columnModeMetric') }}</template>
          <CircularLoading v-if="initialize" :show="initialize"/>
          <div v-else>
            <Draggable item-key="id" :group="{ name: 'metrics', pull: 'clone', put: false }" :list="originalMetrics" :clone="handlerClone"
                       @start="handlerHighlight(true, ColumnType.METRIC)" @end="handlerHighlight(false, ColumnType.METRIC)">
              <template #item="{ element }">
                <Badge variant="outline" class="cursor-pointer mr-1" @dblclick="handlerClone(element)">
                  {{ element.aliasName ? element.aliasName : element.name }}
                </Badge>
              </template>
            </Draggable>
          </div>
        </DataCapCard>
        <DataCapCard>
          <template #title>{{ $t('dataset.common.columnModeDimension') }}</template>
          <CircularLoading v-if="initialize" :show="initialize"/>
          <div v-else>
            <Draggable item-key="id" :group="{ name: 'dimensions', pull: 'clone', put: false }" :list="originalDimensions" :clone="handlerClone"
                       @start="handlerHighlight(true, ColumnType.DIMENSION)" @end="handlerHighlight(false, ColumnType.DIMENSION)">
              <template #item="{ element }">
                <Badge variant="outline" class="cursor-pointer mr-1 mt-1">
                  {{ element.aliasName ? element.aliasName : element.name }}
                </Badge>
              </template>
            </Draggable>
          </div>
        </DataCapCard>
      </aside>
      <div class="flex-1">
        <div class="space-y-6">
          <div class="flex items-center space-x-4 text-sm">
            <div class="min-w-12">{{ $t('dataset.common.columnModeMetric') }}:</div>
            <div :class="cn(`w-full`, (highlight.active && highlight.type === ColumnType.METRIC) && 'border-2 border-primary rounded-sm min-h-8')">
              <Draggable group="metrics" item-key="id" :list="metrics">
                <template #item="{ element, index }">
                  <Badge variant="outline" class="cursor-pointer mr-1 mt-1">
                    <DatasetColumnMetric :element="element"/> &nbsp;
                    <Tooltip :content="$t('common.configure')">
                      <Cog class="pointer" :size="15" @click="handlerColumnConfigure(true, element, ColumnType.METRIC)"/>
                    </Tooltip>
                    <Tooltip :content="$t('common.remove')">
                      <Trash class="point ml-1" :size="15" @click="handlerRemove(index, metrics)"/>
                    </Tooltip>
                  </Badge>
                </template>
              </Draggable>
            </div>
          </div>
          <Separator class="p-0" style="margin-top: 8px;"/>
          <div class="flex h-5 items-center space-x-4 text-sm" style="margin-top: 8px;">
            <div class="min-w-12">{{ $t('dataset.common.columnModeDimension') }}:</div>
            <div :class="cn(`w-full`, (highlight.active && highlight.type === ColumnType.DIMENSION) && 'border-2 border-primary rounded-sm min-h-8')">
              <Draggable group="dimensions" item-key="id" :list="dimensions" class="space-x-1">
                <template #item="{ element, index}">
                  <Badge variant="outline" class="cursor-pointer">
                    {{ element.aliasName ? element.aliasName : element.name }} &nbsp;
                    <Tooltip :content="$t('common.configure')">
                      <Cog class="pointer" :size="15" @click="handlerColumnConfigure(true, element, ColumnType.DIMENSION)"/>
                    </Tooltip>
                    <Tooltip :content="$t('common.remove')">
                      <Trash class="point ml-1" :size="15" @click="handlerRemove(index, dimensions)"/>
                    </Tooltip>
                  </Badge>
                </template>
              </Draggable>
            </div>
          </div>
          <Separator class="p-0" style="margin-top: 8px;"/>
          <div class="flex h-5 items-center space-x-4 text-sm" style="margin-top: 8px;">
            <div class="min-w-12">{{ $t('dataset.common.columnModeFilter') }}:</div>
            <div :class="cn(`w-full`, (highlight.active && highlight.type === ColumnType.DIMENSION) && 'border-2 border-primary rounded-sm min-h-8')">
              <Draggable group="dimensions" item-key="id" :list="filters" class="space-x-1">
                <template #item="{ element, index}">
                  <Badge variant="outline" class="cursor-pointer mr-1 mt-1">
                    {{ element.aliasName ? element.aliasName : element.name }} &nbsp;
                    <Tooltip :content="$t('common.configure')">
                      <Cog class="pointer" :size="15" @click="handlerColumnConfigure(true, element, ColumnType.FILTER)"/>
                    </Tooltip>
                    <Tooltip :content="$t('common.remove')">
                      <Trash class="point ml-1" :size="15" @click="handlerRemove(index, filters)"/>
                    </Tooltip>
                  </Badge>
                </template>
              </Draggable>
            </div>
          </div>
          <Separator class="p-0" style="margin-top: 8px;"/>
          <div class="flex justify-between items-center h-5" style="margin-top: 20px;">
            <div class="flex items-center">
              <span>{{ $t('dataset.common.showPageSize') }}</span>
              <Input type="number" class="w-20 ml-1" v-model="configure.limit" min="1"/>
            </div>
            <div class="flex items-center space-x-4 text-sm">
              <Button size="sm" :disabled="loading" class="pl-3 pr-3" @click="handlerApplyAdhoc">
                <Loader2 v-if="loading" class="w-full justify-center animate-spin"/>
                <CirclePlay v-else/>
              </Button>
              <Button size="sm" class="pl-3 pr-3" variant="outline" :disabled="!showSql.content || loading" @click="handlerShowSql(true)">
                <Eye/>
              </Button>
              <Button size="sm" :disabled="!isPublish || loading" @click="formState.visible = true">
                {{ $t('common.publish') }}
              </Button>
            </div>
          </div>
          <Separator class="p-0" style="margin-top: 20px;"/>
        </div>
        <!-- Result -->
        <VisualEditor :loading="loading" :configuration="configuration" @commitOptions="handlerCommitOptions"/>
      </div>
    </div>
  </div>
  <SqlInfo v-if="showSql.visible" :is-visible="showSql.visible" :content="showSql.content" @close="handlerShowSql(false)"/>
  <DatasetColumnConfigure v-if="columnContent.visible" :is-visible="columnContent.visible" :column-type="columnContent.type" :content="columnContent.content"
                          :configure="columnContent.configure" @close="handlerColumnConfigure(false, null, null)" @commit="handlerCommitColumnConfigure"/>
  <AlertDialog v-if="formState.visible" :default-open="formState.visible">
    <AlertDialogContent>
      <AlertDialogHeader>
        <AlertDialogTitle class="border-b -mt-4 pb-2">{{ $t('common.configure') }}</AlertDialogTitle>
      </AlertDialogHeader>
      <div class="space-y-2">
        <FormField name="name">
          <FormItem>
            <FormLabel class="mr-1 w-20 text-right">
              {{ $t('common.name') }}
            </FormLabel>
            <FormControl>
              <Input v-model="formState.name"/>
            </FormControl>
          </FormItem>
        </FormField>
        <FormField name="description">
          <FormItem>
            <FormLabel class="mr-1 w-20 text-right">
              {{ $t('common.description') }}
            </FormLabel>
            <FormControl>
              <Textarea v-model="formState.description"/>
            </FormControl>
          </FormItem>
        </FormField>
        <FormField name="build">
          <FormItem>
            <FormLabel>
              {{ $t('dataset.common.continuousBuild') }}
              <br/>
            </FormLabel>
            <FormControl>
              <Switch :value="formState.build" @changeValue="formState.build = $event"/>
            </FormControl>
          </FormItem>
        </FormField>
      </div>
      <AlertDialogFooter class="-mb-4 border-t pt-2">
        <Button variant="destructive" @click="formState.visible = false">{{ $t('common.cancel') }}</Button>
        <Button :disabled="!formState.name || published" @click="handlerPublish">
          <Loader2 v-if="published" class="w-full justify-center animate-spin"/>
          {{ $t('common.publish') }}
        </Button>
      </AlertDialogFooter>
    </AlertDialogContent>
  </AlertDialog>
</template>

<script lang="ts">
import Draggable from 'vuedraggable'
import DatasetService from '@/services/dataset'
import { Type } from '@/views/components/visual/Type'
import { Type as ColumnType } from './Type'
import ReportService from '@/services/report'
import { cloneDeep } from 'lodash'
import router from '@/router'
import { Configuration } from '@/views/components/visual/Configuration'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import VisualEditor from '@/views/components/visual/VisualEditor.vue'
import DatasetColumnMetric from '@/views/pages/admin/dataset/components/adhoc/DatasetColumnMetric.vue'
import DatasetColumnConfigure from '@/views/pages/admin/dataset/components/adhoc/DatasetColumnConfigure.vue'
import { defineComponent } from 'vue'
import { Badge } from '@/components/ui/badge'
import { DataCapCard } from '@/views/ui/card'
import { AreaChart, BarChart4, BarChartHorizontal, Baseline, CirclePlay, Cog, Eye, LineChart, Loader2, PieChart, Table, Trash } from 'lucide-vue-next'
import Tooltip from '@/views/ui/tooltip'
import { Separator } from '@/components/ui/separator'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Alert, AlertTitle } from '@/components/ui/alert'
import { ToastUtils } from '@/utils/toast'
import SqlInfo from '@/views/components/sql/SqlInfo.vue'
import { AlertDialog, AlertDialogContent, AlertDialogFooter, AlertDialogHeader } from '@/components/ui/alert-dialog'
import { FormControl, FormField, FormItem, FormLabel } from '@/components/ui/form'
import { Select } from '@/components/ui/select'
import Switch from '@/views/ui/switch'
import { Textarea } from '@/components/ui/textarea'
import { cn } from '@/lib/utils.ts'

export default defineComponent({
  name: 'DatasetAdhoc',
  computed: {
    Type()
    {
      return Type
    },
    ColumnType()
    {
      return ColumnType
    }
  },
  components: {
    DataCapCard,
    Textarea,
    Switch,
    FormField,
    FormControl,
    FormLabel, Select, FormItem,
    AlertDialogFooter, AlertDialogHeader, AlertDialog, AlertDialogContent,
    SqlInfo,
    AlertTitle, Alert,
    Button,
    Input,
    Tooltip,
    Separator,
    Badge,
    DatasetColumnMetric,
    DatasetColumnConfigure,
    CircularLoading,
    Draggable,
    VisualEditor,
    Loader2, Trash, Cog, CirclePlay, Eye, Table, LineChart, BarChart4, AreaChart, PieChart, BarChartHorizontal, Baseline
  },
  setup()
  {
    return {
      cn
    }
  },
  data()
  {
    return {
      loading: false,
      code: null as string | null,
      id: null as number | null,
      originalMetrics: [],
      originalDimensions: [],
      originalData: [],
      metrics: [],
      dimensions: [],
      filters: [],
      configure: {
        columns: [] as any[],
        limit: 1000
      },
      configuration: null as Configuration | null,
      showSql: {
        visible: false,
        content: null as string | null
      },
      columnContent: {
        visible: false,
        type: null as ColumnType | null,
        content: [] as never[],
        configure: null as any | null
      },
      isPublish: false,
      commitOptions: null,
      formState: {
        visible: false,
        name: '',
        description: '',
        build: false
      },
      published: false,
      initialize: false,
      highlight: {
        active: false,
        type: 'METRIC'
      }
    }
  },
  created()
  {
    this.configuration = new Configuration()
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      setTimeout(() => {
        this.initialize = true
        const code = this.$route.params.code as string
        this.code = code as string
        const id = this.$route.params.id
        this.id = id as unknown as number
        DatasetService.getColumnsByCode(this.code)
                      .then(response => {
                        if (response.status) {
                          this.originalData = response.data
                          this.originalMetrics = response.data.filter((item: { mode: string; }) => item.mode === 'METRIC')
                          this.originalDimensions = response.data.filter((item: { mode: string; }) => item.mode === 'DIMENSION')
                          if (id) {
                            ReportService.getById(this.id as number)
                                         .then(response => {
                                           if (response.status) {
                                             this.formState.name = response.data.name
                                             this.formState.description = response.data.description
                                             const query = JSON.parse(response.data.query)
                                             this.mergeColumns(query.columns, this.metrics, ColumnType.METRIC)
                                             this.mergeColumns(query.columns, this.dimensions, ColumnType.DIMENSION)
                                             this.mergeColumns(query.columns, this.filters, ColumnType.FILTER)
                                             this.configure.columns = query.columns
                                             this.configure.limit = query.limit
                                             this.configuration = JSON.parse(response.data.configure)
                                             this.handlerApplyAdhoc()
                                           }
                                         })
                          }
                        }
                        else {
                          ToastUtils.error(response.message)
                        }
                      })
                      .finally(() => this.initialize = false)
      }, 0)
    },
    handlerApplyAdhoc()
    {
      // Set the mode to: FILTER
      this.filters.forEach((item: { mode: ColumnType; }) => item.mode = ColumnType.FILTER)
      this.configure.columns = [...this.splitColumns(this.metrics), ...this.splitColumns(this.dimensions), ...this.splitColumns(this.filters)]
      this.handlerAdhoc()
    },
    handlerAdhoc()
    {
      this.isPublish = true
      this.loading = true
      DatasetService.adhoc(this.code as string, this.configure)
                    .then(response => {
                      if (response.status) {
                        if (this.configuration) {
                          if (response.data.isSuccessful) {
                            this.configuration.headers = response.data.headers
                            this.configuration.columns = response.data.columns
                            this.showSql.content = response.data.content
                            this.configuration.message = null
                          }
                          else {
                            this.configuration.headers = []
                            this.configuration.columns = []
                            this.configuration.message = response.data.message
                          }
                        }
                      }
                      else {
                        ToastUtils.error(response.message)
                      }
                    })
                    .finally(() => this.loading = false)
    },
    handlerClone(value: any)
    {
      return cloneDeep(value)
    },
    handlerRemove(index: number, array: never[])
    {
      array.splice(index, 1)
      this.handlerApplyAdhoc()
    },
    handlerCommit(value: any)
    {
      if (this.configuration) {
        this.configuration.chartConfigure = value
      }
    },
    handlerShowSql(opened: boolean)
    {
      this.showSql.visible = opened
    },
    handlerColumnConfigure(opened: boolean, record: any, type: ColumnType | null)
    {
      this.columnContent.visible = opened
      this.columnContent.type = type
      if (record) {
        this.columnContent.content = this.originalData.find((item: { id: number }) => item.id === record.id) as unknown as never[]
        const foundIndex = this.configure.columns.findIndex((item: { id: unknown; }) => item.id === record.id)
        if (foundIndex !== -1) {
          const column = this.configure.columns[foundIndex]
          column.type = record.type
          this.columnContent.configure = column
        }
        else {
          this.columnContent.configure = { id: record.id, type: record.type }
        }
      }
      else {
        this.columnContent.configure = null
      }
    },
    handlerCommitColumnConfigure(value: any)
    {
      const clonedValue = cloneDeep(value)
      if (clonedValue.mode === ColumnType.METRIC) {
        this.replaceColumn(this.metrics, clonedValue)
      }
      else if (clonedValue.mode === ColumnType.DIMENSION) {
        this.replaceColumn(this.dimensions, clonedValue)
      }
      else if (clonedValue.mode === ColumnType.FILTER) {
        this.replaceColumn(this.filters, clonedValue)
      }
      this.handlerApplyAdhoc()
    },
    handlerCommitOptions(value: any)
    {
      this.commitOptions = value
    },
    handlerPublish()
    {
      this.published = true
      const obj = this.originalDimensions[0] as any
      const configure = {
        id: 0,
        name: this.formState.name,
        realtime: true,
        type: 'DATASET',
        configure: JSON.stringify(this.commitOptions),
        dataset: {
          id: obj.dataset.id
        },
        query: JSON.stringify(this.configure),
        description: this.formState.description
      }
      if (this.id) {
        configure.id = this.id
      }
      ReportService.saveOrUpdate(configure)
                   .then(response => {
                     if (response.status) {
                       ToastUtils.success(this.$t('report.tip.publishSuccess').replace('$VALUE', this.formState.name))
                       this.formState.visible = false
                       if (!this.formState.build) {
                         router.push('/admin/report')
                       }
                     }
                   })
                   .finally(() => this.published = false)
    },
    handlerHighlight(opened: boolean, type: any)
    {
      this.highlight.active = opened
      this.highlight.type = type
    },
    mergeColumns(originalColumns: any[], array: any[], type?: ColumnType)
    {
      originalColumns.filter((item: { mode: ColumnType; id: number; }) => {
        const column = this.originalData.filter((value: { id: number; }) => value.id === item.id)[0]
        if (type) {
          if (item.mode === type) {
            Object.assign(column, item)
            array.push(column)
          }
        }
        else {
          Object.assign(column, item)
          array.push(column)
        }
      })
    },
    replaceColumn(originalColumns: any[], originalValue: any)
    {
      const index = originalColumns.findIndex((item: { id: number; }) => item.id === originalValue.id)
      if (index !== -1) {
        const cloneValue = cloneDeep(originalValue)
        originalColumns[index] = Object.assign(originalValue, originalColumns[index], cloneValue)
      }
    },
    splitColumns(original: any[]): any[]
    {
      const array: any[] = []
      original.forEach((item: { id: number; mode: ColumnType; alias: string; expression: string; name: string; function: string; value: string; order: string; }) => array.push(
          {
            id: item.id,
            mode: item.mode,
            alias: item.alias,
            expression: item.expression,
            name: item.name,
            function: item.function,
            value: item.value,
            order: item.order
          }))
      return array
    }
  }
})
</script>
