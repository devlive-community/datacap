<template>
  <Sheet :open="visible" class="w-full" @update:open="handlerCancel">
    <SheetContent class="min-w-[30%]">
      <SheetHeader class="border-b pb-3">
        <SheetTitle class="-mt-3">{{ title }}</SheetTitle>
      </SheetHeader>
      <div class="grid gap-4 py-4">
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="content" class="text-right">{{ $t('common.name') }}</Label>
          <Input v-model="formState.name" :disabled="formState.system" class="col-span-3"/>
        </div>
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
        <div class="grid grid-cols-4 items-center mt-2 gap-4">
          <Label for="description" class="text-right">{{ $t('common.description') }}</Label>
          <Textarea v-model="formState.description" class="col-span-3"/>
        </div>
        <div class="grid grid-cols-4 items-center mt-2 gap-4">
          <Label for="content" class="text-right">{{ $t('common.content') }}</Label>
          <Textarea v-model="formState.content" class="col-span-3"/>
        </div>
      </div>
      <SheetFooter class="absolute bottom-0 left-0 right-0 mb-3 mr-3 pt-3 border-t">
        <SheetClose as-child>
          <Button :disabled="loading" variant="destructive" @click="handlerCancel()">{{ $t('common.cancel') }}</Button>
          <Button :loading="loading" @click="handlerSave()">
            <Loader2 v-if="loading" class="w-full justify-center animate-spin"/>
            {{ $t('common.save') }}
          </Button>
        </SheetClose>
      </SheetFooter>
    </SheetContent>
  </Sheet>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { TemplateModel, TemplateRequest } from '@/model/template'
import { Loader2 } from 'lucide-vue-next'
import { Textarea } from '@/components/ui/textarea'
import { Sheet, SheetClose, SheetContent, SheetFooter, SheetHeader, SheetTitle } from '@/components/ui/sheet'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '@/components/ui/select'
import { ToastUtils } from '@/utils/toast'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import SourceService from '@/services/source'
import TemplateService from '@/services/template'
import { Button } from '@/components/ui/button'
import { cloneDeep } from 'lodash'

export default defineComponent({
  name: 'TemplateInfo',
  components: {
    Button,
    Input,
    Label,
    SheetClose, SheetFooter, SheetTitle, SheetHeader, Sheet, SheetContent,
    Textarea,
    Loader2,
    Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue
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
      type: Object as () => TemplateModel
    }
  },
  data()
  {
    return {
      formState: null as unknown as TemplateModel,
      loading: false,
      title: null as string | null,
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
      SourceService.getPlugins()
          .then(response => {
            if (response.status) {
              this.plugins = response.data
            }
          })

      this.formState = TemplateRequest.of()
      this.title = `${this.$t('template.common.create')}`
      if (this.info) {
        this.formState = cloneDeep(this.info)
        const targetPlugins = (this.info.plugin as string).split(',')
        this.formState.plugin = targetPlugins[0]
        this.title = `${this.$t('template.common.modify').replace('$NAME', this.info.name as string)}`
      }
    },
    handlerSave()
    {
      this.loading = true
      const plugins = []
      plugins.push(this.formState.plugin as string)
      this.formState.plugin = plugins
      TemplateService.saveOrUpdate(this.formState)
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
