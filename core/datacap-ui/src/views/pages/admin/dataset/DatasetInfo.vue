<template>
  <div class="flex flex-col space-y-1">
    <div class="flex justify-end">
      <Button size="sm" @click="configureVisible = true">
        {{ $t('common.configure') }}
      </Button>
    </div>
    <CircularLoading v-if="loading" :show="loading"/>
    <div v-else>
      <Card v-if="sourceInfo" title-class="p-2" body-class="p-0">
        <template #title>
          <Button size="sm" :loading="running" :disabled="running" @click="handlerRun()">
            {{ $t('query.common.execute') }}
          </Button>
        </template>
        <FormField v-slot="{ componentField }" name="content">
          <FormItem>
            <FormControl>
              <AceEditor :value="value" v-bind="componentField" @update:value="value = $event"/>
            </FormControl>
          </FormItem>
        </FormField>
      </Card>
      <div v-if="data || code" class="mt-3">
        <CircularLoading v-if="running" :show="running"/>
        <AgGridVue v-else-if="data?.data.columns" :style="{height: '300px'}" class="ag-theme-datacap" :pagination="true" :columnDefs="columnDefs" :rowData="data.data.columns"
                   :gridOptions="gridOptions as any"/>
        <Sheet :default-open="configureVisible" :open="configureVisible" @update:open="configureVisible = false">
          <SheetContent side="bottom" class="w-full h-[80%]">
            <SheetHeader class="border-b pb-3">
              <SheetTitle>
                {{ $t('common.configure') }}
                <Button size="sm" class="float-right mr-5 -mt-2" @click="handlerCreate">
                  {{ code ? $t('dataset.common.modify') : $t('dataset.common.create') }}
                </Button>
              </SheetTitle>
            </SheetHeader>
            <Alert v-if="validator" variant="destructive" class="mt-2">{{ validatorMessage }}</Alert>
            <Tabs default-value="columns" class="mt-1">
              <TabsList class="grid w-full grid-cols-2">
                <TabsTrigger value="columns">{{ $t('dataset.common.dataColumn') }}</TabsTrigger>
                <TabsTrigger value="configure">{{ $t('dataset.common.dataConfigure') }}</TabsTrigger>
              </TabsList>
              <TabsContent value="columns">
                <CircularLoading v-if="loading" :show="loading"/>
                <div v-else class="flex w-full flex-col">
                  <div class="flex flex-1 flex-col gap-4 p-1 text-center">
                    <div class="grid items-center gap-3 md:grid-cols-2 md:gap-4 lg:grid-cols-12">
                      <div>{{ $t('dataset.common.columnName') }}</div>
                      <div>{{ $t('dataset.common.columnAlias') }}</div>
                      <div>{{ $t('dataset.common.columnType') }}</div>
                      <div>{{ $t('dataset.common.columnMode') }}</div>
                      <div>{{ $t('dataset.common.columnDefaultValue') }}</div>
                      <div>{{ $t('dataset.common.columnIsNullable') }}</div>
                      <div>{{ $t('dataset.common.columnIsOrderByKey') }}</div>
                      <div>{{ $t('dataset.common.columnIsPartitionKey') }}</div>
                      <div>{{ $t('dataset.common.columnIsPrimaryKey') }}</div>
                      <div>{{ $t('dataset.common.columnIsSampling') }}</div>
                      <div>{{ $t('dataset.common.columnLength') }}</div>
                      <div>{{ $t('common.action') }}</div>
                    </div>
                    <div class="grid gap-3 md:grid-cols-2 md:gap-3 lg:grid-cols-12 h-[480px] overflow-y-auto pt-2 pb-2">
                      <template v-for="(item, index) in formState.columns" :key="index">
                        <div>
                          <Input v-model="item.name" type="text"/>
                        </div>
                        <div>
                          <Input v-model="item.aliasName" type="text"/>
                        </div>
                        <div>
                          <Select v-model="item.type">
                            <SelectTrigger>
                              <SelectValue placeholder="Select a fruit"/>
                            </SelectTrigger>
                            <SelectContent>
                              <SelectItem value="STRING">{{ $t('dataset.common.columnTypeString') }}</SelectItem>
                              <SelectItem value="NUMBER">{{ $t('dataset.common.columnTypeNumber') }}</SelectItem>
                              <SelectItem value="NUMBER_SIGNED">{{ $t('dataset.common.columnTypeNumberSigned') }}</SelectItem>
                              <SelectItem value="BOOLEAN">{{ $t('dataset.common.columnTypeBoolean') }}</SelectItem>
                              <SelectItem value="DATETIME">{{ $t('dataset.common.columnTypeDateTime') }}</SelectItem>
                            </SelectContent>
                          </Select>
                        </div>
                        <div class="ml-4">
                          <div class="flex items-center space-x-2 mt-2">
                            <Label for="airplane-mode">{{ $t('dataset.common.columnModeMetric') }}</Label>
                            <Switch v-model="item.mode" :default-checked="item.mode === 'DIMENSION'" @update:checked="setMode(item, $event)"/>
                            <Label for="airplane-mode">{{ $t('dataset.common.columnModeDimension') }}</Label>
                          </div>
                        </div>
                        <div>
                          <Input v-model="item.defaultValue" type="text" :disabled="item.virtualColumn"/>
                        </div>
                        <div class="mt-2">
                          <Switch v-model="item.nullable" :disabled="item.virtualColumn" :default-checked="item.nullable"
                                  @update:checked="setNullable(item, $event)"/>
                        </div>
                        <div class="mt-2 ml-4">
                          <Switch v-model="item.orderByKey" :default-checked="item.orderByKey" :disabled="item.virtualColumn"
                                  @update:checked="setOrderByKey(item, $event)"/>
                        </div>
                        <div class="mt-2 ml-4">
                          <Switch v-model="item.partitionKey" :disabled="item.virtualColumn" :default-checked="item.partitionKey"
                                  @update:checked="setPartitionKey(item, $event)"/>
                        </div>
                        <div class="mt-2 ml-4">
                          <Switch v-model="item.primaryKey" :disabled="item.virtualColumn" :default-checked="item.primaryKey"
                                  @update:checked="setPrimaryKey(item, $event)"/>
                        </div>
                        <div class="mt-2 ml-4">
                          <Switch v-model="item.samplingKey" :disabled="item.virtualColumn" :default-checked="item.samplingKey"
                                  @update:checked="setSamplingKey(item, $event)"/>
                        </div>
                        <div>
                          <Input v-model="item.length" type="number" :disabled="item.type === 'BOOLEAN' || item.type === 'DATETIME' || item.virtualColumn"/>
                        </div>
                        <div class="space-x-1 ml-4">
                          <Popover>
                            <PopoverTrigger as-child>
                              <Button class="rounded-full w-8 h-8" variant="outline" size="icon">
                                <Pencil :size="15"/>
                              </Button>
                            </PopoverTrigger>
                            <PopoverContent class="w-80">
                              <div class="grid gap-4">
                                <div class="space-y-2">
                                  <h4 class="font-medium leading-none">{{ $t('dataset.common.columnComment') }}</h4>
                                </div>
                                <Textarea v-model="item.comment"/>
                              </div>
                            </PopoverContent>
                          </Popover>
                          <Button class="rounded-full w-8 h-8" variant="destructive" size="icon" :disabled="!item.customColumn" @click="handlerRemoveColumn(index)">
                            <Trash :size="15"/>
                          </Button>
                          <Button class="rounded-full w-8 h-8" size="icon" @click="handlerAddColumn(index)">
                            <Plus :size="15"/>
                          </Button>
                        </div>
                      </template>
                    </div>
                  </div>
                </div>
              </TabsContent>
              <TabsContent value="configure">
                <Card class="border-0 mt-5 shadow-transparent">
                  <CardContent class="grid gap-6 justify-center pt-2 pb-2">
                    <div class="grid grid-cols-2 gap-4">
                      <div class="grid gap-2">
                        <Label for="name">{{ $t('common.name') }}</Label>
                        <Input v-model="formState.name as string"/>
                      </div>
                      <div class="grid gap-2">
                        <Label for="executor">{{ $t('common.executor') }}</Label>
                        <Select v-model="formState.executor">
                          <SelectTrigger>
                            <SelectValue/>
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem v-for="item in executors" :value="item">{{ item }}</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                      <div class="grid gap-2">
                        <Label for="syncMode">{{ $t('dataset.common.syncMode') }}</Label>
                        <Select v-model="formState.syncMode">
                          <SelectTrigger>
                            <SelectValue :placeholder="$t('card.tip.roleHolder')"/>
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="MANUAL">{{ $t('dataset.common.syncModeManual') }}</SelectItem>
                            <SelectItem value="TIMING">{{ $t('dataset.common.syncModeTiming') }}</SelectItem>
                            <SelectItem value="OUT_SYNC">{{ $t('dataset.common.syncModeOutSync') }}</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                      <div class="grid gap-2" v-if="formState.syncMode === 'TIMING'">
                        <Label for="syncMode">{{ $t('dataset.common.columnExpression') }}</Label>
                        <Input v-model="formState.expression as string" placeholder="0 0 * * * ?"/>
                      </div>
                      <div class="grid gap-2" v-if="formState.syncMode === 'TIMING'">
                        <Label for="syncMode">{{ $t('common.scheduler') }}</Label>
                        <Select v-model="formState.scheduler">
                          <SelectTrigger>
                            <SelectValue/>
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem v-for="item in schedulers" :value="item">{{ item }}</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                    </div>
                    <Separator/>
                    <div class="grid gap-2 -mt-3">
                      <Label for="description">
                        <HoverCard>
                          <HoverCardTrigger as-child>
                            <Button variant="link" class="-ml-4">{{ $t('dataset.common.dataLifeCycle') }}</Button>
                          </HoverCardTrigger>
                          <HoverCardContent class="w-80">
                            {{ $t('dataset.tip.lifeCycle') }}
                          </HoverCardContent>
                        </HoverCard>
                      </Label>
                    </div>
                    <Alert v-if="formState.columns.filter(item => item.type === 'DATETIME').length === 0" variant="destructive" class="-mt-3">
                      {{ $t('dataset.tip.lifeCycleMustDateColumn') }}
                    </Alert>
                    <div v-else class="grid grid-cols-2 gap-4 -mt-3">
                      <div class="grid gap-2">
                        <Label>{{ $t('dataset.common.lifeCycleColumn') }}</Label>
                        <Select v-model="formState.lifeCycleColumn as string">
                          <SelectTrigger>
                            <SelectValue/>
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem v-for="item in formState.columns.filter(item => item.type === 'DATETIME')" :key="item.name" :value="item.name">
                              {{ item.name }}
                            </SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                      <div class="grid gap-2">
                        <Label>{{ $t('dataset.common.lifeCycleNumber') }}</Label>
                        <Input :disabled="!formState.lifeCycleColumn" type="number" v-model="formState.lifeCycle as number"/>
                      </div>
                      <div class="grid gap-2">
                        <Label>{{ $t('dataset.common.lifeCycleNumber') }}</Label>
                        <Select :disabled="!formState.lifeCycleColumn" v-model="formState.lifeCycleType as string">
                          <SelectTrigger>
                            <SelectValue/>
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="MONTH">
                              {{ $t('dataset.common.lifeCycleMonth') }}
                            </SelectItem>
                            <SelectItem value="WEEK">
                              {{ $t('dataset.common.lifeCycleWeek') }}
                            </SelectItem>
                            <SelectItem value="DAY">
                              {{ $t('dataset.common.lifeCycleDay') }}
                            </SelectItem>
                            <SelectItem value="HOUR">
                              {{ $t('dataset.common.lifeCycleHour') }}
                            </SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                    </div>
                    <Separator/>
                    <div class="grid gap-2 -mt-3">
                      <Label for="description">{{ $t('common.description') }}</Label>
                      <Textarea v-model="formState.description as string"/>
                    </div>
                  </CardContent>
                </Card>
              </TabsContent>
            </Tabs>
          </SheetContent>
        </Sheet>
      </div>
      <div v-else-if="!sourceInfo" class="mt-3 justify-center items-center">
        <div class="flex flex-col items-center space-y-3">
          <Alert variant="destructive" class="w-1/3">
            {{ i18n.t('dataset.common.onlyPreviewCreate') }}
          </Alert>
          <Button>
            <RouterLink to="/admin/query">
              {{ i18n.t('dataset.common.returnQuery') }}
            </RouterLink>
          </Button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Button from '@/views/ui/button'
import { useI18n } from 'vue-i18n'
import GridOptions from '@/views/components/grid/GridOptions'
import DatasetService from '@/services/dataset'
import { HttpUtils } from '@/utils/http'
import { GridColumn } from '@/views/components/grid/GridColumn'
import PluginService from '@/services/plugin'
import { Sheet, SheetClose, SheetContent, SheetDescription, SheetHeader, SheetTitle, SheetTrigger } from '@/components/ui/sheet'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Alert } from '@/components/ui/alert'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { Input } from '@/components/ui/input'
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Switch } from '@/components/ui/switch'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover'
import { Pencil, Plus, Trash } from 'lucide-vue-next'
import Card from '@/views/ui/card'
import { HoverCard, HoverCardContent, HoverCardTrigger } from '@/components/ui/hover-card'
import { Separator } from '@/components/ui/separator'
import { ToastUtils } from '@/utils/toast'
import { AgGridVue } from 'ag-grid-vue3'
import 'ag-grid-community/styles/ag-grid.css'
import '@/views/components/grid/ag-theme-datacap.css'
import { DatasetModel } from '@/model/dataset'
import { ResponseModel } from '@/model/response.ts'
import AceEditor from '@/views/components/editor/AceEditor.vue'
import { SourceModel } from '@/model/source.ts'
import SourceService from '@/services/source'
import ExecuteService from '@/services/execute'
import { ExecuteModel } from '@/model/execute.ts'
import { FormControl, FormField, FormItem } from '@/components/ui/form'
import { ArrayUtils } from '@/utils/array.ts'
import { join } from 'lodash'

export default defineComponent({
  name: 'DatasetInfo',
  components: {
    FormItem, FormField, FormControl,
    AceEditor,
    Separator,
    Textarea,
    Label,
    Switch,
    SelectTrigger, SelectContent, SelectItem, SelectGroup, SelectValue, Select,
    Input,
    CircularLoading,
    Alert,
    Button,
    SheetClose, Sheet, SheetContent, SheetDescription, SheetHeader, SheetTitle, SheetTrigger,
    Tabs, TabsContent, TabsList, TabsTrigger,
    Popover, PopoverContent, PopoverTrigger,
    Pencil, Trash, Plus,
    Card,
    HoverCard, HoverCardContent, HoverCardTrigger,
    AgGridVue
  },
  setup()
  {
    const i18n = useI18n()
    const gridOptions = GridOptions.createDefaultOptions(i18n)
    return {
      i18n,
      gridOptions
    }
  },
  data()
  {
    return {
      code: null as string | null,
      loading: false,
      saving: false,
      validator: false,
      validatorMessage: null as string | null,
      columnDefs: [] as GridColumn[],
      schedulers: [],
      executors: [],
      configureVisible: false,
      formState: {
        id: null,
        name: null as string | null | undefined,
        description: null as string | null | undefined,
        query: null,
        syncMode: 'MANUAL',
        columns: [] as any[],
        source: { id: null },
        expression: null as string | null,
        scheduler: 'Default',
        executor: 'Default',
        lifeCycle: null as number | null,
        lifeCycleColumn: null as string | null,
        lifeCycleType: null as string | null
      },
      data: null as ResponseModel | null,
      sourceInfo: null as SourceModel | null,
      value: '',
      running: false
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      setTimeout(() => {
        PluginService.getPlugins()
                     .then(response => {
                       if (response.status) {
                         this.schedulers = response.data['scheduler']
                         this.executors = response.data['executor']
                       }
                     })
        const code = this.$route.params.code
        const sourceCode = this.$route.params.sourceCode
        if (code) {
          this.loading = true
          this.code = code as string
          const axios = new HttpUtils().getAxios()
          axios.all([DatasetService.getByCode(this.code), DatasetService.getColumnsByCode(this.code)])
               .then(axios.spread((info, column) => {
                 if (info.status) {
                   this.formState = info.data
                   this.formState.source.id = info.data.source.id
                   this.sourceInfo = info.data.source
                   this.value = info.data.query
                   this.handlerRun()
                 }
                 if (column.status) {
                   this.formState.columns = column.data
                 }
               }))
               .finally(() => this.loading = false)
        }
        else if (sourceCode) {
          this.loading = true
          SourceService.getByCode(sourceCode as string)
                       .then(response => {
                         if (response.status) {
                           this.sourceInfo = response.data
                           this.formState.source.id = response.data.id
                         }
                       })
                       .finally(() => this.loading = false)
        }
      })
    },
    handlerCreate()
    {
      if (!this.beforeCheck()) {
        this.saving = true
        this.formState.query = this.value
        DatasetService.saveOrUpdate(this.formState as unknown as DatasetModel)
                      .then(response => {
                        if (response.status) {
                          ToastUtils.success(`${ this.$t('dataset.tip.publishSuccess').replace('$VALUE', this.formState.name) }`)
                          this.$router.push('/admin/dataset')
                        }
                      })
                      .finally(() => this.saving = false)
      }
    },
    handlerAddColumn(index: number)
    {
      this.formState.columns.splice(index + 1, 0, {
        id: null,
        name: null,
        aliasName: null,
        type: 'STRING',
        comment: null,
        defaultValue: null,
        position: index + 1,
        nullable: false,
        length: 0,
        original: null,
        orderByKey: false,
        partitionKey: false,
        primaryKey: false,
        samplingKey: false,
        mode: 'DIMENSION',
        virtualColumn: true,
        customColumn: true
      })
    },
    handlerRemoveColumn(index: number)
    {
      this.formState.columns.splice(index, 1)
    },
    handlerRun()
    {
      const configure: ExecuteModel = {
        content: this.value,
        name: this.sourceInfo?.id as unknown as string,
        mode: 'DATASET',
        format: 'JSON'
      }
      this.running = true
      ExecuteService.execute(configure, null)
                    .then((response) => {
                      if (response.status) {
                        this.data = response
                        response.data?.headers.forEach((header: any, index: number) => {
                          const columnDef: GridColumn = { headerName: header, field: header }
                          this.columnDefs.push(columnDef)
                          if (this.formState.columns.length === 0) {
                            const column = {
                              id: null,
                              name: `column_${ index + 1 }`,
                              aliasName: header.replace('(', '_').replace(')', ''),
                              type: 'STRING',
                              comment: header,
                              defaultValue: null,
                              position: index,
                              nullable: false,
                              length: 0,
                              original: header,
                              orderByKey: false,
                              partitionKey: false,
                              primaryKey: false,
                              samplingKey: false,
                              mode: 'DIMENSION',
                              virtualColumn: false,
                              customColumn: false
                            }
                            this.formState.columns.push(column)
                          }
                        })
                      }
                      else {
                        ToastUtils.error(response.message)
                      }
                    })
                    .finally(() => this.running = false)
    },
    beforeCheck(): boolean
    {
      const duplicateColumns = ArrayUtils.findDuplicates(this.formState.columns)
      if (duplicateColumns.length > 0) {
        this.validator = true
        this.validatorMessage = this.$t('dataset.validator.duplicateColumn').replace('$VALUE', join(duplicateColumns, ','))
        return true
      }

      const orderByColumns = this.formState.columns.filter(item => item.orderByKey)
      const primaryKeyColumns = this.formState.columns.filter(item => item.primaryKey)
      if (orderByColumns.length === 0 && primaryKeyColumns.length === 0) {
        this.validator = true
        this.validatorMessage = this.$t('dataset.validator.specifiedColumn')
        return true
      }

      if (!this.formState.name) {
        this.validator = true
        this.validatorMessage = this.$t('dataset.validator.specifiedName')
        return true
      }
      return false
    },
    validatorSampling()
    {
      const samplingColumns = this.formState.columns
                                  .filter((item: { samplingKey: boolean; }) => item.samplingKey)
      if (samplingColumns.length === 0) {
        this.validator = false
        this.validatorMessage = null
        return
      }

      const orderByColumns = this.formState.columns
                                 .filter((item: { orderByKey: boolean; }) => item.orderByKey)
      const isNameInOrderByColumns = samplingColumns.every((samplingItem: { name: string; }) => {
        return orderByColumns.some((orderByItem: { name: string; }) => orderByItem.name === samplingItem.name)
      })
      if (!isNameInOrderByColumns) {
        this.validator = true
        this.validatorMessage = this.$t('dataset.tip.validatorSampling') as string
      }
      else {
        this.validator = false
        this.validatorMessage = null
      }
    },
    setMode(item: any, checked: boolean)
    {
      if (!checked) {
        item.mode = 'METRIC'
      }
      else {
        item.mode = 'DIMENSION'
      }
    },
    setNullable(item: any, checked: boolean)
    {
      item.nullable = checked
    },
    setOrderByKey(item: any, checked: boolean)
    {
      item.orderByKey = checked
      this.validatorSampling()
    },
    setPartitionKey(item: any, checked: boolean)
    {
      item.partitionKey = checked
      this.validatorSampling()
    },
    setPrimaryKey(item: any, checked: boolean)
    {
      item.primaryKey = checked
      this.validatorSampling()
    },
    setSamplingKey(item: any, checked: boolean)
    {
      item.samplingKey = checked
      this.validatorSampling()
    }
  }
})
</script>
