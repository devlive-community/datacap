<template>
  <Dialog :is-visible="visible" :title="title as string" :width="'60%'">
    <CircularLoading v-if="loading" :show="loading"/>
    <div v-else class="pl-3 pr-3">
      <Tabs v-model="activeTab" :default-value="activeTab" class="w-full" @update:modelValue="handlerChangeConfigure($event)">
        <TabsList>
          <TabsTrigger v-for="tab in configureTabs" :value="tab">{{ $t(`source.common.${ tab }`) }}</TabsTrigger>
        </TabsList>
        <TabsContent v-if="activeTab === 'source'" value="source" class="max-h-96 overflow-y-auto">
          <FormField v-for="key in Object.keys(plugins)" :key="key" type="radio" name="plugin">
            <FormItem class="space-y-1">
              <FormLabel>{{ key }}</FormLabel>
              <FormDescription>
                <Separator/>
              </FormDescription>
              <FormMessage/>
              <RadioGroup v-model="formState.type" :default-value="formState.type" class="grid w-full grid-cols-6 gap-8 pt-2" @update:modelValue="handlerChangePlugin">
                <FormItem v-for="plugin in plugins[key as any]" :key="plugin.name" class="flex flex-col items-center">
                  <FormLabel class="[&:has([data-state=checked])>div]:border-primary cursor-pointer">
                    <FormControl>
                      <RadioGroupItem :value="plugin.name + '_' + plugin.type" class="sr-only"/>
                    </FormControl>
                    <div class="items-center rounded-full border-4 border-muted p-1 hover:border-accent w-28 h-28 flex justify-center">
                      <Avatar :src="'/static/images/plugin/' + plugin.name + '.png'" :alt="plugin.name" class="w-full h-full"/>
                    </div>
                    <span class="block w-full p-2 text-center font-normal">{{ plugin.name }}</span>
                  </FormLabel>
                </FormItem>
              </RadioGroup>
            </FormItem>
          </FormField>
        </TabsContent>
        <TabsContent v-else :value="activeTab" class="min-h-96 max-h-96 overflow-y-auto pl-4 pr-4">
          <div class="grid w-full grid-cols-2 gap-8 pt-2">
            <FormField v-for="configure in pluginTabConfigure" :name="configure.field">
              <FormItem class="space-y-2">
                <FormLabel>
                  <span v-if="configure.field !== 'configures'">{{ $t('source.common.' + configure.field) }}</span>
                </FormLabel>
                <FormDescription>{{ configure.description }}</FormDescription>
                <FormMessage/>
                <Input v-if="configure.type === 'String'" type="text" :default-value="configure.value" :disabled="configure.disabled" v-model="configure.value"/>
                <Input v-else-if="configure.type === 'Number'" type="number" :disabled="configure.disabled" :max="configure.max" :min="configure.min" v-model="configure.value"/>
                <Switch v-else-if="configure.type === 'Boolean'" :disabled="configure.disabled" v-model="configure.value"/>
                <Upload v-else-if="configure.type === 'File'" multiple :format="['xml']" :on-success="handlerUploadSuccess" :on-remove="handlerUploadRemove"
                        action="/api/v1/source/uploadFile"
                        :headers="{'Authorization': auth?.type + ' ' + auth?.token, 'PluginType': (formState.type as string).split(' ')[0]}">
                  <Button icon="ios-cloud-upload-outline">{{ $t('common.upload') }}</Button>
                </Upload>
                <div v-else>
                  <Button size="icon" class="rounded-full w-5 h-5" @click="handlerPlusConfigure(configure.value)">
                    <Plus :size="15"/>
                  </Button>
                  <FormItem v-for="(element, index) in configure.value" :key="index" class="space-x-2 flex items-center">
                    <FormLabel class="min-w-8 pt-1">{{ $t('common.field') }}</FormLabel>
                    <Input v-model="element.field"/>
                    <FormLabel>{{ $t('common.value') }}</FormLabel>
                    <Input v-model="element.value"/>
                    <div>
                      <Button size="icon" color="#EF4444" class="rounded-full w-5 h-5" @click="handlerMinusConfigure(element, configure.value)">
                        <Minus :size="15"/>
                      </Button>
                    </div>
                  </FormItem>
                </div>
              </FormItem>
            </FormField>
          </div>
        </TabsContent>
      </Tabs>
    </div>
    <template #footer>
      <div class="space-x-5">
        <Button variant="destructive" size="sm" @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button size="sm" :loading="testing" :disabled="testing" @click="handlerTest()">
          {{ $t('common.test') }}
        </Button>
        <Button size="sm" :loading="saving" :disabled="!testInfo.connected || saving" @click="handlerSave()">
          {{ $t('common.save') }}
        </Button>
      </div>
    </template>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { SourceModel, SourceRequest } from '@/model/source'
import { cloneDeep, join } from 'lodash'
import Dialog from '@/views/ui/dialog'
import Button from '@/views/ui/button'
import SourceService from '@/services/source'
import { ToastUtils } from '@/utils/toast'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { cn } from '@/lib/utils'
import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Separator } from '@/components/ui/separator'
import Avatar from '@/views/ui/avatar'
import { RadioGroup, RadioGroupItem } from '@/components/ui/radio-group'
import { Input } from '@/components/ui/input'
import { Switch } from '@/components/ui/switch'
import { Upload } from 'view-ui-plus'
import { TokenUtils } from '@/utils/token'
import '@/views/pages/admin/source/style.css'
import { ResponseModel } from '@/model/response'
import { Minus, Plus } from 'lucide-vue-next'

interface TestInfo
{
  connected: boolean,
  percent: number,
  successful: boolean
}

export default defineComponent({
  name: 'SourceInfo',
  components: {
    Upload,
    Switch,
    Input,
    Avatar,
    Separator,
    CircularLoading,
    Button,
    Dialog,
    Tabs, TabsContent, TabsList, TabsTrigger,
    FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage,
    RadioGroup, RadioGroupItem,
    Plus, Minus
  },
  setup()
  {
    const auth = TokenUtils.getAuthUser()

    return {
      cn,
      auth
    }
  },
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible
      },
      set(value: boolean)
      {
        this.$emit('close', value)
      }
    }
  },
  props: {
    isVisible: {
      type: Boolean
    },
    info: {
      type: Object as () => SourceModel | null
    }
  },
  data()
  {
    return {
      formState: null as unknown as SourceModel,
      loading: false,
      saving: false,
      title: null as string | null,
      testing: false,
      testInfo: null as unknown as TestInfo,
      configureTabs: ['source'],
      activeTab: 'source',
      plugins: [] as any[],
      pluginConfigure: null as unknown as any,
      pluginTabConfigure: null as unknown as any,
      applyConfigure: null as unknown as any
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.testInfo = { connected: false, percent: 0, successful: false }
      this.title = `${ this.$t('source.common.create') }`
      if (this.info) {
        this.title = `${ this.$t('source.common.modify').replace('$NAME', this.info.name as string) }`
        this.loading = true
        SourceService.getById(this.info.id as number)
                     .then(response => {
                       if (response.status) {
                         this.formState = cloneDeep(response.data)
                         this.formState.type = this.formState.type + '_' + this.formState.protocol
                         this.applyConfigure = response.data?.schema
                         this.pluginConfigure = this.applyConfigure?.configures
                         if (this.pluginConfigure) {
                           this.resetConfigureTab()
                         }
                       }
                     })
                     .finally(() => this.loading = false)
      }
      else {
        this.formState = SourceRequest.of()
      }

      SourceService.getPlugins()
                   .then(response => {
                     if (response.status) {
                       this.plugins = response.data
                     }
                   })
    },
    handlerSave()
    {
      this.saving = true
      const temp = (cloneDeep(this.formState.type) as string).split('_')
      let type = temp[1]
      let name = temp[0]
      if (temp.length === 3) {
        type = temp[2]
        name = join([temp[0], temp[1]], ' ')
      }
      const configure = {
        id: this.info?.id as number,
        type: type,
        name: name,
        configure: this.applyConfigure,
        version: this.formState.version
      }
      SourceService.saveOrUpdate(configure)
                   .then((response) => {
                     if (response.status) {
                       ToastUtils.success('Create successful')
                       this.handlerCancel()
                     }
                   })
                   .finally(() => this.saving = false)
    },
    handlerTest()
    {
      this.testing = true
      const temp = (cloneDeep(this.formState.type) as string).split('_')
      const type = temp[1]
      const name = temp[0]
      const configure = { type: type, name: name, configure: this.applyConfigure }
      SourceService.testConnection(configure)
                   .then((response) => {
                     this.testInfo.percent = 100
                     if (response.status) {
                       ToastUtils.success('Test successful')
                       this.testInfo.connected = true
                       this.testInfo.successful = true
                       this.formState.version = response.data?.columns[0]?.version ? response.data?.columns[0]?.version : response.data?.columns[0]?.result
                     }
                     else {
                       ToastUtils.error(response.message)
                       this.testInfo.connected = false
                       this.testInfo.successful = false
                     }
                   })
                   .finally(() => this.testing = false)
    },
    handlerChangePlugin(value: string)
    {
      const pluginAndType = value.split('_')
      const applyPlugins: [] = this.plugins[pluginAndType[1] as any]
      const applyPlugin = applyPlugins.filter(plugin => plugin['name'] === pluginAndType[0])[0] as any
      this.applyConfigure = applyPlugin?.configure
      this.pluginConfigure = applyPlugin?.configure?.configures
      this.resetConfigureTab()
    },
    handlerChangeConfigure(value: any)
    {
      if (value !== 'source') {
        this.pluginTabConfigure = this.pluginConfigure.filter((field: { group: string }) => field.group === value)
      }
    },
    handlerUploadSuccess(response: ResponseModel)
    {
      if (response.status) {
        const configure = this.applyConfigure.configures.filter((configure: { field: string }) => configure.field === 'file')
        configure[0].value.push(response.data)
      }
    },
    handlerUploadRemove(file: any)
    {
      const configure = this.applyConfigure.configures.filter((configure: { field: string }) => configure.field === 'file')
      configure[0].value = configure[0].value.filter((value: string) => !value.endsWith(file.name))
    },
    handlerPlusConfigure(array: Array<any>)
    {
      if (!array) {
        array = new Array<any>()
      }
      const configure = { field: '', value: '' }
      array.push(configure)
    },
    handlerMinusConfigure(configure: any, array: Array<any>)
    {
      const index = array.indexOf(configure)
      if (index !== -1) {
        array.splice(index, 1)
      }
    },
    handlerCancel()
    {
      this.visible = false
    },
    resetConfigureTab()
    {
      this.configureTabs = ['source']
      this.configureTabs = [...this.configureTabs, ...Array.from(new Set(this.pluginConfigure.map((v: { group: string }) => v.group)))] as string[]
    }
  }
})
</script>
