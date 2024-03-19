<template>
  <div class="hidden space-y-6 pb-16 w-full h-full md:block">
    <div class="flex flex-col space-y-8 lg:flex-row lg:space-x-12 lg:space-y-0">
      <aside class="-mx-4 w-[200px]">
        <Card>
          <CardHeader class="p-3 border-b">
            <CardTitle>
              {{ $t('dataset.common.columnModeMetric') }}
            </CardTitle>
          </CardHeader>
          <CardContent class="p-3">
            <Draggable item-key="id" :clone="handlerClone" :group="{ name: 'metrics', pull: 'clone', put: false }" :list="originalMetrics">
              <template #item="{ element }">
                <Badge variant="outline" class="cursor-pointer mr-1">
                  {{ element.aliasName ? element.aliasName : element.name }}
                </Badge>
              </template>
            </Draggable>
          </CardContent>
        </Card>
        <Card class="mt-5">
          <CardHeader class="p-3 border-b">
            <CardTitle>
              {{ $t('dataset.common.columnModeDimension') }}
            </CardTitle>
          </CardHeader>
          <CardContent class="p-3">
            <Draggable item-key="id" :clone="handlerClone" :group="{ name: 'dimensions', pull: 'clone', put: false }" :list="originalDimensions">
              <template #item="{ element }">
                <Badge variant="outline" class="cursor-pointer mr-1 mt-1">
                  {{ element.aliasName ? element.aliasName : element.name }}
                </Badge>
              </template>
            </Draggable>
          </CardContent>
        </Card>
      </aside>
      <div class="flex-1">
        <div class="space-y-6">
          <div class="flex h-5 items-center space-x-4 text-sm">
            <div>{{ $t('dataset.common.columnModeMetric') }}:</div>
            <div>
              <Draggable group="metrics" item-key="id" :list="metrics">
                <template #item="{ element, index }">
                  <Badge variant="outline" class="cursor-pointer mr-1 mt-1">
                    <DatasetColumnMetric :element="element"/> &nbsp;
                    <TooltipProvider>
                      <Tooltip>
                        <TooltipTrigger as-child>
                          <Cog class="pointer" size="15" @click="handlerColumnConfigure(true, element, ColumnType.METRIC)"/>
                        </TooltipTrigger>
                        <TooltipContent>
                          {{ $t('common.configure') }}
                        </TooltipContent>
                      </Tooltip>
                    </TooltipProvider>
                    <TooltipProvider>
                      <Tooltip>
                        <TooltipTrigger as-child>
                          <Trash class="point ml-1" size="15" @click="handlerRemove(index, metrics)"/>
                        </TooltipTrigger>
                        <TooltipContent>
                          {{ $t('common.remove') }}
                        </TooltipContent>
                      </Tooltip>
                    </TooltipProvider>
                  </Badge>
                </template>
              </Draggable>
            </div>
          </div>
          <Separator class="p-0" style="margin-top: 8px;"/>
          <div class="flex h-5 items-center space-x-4 text-sm" style="margin-top: 8px;">
            <div>{{ $t('dataset.common.columnModeDimension') }}:</div>
            <div>
              <Draggable group="dimensions" item-key="id" :list="dimensions">
                <template #item="{ element, index}">
                  <Badge variant="outline" class="cursor-pointer mr-1 mt-1">
                    {{ element.aliasName ? element.aliasName : element.name }} &nbsp;
                    <TooltipProvider>
                      <Tooltip>
                        <TooltipTrigger as-child>
                          <Cog class="pointer" size="15" @click="handlerColumnConfigure(true, element, ColumnType.DIMENSION)"/>
                        </TooltipTrigger>
                        <TooltipContent>
                          {{ $t('common.configure') }}
                        </TooltipContent>
                      </Tooltip>
                    </TooltipProvider>
                    <TooltipProvider>
                      <Tooltip>
                        <TooltipTrigger as-child>
                          <Trash class="point ml-1" size="15" @click="handlerRemove(index, dimensions)"/>
                        </TooltipTrigger>
                        <TooltipContent>
                          {{ $t('common.remove') }}
                        </TooltipContent>
                      </Tooltip>
                    </TooltipProvider>
                  </Badge>
                </template>
              </Draggable>
            </div>
          </div>
          <Separator class="p-0" style="margin-top: 8px;"/>
          <div class="flex h-5 items-center space-x-4 text-sm" style="margin-top: 8px;">
            <div>{{ $t('dataset.common.columnModeFilter') }}:</div>
            <div>
              <Draggable group="dimensions" item-key="id" :list="filters">
                <template #item="{ element, index}">
                  <Badge variant="outline" class="cursor-pointer mr-1 mt-1">
                    {{ element.aliasName ? element.aliasName : element.name }} &nbsp;
                    <TooltipProvider>
                      <Tooltip>
                        <TooltipTrigger as-child>
                          <Cog class="pointer" size="15" @click="handlerColumnConfigure(true, element, ColumnType.FILTER)"/>
                        </TooltipTrigger>
                        <TooltipContent>
                          {{ $t('common.configure') }}
                        </TooltipContent>
                      </Tooltip>
                    </TooltipProvider>
                    <TooltipProvider>
                      <Tooltip>
                        <TooltipTrigger as-child>
                          <Trash class="point ml-1" size="15" @click="handlerRemove(index, filters)"/>
                        </TooltipTrigger>
                        <TooltipContent>
                          {{ $t('common.remove') }}
                        </TooltipContent>
                      </Tooltip>
                    </TooltipProvider>
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
              <Button :disabled="loading" class="pl-3 pr-3" @click="handlerApplyAdhoc">
                <Loader2 v-if="loading" class="w-full justify-center animate-spin"/>
                <CirclePlay v-else/>
              </Button>
              <Button class="pl-3 pr-3" variant="outline" :disabled="!showSql.content || loading" @click="handlerShowSql(true)">
                <Eye/>
              </Button>
              <Button :disabled="!isPublish || loading" @click="formState.visible = true">
                {{ $t('common.publish') }}
              </Button>
            </div>
          </div>
          <Separator class="p-0" style="margin-top: 20px;"/>
        </div>
        <!-- Result -->
        <div class="flex">
          <div class="left flex-1 justify-center">
            <CircularLoading v-if="loading" :show="loading"/>
            <VisualEditor v-else :configuration="configuration" @commitOptions="handlerCommitOptions"/>
          </div>
          <div class="right w-[210px]">
            <Card class="mt-2 ml-1">
              <CardHeader class="p-2 border-b text-center">
                <CardTitle>{{ $t('dataset.common.visualType') }}</CardTitle>
              </CardHeader>
              <CardContent class="pt-2">
                <ToggleGroup v-model="configuration.type" type="single">
                  <div class="toggle-group-row">
                    <ToggleGroupItem class="mr-1" :value="Type.TABLE">
                      <TooltipProvider>
                        <Tooltip>
                          <TooltipTrigger>
                            <Table/>
                          </TooltipTrigger>
                          <TooltipContent>
                            {{ $t('dataset.common.visualTypeTable') }}
                          </TooltipContent>
                        </Tooltip>
                      </TooltipProvider>
                    </ToggleGroupItem>
                    <ToggleGroupItem class="mr-1" :value="Type.LINE">
                      <TooltipProvider>
                        <Tooltip>
                          <TooltipTrigger>
                            <LineChart/>
                          </TooltipTrigger>
                          <TooltipContent>
                            {{ $t('dataset.common.visualTypeLine') }}
                          </TooltipContent>
                        </Tooltip>
                      </TooltipProvider>
                    </ToggleGroupItem>
                    <ToggleGroupItem :value="Type.BAR">
                      <TooltipProvider>
                        <Tooltip>
                          <TooltipTrigger>
                            <BarChart4/>
                          </TooltipTrigger>
                          <TooltipContent>
                            {{ $t('dataset.common.visualTypeBar') }}
                          </TooltipContent>
                        </Tooltip>
                      </TooltipProvider>
                    </ToggleGroupItem>
                    <ToggleGroupItem class="mt-2" :value="Type.AREA">
                      <TooltipProvider>
                        <Tooltip>
                          <TooltipTrigger>
                            <AreaChart/>
                          </TooltipTrigger>
                          <TooltipContent>
                            {{ $t('dataset.common.visualTypeArea') }}
                          </TooltipContent>
                        </Tooltip>
                      </TooltipProvider>
                    </ToggleGroupItem>
                    <ToggleGroupItem class="mt-2 mr-1" :value="Type.PIE">
                      <TooltipProvider>
                        <Tooltip>
                          <TooltipTrigger>
                            <PieChart/>
                          </TooltipTrigger>
                          <TooltipContent>
                            {{ $t('dataset.common.visualTypePie') }}
                          </TooltipContent>
                        </Tooltip>
                      </TooltipProvider>
                    </ToggleGroupItem>
                    <ToggleGroupItem class="mt-2 mr-1" :value="Type.HISTOGRAM">
                      <TooltipProvider>
                        <Tooltip>
                          <TooltipTrigger>
                            <BarChartHorizontal/>
                          </TooltipTrigger>
                          <TooltipContent>
                            {{ $t('dataset.common.visualTypeHistogram') }}
                          </TooltipContent>
                        </Tooltip>
                      </TooltipProvider>
                    </ToggleGroupItem>
                    <ToggleGroupItem class="mt-2 mr-1" :value="Type.WORDCLOUD">
                      <TooltipProvider>
                        <Tooltip>
                          <TooltipTrigger>
                            <Baseline/>
                          </TooltipTrigger>
                          <TooltipContent>
                            {{ $t('dataset.common.visualTypeWordCloud') }}
                          </TooltipContent>
                        </Tooltip>
                      </TooltipProvider>
                    </ToggleGroupItem>
                  </div>
                </ToggleGroup>
              </CardContent>
              <CardHeader class="p-2 border-b text-center">
                <CardTitle>{{ $t('dataset.common.visualConfigure') }}</CardTitle>
              </CardHeader>
              <CardContent class="pt-2">
                <DatasetVisualConfigureLine v-if="configuration.type === Type.LINE" :columns="configuration.headers" @commit="handlerCommit"/>
                <DatasetVisualConfigureBar v-else-if="configuration.type === Type.BAR" :columns="configuration.headers" @commit="handlerCommit"/>
                <DatasetVisualConfigureArea v-else-if="configuration.type === Type.AREA" :columns="configuration.headers" @commit="handlerCommit"/>
                <DatasetVisualConfigurePie v-else-if="configuration.type === Type.PIE" :columns="configuration.headers" @commit="handlerCommit"/>
                <DatasetVisualConfigureHistogram v-else-if="configuration.type === Type.HISTOGRAM" :columns="configuration.headers" @commit="handlerCommit"/>
                <DatasetVisualConfigureWordCloud v-else-if="configuration.type === Type.WORDCLOUD" :columns="configuration.headers" @commit="handlerCommit"/>
                <Alert v-else>
                  <AlertTitle>{{ $t('dataset.common.visualConfigureNotSpecified') }}</AlertTitle>
                </Alert>
              </CardContent>
            </Card>
          </div>
        </div>
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
      <FormField class="flex items-center">
        <FormItem class="flex-1">
          <div class="flex items-center">
            <FormLabel class="mr-1 w-20 text-right">
              {{ $t('common.name') }}
            </FormLabel>
            <FormControl>
              <Input v-model="formState.name"/>
            </FormControl>
          </div>
        </FormItem>
      </FormField>
      <AlertDialogFooter class="-mb-4 border-t pt-2">
        <Button @click="formState.visible = false">{{ $t('common.cancel') }}</Button>
        <Button :disabled="!formState.name" @click="handlerPublish">
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
import DatasetVisualConfigureWordCloud from '@/views/pages/admin/dataset/components/adhoc/DatasetVisualConfigureWordCloud.vue'
import DatasetVisualConfigureHistogram from '@/views/pages/admin/dataset/components/adhoc/DatasetVisualConfigureHistogram.vue'
import DatasetVisualConfigurePie from '@/views/pages/admin/dataset/components/adhoc/DatasetVisualConfigurePie.vue'
import DatasetVisualConfigureArea from '@/views/pages/admin/dataset/components/adhoc/DatasetVisualConfigureArea.vue'
import DatasetColumnMetric from '@/views/pages/admin/dataset/components/adhoc/DatasetColumnMetric.vue'
import DatasetColumnConfigure from '@/views/pages/admin/dataset/components/adhoc/DatasetColumnConfigure.vue'
import DatasetVisualConfigureBar from '@/views/pages/admin/dataset/components/adhoc/DatasetVisualConfigureBar.vue'
import DatasetVisualConfigureLine from '@/views/pages/admin/dataset/components/adhoc/DatasetVisualConfigureLine.vue'
import { defineComponent } from 'vue'
import { Badge } from '@/components/ui/badge'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { AreaChart, BarChart4, BarChartHorizontal, Baseline, CirclePlay, Cog, Eye, LineChart, Loader2, PieChart, Table, Trash } from 'lucide-vue-next'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'
import { Separator } from '@/components/ui/separator'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { RadioGroup, RadioGroupItem } from '@/components/ui/radio-group'
import { ToggleGroup, ToggleGroupItem } from '@/components/ui/toggle-group'
import { Alert, AlertTitle } from '@/components/ui/alert'
import { ToastUtils } from '@/utils/toast'
import SqlInfo from '@/views/components/sql/SqlInfo.vue'
import { AlertDialog, AlertDialogFooter, AlertDialogHeader, AlertDialogContent } from '@/components/ui/alert-dialog'
import { FormControl, FormField, FormItem, FormLabel } from '@/components/ui/form'
import { Select } from '@/components/ui/select'

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
    FormField,
    FormControl,
    FormLabel, Select, FormItem,
    AlertDialogFooter, AlertDialogHeader, AlertDialog, AlertDialogContent,
    SqlInfo,
    AlertTitle, Alert,
    ToggleGroupItem, ToggleGroup,
    RadioGroupItem, RadioGroup,
    Button,
    Input,
    Tooltip, TooltipContent, TooltipTrigger, TooltipProvider,
    Separator,
    CardTitle, CardHeader, CardContent, Card,
    Badge,
    DatasetVisualConfigureWordCloud,
    DatasetVisualConfigureHistogram,
    DatasetVisualConfigurePie,
    DatasetVisualConfigureArea,
    DatasetColumnMetric,
    DatasetColumnConfigure,
    DatasetVisualConfigureBar,
    DatasetVisualConfigureLine,
    CircularLoading,
    Draggable,
    VisualEditor,
    Loader2, Trash, Cog, CirclePlay, Eye, Table, LineChart, BarChart4, AreaChart, PieChart, BarChartHorizontal, Baseline
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
        columns: [],
        limit: 1000
      },
      configuration: null as Configuration | null,
      showSql: {
        visible: false,
        content: null
      },
      columnContent: {
        visible: false,
        type: null as ColumnType | null,
        content: [] as never[],
        configure: null
      },
      isPublish: false,
      commitOptions: null,
      formState: {
        visible: false,
        name: ''
      },
      published: false
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
      }, 0)
    },
    handlerApplyAdhoc()
    {
      // Set the mode to: FILTER
      this.filters.forEach((item: { mode: ColumnType; }) => item.mode = ColumnType.FILTER)
      this.configure.columns = [...this.metrics, ...this.dimensions, ...this.filters]
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
    handlerRemove(index: number, array: [])
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
          this.columnContent.configure = this.configure.columns[foundIndex]
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
        query: JSON.stringify(this.configure)
      }
      if (this.id) {
        configure.id = this.id
      }
      ReportService.saveOrUpdate(configure)
          .then(response => {
            if (response.status) {
              ToastUtils.success(this.$t('report.common.publishSuccess').replace('REPLACE_NAME', this.formState.name))
              this.formState.visible = false
              router.push('/admin/report')
            }
          })
          .finally(() => this.published = false)
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
    }
  }
})
</script>
