<template>
  <Sheet :open="visible" class="w-full" @update:open="handlerCancel">
    <SheetContent class="min-w-[40%]">
      <SheetHeader class="border-b pb-3">
        <SheetTitle class="-mt-3">{{ $t('function.common.import') }}</SheetTitle>
      </SheetHeader>
      <div class="grid gap-4 py-4">
        <Tabs v-model="formState.mode" :default-value="formState.mode" class="w-full">
          <TabsList class="grid w-full grid-cols-2">
            <TabsTrigger :value="FunctionMode.txt">{{ $t('common.content') }}</TabsTrigger>
            <TabsTrigger :value="FunctionMode.url">{{ $t('function.common.importFromUrl') }}</TabsTrigger>
          </TabsList>
          <TabsContent :value="FunctionMode.txt">
            <div class="grid grid-cols-4 items-center gap-4">
              <Label for="content" class="text-right">{{ $t('common.content') }}</Label>
              <Textarea v-model="formState.content" class="col-span-3"/>
            </div>
          </TabsContent>
          <TabsContent :value="FunctionMode.url">
            <div class="space-y-2">
              <Alert variant="destructive">
                <AlertTitle>{{ $t('function.common.keyword') }}</AlertTitle>
                <AlertDescription>(http|https)://datacap.edurt.io/resources/functions/plugin/keywords.txt</AlertDescription>
              </Alert>
              <Alert variant="destructive">
                <AlertTitle>{{ $t('function.common.operator') }}</AlertTitle>
                <AlertDescription>(http|https)://datacap.edurt.io/resources/functions/plugin/operators.txt</AlertDescription>
              </Alert>
              <Alert variant="destructive">
                <AlertTitle>{{ $t('function.common.function') }}</AlertTitle>
                <AlertDescription>(http|https)://datacap.edurt.io/resources/functions/plugin/functions.txt</AlertDescription>
              </Alert>
            </div>
            <div class="grid grid-cols-4 items-center mt-2 gap-4">
              <Label for="content" class="text-right">{{ $t('common.content') }}</Label>
              <Textarea v-model="formState.content" class="col-span-3"/>
            </div>
          </TabsContent>
        </Tabs>
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="plugin" class="text-right">{{ $t('common.plugin') }}</Label>
          <Select v-model="formState.plugin as string">
            <SelectTrigger class="col-span-3">
              <SelectValue :placeholder="$t('function.tip.selectPluginHolder')"/>
            </SelectTrigger>
            <SelectContent>
              <SelectGroup v-for="key in Object.keys(plugins)">
                <SelectLabel>{{ key }}</SelectLabel>
                <SelectItem v-for="plugin in plugins[key as any]" :value="(plugin as any).name" class="cursor-pointer">{{ (plugin as any).name }}</SelectItem>
              </SelectGroup>
            </SelectContent>
          </Select>
        </div>
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="description" class="text-right">{{ $t('common.type') }}</Label>
          <Select v-model="formState.type">
            <SelectTrigger class="col-span-3">
              <SelectValue :placeholder="$t('function.tip.selectTypeHolder')"/>
            </SelectTrigger>
            <SelectContent>
              <SelectItem v-for="item in types" :value="item.value" class="cursor-pointer">{{ item.label }}</SelectItem>
            </SelectContent>
          </Select>
        </div>
      </div>
      <SheetFooter class="absolute bottom-0 left-0 right-0 mb-3 mr-3 pt-3 border-t">
        <SheetClose as-child>
          <Button :disabled="loading" variant="destructive" @click="handlerCancel()">{{ $t('common.cancel') }}</Button>
          <Button :loading="loading" @click="handlerSave()">
            <Loader2 v-if="loading" class="w-full justify-center animate-spin"/>
            {{ $t('function.common.import') }}
          </Button>
        </SheetClose>
      </SheetFooter>
    </SheetContent>
  </Sheet>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Sheet, SheetClose, SheetContent, SheetDescription, SheetFooter, SheetHeader, SheetTitle, SheetTrigger } from '@/components/ui/sheet'
import { useI18n } from 'vue-i18n'
import { createDefaultType } from '@/views/pages/system/function/FunctionUtils'
import { FunctionImportModel, FunctionMode } from '@/model/function'
import FunctionService from '@/services/function'
import SourceService from '@/services/source'
import { ToastUtils } from '@/utils/toast'
import { Button } from '@/components/ui/button'
import { Loader2 } from 'lucide-vue-next'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Textarea } from '@/components/ui/textarea'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert'

export default defineComponent({
  name: 'FunctionImport',
  components: {
    Textarea,
    SelectItem, SelectLabel, SelectValue, SelectTrigger, SelectGroup, SelectContent, Select,
    Input,
    Label,
    Loader2,
    Button,
    Sheet, SheetClose, SheetContent, SheetDescription, SheetFooter, SheetHeader, SheetTitle, SheetTrigger,
    Tabs, TabsContent, TabsList, TabsTrigger,
    Alert, AlertDescription, AlertTitle
  },
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    }
  },
  setup()
  {
    const types = createDefaultType(useI18n())
    return {
      types
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
    },
    FunctionMode()
    {
      return FunctionMode
    }
  },
  data()
  {
    return {
      formState: null as unknown as FunctionImportModel,
      loading: false,
      plugins: []
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.formState = {
        mode: FunctionMode.txt,
        plugin: undefined,
        content: undefined,
        type: undefined
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
      this.loading = true
      const plugins = []
      plugins.push(this.formState.plugin as string)
      this.formState.plugin = plugins
      FunctionService.import(this.formState)
          .then((response) => {
            if (response.status) {
              ToastUtils.success('Create successful')
              this.handlerCancel()
            }
            else {
              ToastUtils.error(response.message)
            }
          })
          .finally(() => this.loading = false)
    },
    handlerCancel()
    {
      this.$emit('close', false)
    }
  }
})
</script>
