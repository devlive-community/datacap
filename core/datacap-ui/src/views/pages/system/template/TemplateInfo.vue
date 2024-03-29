<template>
  <Sheet :open="visible" class="w-full" @update:open="handlerCancel">
    <SheetContent class="min-w-[25%]">
      <SheetHeader class="border-b pb-3">
        <SheetTitle class="-mt-3">{{ title }}</SheetTitle>
      </SheetHeader>
      <CircularLoading v-if="loading" :show="loading"/>
      <div v-else class="grid gap-4 py-4">
        <FormField v-slot="{ componentField }" name="username">
          <FormItem>
            <FormLabel>{{ $t('common.name') }}</FormLabel>
            <FormControl>
              <Input v-model="formState.name" :disabled="formState.system" v-bind="componentField"/>
            </FormControl>
          </FormItem>
        </FormField>
        <FormField v-slot="{ componentField }" name="plugin">
          <FormItem>
            <FormLabel>{{ $t('common.plugin') }}</FormLabel>
            <FormControl>
              <div>
                <DropdownMenu>
                  <DropdownMenuTrigger as-child>
                    <Button variant="outline" class="col-span-3">
                      {{ $t('function.tip.selectPluginHolder') }}
                      <span v-if="formState.plugin" class="ml-1">[ {{ formState.plugin.split(',').length }} ]</span>
                    </Button>
                  </DropdownMenuTrigger>
                  <DropdownMenuContent class="max-h-48 overflow-y-auto">
                    <DropdownMenuCheckboxItem class="cursor-pointer" v-for="plugin in plugins" v-model:checked="plugin.checked" @update:checked="setChecked"
                                              v-bind="componentField">
                      {{ (plugin as any).name }}
                    </DropdownMenuCheckboxItem>
                  </DropdownMenuContent>
                </DropdownMenu>
              </div>
            </FormControl>
          </FormItem>
        </FormField>
        <FormField v-slot="{ componentField }" name="description">
          <FormItem>
            <FormLabel>{{ $t('common.description') }}</FormLabel>
            <FormControl>
              <Textarea v-model="formState.description" v-bind="componentField"/>
            </FormControl>
          </FormItem>
        </FormField>
        <FormField v-slot="{ componentField }" name="content">
          <FormItem>
            <FormLabel>{{ $t('common.content') }}</FormLabel>
            <FormControl>
              <AceEditor :value="formState.content" v-bind="componentField" @update:value="setContent"/>
            </FormControl>
          </FormItem>
        </FormField>
      </div>
      <SheetFooter class="absolute bottom-0 left-0 right-0 mb-3 mr-3 pt-3 border-t">
        <SheetClose as-child>
          <Button :disabled="saving" variant="destructive" @click="handlerCancel()">{{ $t('common.cancel') }}</Button>
          <Button :disabled="saving" @click="handlerSave()">
            <Loader2 v-if="saving" class="w-full justify-center animate-spin"/>
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
import { ToastUtils } from '@/utils/toast'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import SourceService from '@/services/source'
import TemplateService from '@/services/template'
import { Button } from '@/components/ui/button'
import { DropdownMenu, DropdownMenuCheckboxItem, DropdownMenuContent, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuTrigger } from '@/components/ui/dropdown-menu'
import { cloneDeep } from 'lodash'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import AceEditor from '@/views/components/editor/AceEditor.vue'

export default defineComponent({
  name: 'TemplateInfo',
  components: {
    AceEditor,
    CircularLoading,
    Button,
    Input,
    Label,
    SheetClose, SheetFooter, SheetTitle, SheetHeader, Sheet, SheetContent,
    Textarea,
    Loader2,
    DropdownMenu, DropdownMenuCheckboxItem, DropdownMenuContent, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuTrigger,
    FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage
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
      type: Object as () => TemplateModel | null
    }
  },
  data()
  {
    return {
      formState: null as unknown as TemplateModel,
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
      this.title = `${this.$t('template.common.create')}`
      if (this.info) {
        this.formState = cloneDeep(this.info)
        this.title = `${this.$t('template.common.modify').replace('$NAME', this.info.name as string)}`
      }
      else {
        this.formState = TemplateRequest.of()
      }

      this.loading = true
      SourceService.getPlugins()
          .then(response => {
            if (response.status) {
              this.plugins = Object.values(response.data).reduce((acc, curr) => (acc as any).concat(curr), []) as any[]
              this.formState.plugin?.split(',').forEach(formPlugin => {
                const foundPlugin = this.plugins.find(plugin => plugin.name === formPlugin)
                if (foundPlugin) {
                  foundPlugin.checked = true
                }
              })
              this.setChecked()
            }
          })
          .finally(() => this.loading = false)
    },
    handlerSave()
    {
      this.saving = true
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
          .finally(() => this.saving = false)
    },
    handlerCancel()
    {
      this.$emit('close', false)
    },
    setChecked()
    {
      this.formState.plugin = this.plugins.filter(it => it.checked)
          .map(it => it.name)
          .join(',')
    },
    setContent(value: string)
    {
      this.formState.content = value
    }
  }
})
</script>
