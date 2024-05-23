<template>
  <div class="layout">
    <Card title-class="p-2">
      <template #title>{{ title }}</template>
      <template #extra>
        <div class="space-x-5">
          <Button size="sm" @click="handlerAddReport(true)">
            {{ $t('dashboard.common.addReport') }}
          </Button>
          <Button size="sm" @click="handlerSaveVisible(true)">
            {{ $t('common.save') }}
          </Button>
        </div>
      </template>
      <div id="content">
        <GridLayout ref="refLayout" :layout="layouts" :responsive="true" :col-num="12" :row-height="30" :is-draggable="true" :is-resizable="true" :vertical-compact="true"
                    :use-css-transforms="true">
          <GridItem v-for="item in layouts" :ref="el => set$Children(el)" :key="item.i" :x="item.x" :y="item.y" :w="item.w" :h="item.h" :i="item.i" :min-h="3" :min-w="3"
                    @resized="handlerResize">
            <Card :style="{width: item.width, height: item.height}" title-class="p-2">
              <template #title>{{ item.title ? item.title : $t('dataset.common.notSpecifiedTitle') }}</template>
              <template #extra>
                <Button size="icon" class="w-6 h-6 rounded-full bg-color-error" @click="handlerRemove(item.i)">
                  <Trash :size="15"/>
                </Button>
              </template>
              <VisualView v-if="item.original" class="-ml-3" :width="item.width.replace('px', '') - 20 + 'px'" :height="item.height.replace('px', '') - 48 + 'px'"
                          :code="item.node.code" :configuration="JSON.parse(item.node.configure)" :type="item.original?.type"
                          :query="item.original.type === 'DATASET' ? JSON.parse(item.original.query as string) : item.original.query" :original="item?.original?.source?.id"/>
              <VisualView v-else class="-ml-3" :width="item.width.replace('px', '') - 20 + 'px'" :height="item.height.replace('px', '') - 48 + 'px'"
                          :code="item.node.code" :configuration="JSON.parse(item.node.configure)" :query="JSON.parse(item.node.query)"/>
            </Card>
          </GridItem>
        </GridLayout>
      </div>
    </Card>
    <Dialog :is-visible="configureVisible" :title="$t('common.configure')">
      <div v-if="formState" class="pl-3 pr-4">
        <FormField name="name">
          <FormItem class="space-y-2">
            <FormLabel>{{ $t('common.name') }}</FormLabel>
            <FormMessage/>
            <Input v-model="formState.name"/>
          </FormItem>
        </FormField>
        <FormField name="description">
          <FormItem class="space-y-2">
            <FormLabel>{{ $t('common.description') }}</FormLabel>
            <FormMessage/>
            <Textarea v-model="formState.description"/>
          </FormItem>
        </FormField>
        <FormField name="avatar">
          <FormItem class="space-y-2">
            <FormLabel>{{ $t('common.avatar') }}</FormLabel>
            <FormMessage/>
            <CropperHome @update:value="handlerCropper"/>
          </FormItem>
        </FormField>
      </div>
      <template #footer>
        <div class="space-x-5">
          <Button variant="outline" size="sm" @click="configureVisible = false">
            {{ $t('common.cancel') }}
          </Button>
          <Button :loading="loading" :disabled="loading" size="sm" @click="handlerSave">
            {{ $t('common.save') }}
          </Button>
        </div>
      </template>
    </Dialog>
  </div>
  <ChartContainer v-if="dataReportVisible" :is-visible="dataReportVisible" @change="handlerChange" @close="handlerAddReport(false)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { GridItem, GridLayout } from 'vue3-grid-layout-next'
import DashboardService from '@/services/dashboard'
import { ReportModel } from '@/model/report.ts'
import VisualView from '@/views/components/visual/VisualView.vue'
import { DashboardModel, DashboardRequest } from '@/model/dashboard.ts'
import Card from '@/views/ui/card'
import { Trash } from 'lucide-vue-next'
import ChartContainer from '@/views/pages/admin/dashboard/components/ChartContainer.vue'
import { ToastUtils } from '@/utils/toast.ts'
import Dialog from '@/views/ui/dialog'
import Button from '@/views/ui/button'
import { Input } from '@/components/ui/input'
import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { cloneDeep } from 'lodash'
import { Textarea } from '@/components/ui/textarea'
import CropperHome from '@/views/components/cropper/CropperHome.vue'
import UploadService from '@/services/upload'

export default defineComponent({
  name: 'DashboardEditor',
  components: {
    CropperHome,
    Textarea,
    Input,
    ChartContainer,
    VisualView,
    GridItem, GridLayout,
    Card,
    Button,
    Trash,
    Dialog,
    FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage
  },
  props: {
    info: {
      type: Object as () => DashboardModel | null
    }
  },
  data()
  {
    return {
      title: null as string | null,
      reportItems: [] as ReportModel[],
      mapCache: new Map(),
      columnNumber: 12,
      layouts: [] as any[],
      loading: false,
      configureVisible: false,
      formState: null as DashboardModel | null,
      dataReportVisible: false
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
        this.title = this.$t('dashboard.common.create')
        if (this.info) {
          this.title = this.$t('dashboard.common.modifyInfo').replace('$VALUE', this.info.name as string)
          this.formState = cloneDeep(this.info)
          this.layouts = JSON.parse(this.info?.configure as string)
        }
        else {
          this.formState = DashboardRequest.of()
        }
      }, 300)
    },
    handlerResize(i: string | number, w: number, h: number, x: number, y: number)
    {
      console.debug(w, h)
      const node = this.layouts.find((obj: { i: string; }) => obj.i === i)
      node.width = `${ y }px`
      node.height = `${ x }px`
    },
    handlerRemove(i: string | number)
    {
      this.layouts = this.layouts.filter((obj: { i: string; }) => obj.i !== i)
    },
    handlerSaveVisible(opened: boolean)
    {
      this.configureVisible = opened
    },
    handlerCropper(value: any)
    {
      const configure = {
        code: this.formState?.code,
        mode: 'DASHBOARD',
        file: value
      }
      UploadService.upload(configure)
                   .then(response => {
                     if (response.status) {
                       if (this.formState) {
                         this.formState.avatar = response.data
                       }
                       ToastUtils.success(this.$t('common.successfully'))
                     }
                     else {
                       ToastUtils.error(response.message)
                     }
                   })
    },
    handlerSave()
    {
      if (this.formState) {
        this.formState.configure = JSON.stringify(this.layouts)
        this.layouts.forEach((item: { node: { id: any; }; }) => this.formState?.reports?.push({ id: item.node.id }))
        this.loading = true
        DashboardService.saveOrUpdate(this.formState)
                        .then(response => {
                          if (response.status) {
                            ToastUtils.success(this.$t('dashboard.tip.publishSuccess').replace('$VALUE', <string>this.formState?.name))
                            if (response.data) {
                              this.$router.push(`/admin/dashboard/preview/${ response.data?.code }`)
                            }
                            else {
                              this.$router.push('/console/dashboard')
                            }
                          }
                          else {
                            ToastUtils.error(response.message)
                          }
                        })
                        .finally(() => this.loading = false)
      }
    },
    handlerAddReport(opened: boolean)
    {
      this.dataReportVisible = opened
    },
    handlerChange(node: ReportModel)
    {
      const newItem = {
        x: 0,
        y: 0,
        w: 3,
        h: 4,
        width: '350px',
        height: '150px',
        i: 'new-' + Date.now(),
        title: node.name,
        node: {
          id: node.id,
          configure: node.configure,
          code: node.dataset?.code,
          query: node?.query
        },
        original: node
      }
      this.layouts.push(newItem)
    },
    set$Children(vm: any)
    {
      if (vm && vm.i) {
        this.mapCache.set(vm.i, vm)
      }
    }
  }
})
</script>
