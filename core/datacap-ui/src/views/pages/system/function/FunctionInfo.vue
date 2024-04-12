<template>
  <Sheet :open="visible" class="w-full" @update:open="handlerCancel">
    <SheetContent class="min-w-[25%]">
      <SheetHeader class="border-b pb-3">
        <SheetTitle class="-mt-3">{{ title }}</SheetTitle>
      </SheetHeader>
      <CircularLoading v-if="loading" :show="loading"/>
      <div v-else class="grid gap-4 py-4">
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="name" class="text-right">{{ $t('common.name') }}</Label>
          <Input v-model="formState.name" class="col-span-3"/>
        </div>
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="plugin" class="text-right">{{ $t('common.plugin') }}</Label>
          <MultipleSelect :plugins="plugins" @changeValue="handlerPluginChange"/>
        </div>
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="content" class="text-right">{{ $t('common.content') }}</Label>
          <Textarea v-model="formState.content" class="col-span-3"/>
        </div>
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="description" class="text-right">{{ $t('common.description') }}</Label>
          <Textarea v-model="formState.description" class="col-span-3"/>
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
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="example" class="text-right">{{ $t('function.common.example') }}</Label>
          <Textarea v-model="formState.example" class="col-span-3"/>
        </div>
      </div>
      <SheetFooter class="absolute bottom-0 left-0 right-0 mb-3 mr-3 pt-3 border-t">
        <SheetClose as-child>
          <Button :disabled="saving" variant="destructive" @click="handlerCancel()">{{ $t('common.cancel') }}</Button>
          <Button :disabled="saving" @click="handlerSave()">
            <Loader2 v-if="saving" class="w-full justify-center animate-spin"/>
            {{ $t('common.submit') }}
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
import { FunctionModel } from '@/model/function'
import FunctionService from '@/services/function'
import SourceService from '@/services/source'
import { ToastUtils } from '@/utils/toast'
import { Button } from '@/components/ui/button'
import { Loader2 } from 'lucide-vue-next'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Textarea } from '@/components/ui/textarea'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import MultipleSelect from '@/views/components/select/MultipleSelect.vue'
import { cloneDeep } from 'lodash'

export default defineComponent({
  name: 'FunctionInfo',
  components: {
    MultipleSelect,
    CircularLoading,
    Textarea,
    SelectItem, SelectLabel, SelectValue, SelectTrigger, SelectGroup, SelectContent, Select,
    Input,
    Label,
    Loader2,
    Button,
    Sheet, SheetClose, SheetContent, SheetDescription, SheetFooter, SheetHeader, SheetTitle, SheetTrigger
  },
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    info: {
      type: Object as () => FunctionModel | null
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
    }
  },
  data()
  {
    return {
      formState: null as unknown as FunctionModel,
      loading: false,
      saving: false,
      title: null as string | null,
      plugins: [] as any[]
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.title = `${ this.$t('function.common.create') }`
      if (this.info) {
        this.formState = cloneDeep(this.info)
        this.title = `${ this.$t('function.common.modify').replace('$NAME', this.info.name as string) }`
      }
      else {
        this.formState = {
          name: undefined,
          plugin: undefined,
          content: undefined,
          description: undefined,
          type: undefined,
          example: undefined
        }
      }

      this.loading = true
      SourceService.getPlugins()
                   .then(response => {
                     if (response.status) {
                       this.plugins = Object.values(response.data).reduce((acc, curr) => (acc as any).concat(curr), []) as any[]
                       if (this.formState.plugin) {
                         (this.formState.plugin as string[]).forEach(formPlugin => {
                           const foundPlugin = this.plugins.find(plugin => plugin.name === formPlugin)
                           if (foundPlugin) {
                             foundPlugin.checked = true
                           }
                         })
                       }
                     }
                   })
                   .finally(() => this.loading = false)
    },
    handlerSave()
    {
      this.saving = true
      const plugins = []
      plugins.push(this.formState.plugin as string)
      this.formState.plugin = plugins
      FunctionService.saveOrUpdate(this.formState)
                     .then((response) => {
                       if (response.status) {
                         ToastUtils.success('Create successful')
                         this.visible = false
                       }
                       else {
                         ToastUtils.error(response.message)
                       }
                     })
                     .finally(() => this.saving = false)
    },
    handlerPluginChange(value: string)
    {
      this.formState.plugin = value.split(',')
                                   .join(',')
    },
    handlerCancel()
    {
      this.$emit('close', false)
    }
  }
})
</script>
