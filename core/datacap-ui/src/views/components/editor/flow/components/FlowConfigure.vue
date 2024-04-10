<template>
  <div>
    <Drawer :is-visible="visible" :width="'10%'" @close="handlerCancel">
      <template #title>{{ `[ ${ data?.name } ] ${ $t('common.configure') }` }}</template>
      <div v-for="item in data?.configure" class="mt-4">
        <FormField class="space-x-3 space-y-3" v-if="item.input" :name="item.field">
          <FormItem class="space-y-2">
            <FormLabel class="flex flex-row items-start font-normal space-x-1">
              <span>{{ item.field }}</span>
              <Tooltip v-if="item.description" :content="item.description">
                <HelpCircle :size="15" class="cursor-pointer"/>
              </Tooltip>
            </FormLabel>
            <FormControl v-if="item.type === 'INPUT'" class="flex flex-row items-start font-normal space-x-1">
              <Input v-model="item.value" :disabled="item.override && item.tooltip" :style="{width: item.width + 'px'}"/>
              <Tooltip v-if="item.tooltip" :content="item.tooltip">
                <Checkbox v-model="item.override" class="ml-2"/>
              </Tooltip>
            </FormControl>
            <FormControl v-if="item.type === 'TEXT'" class="flex items-center font-normal space-x-1">
              <Textarea v-model="item.value" :disabled="item.override && item.tooltip" :style="{width: item.width + 'px'}"/>
              <Tooltip v-if="item.tooltip" :content="item.tooltip">
                <Checkbox :default-checked="item.override" class="ml-2" @update:checked="item.override = $event"/>
              </Tooltip>
            </FormControl>
            <FormControl v-if="item.type === 'SWITCH'" class="flex flex-row items-start font-normal space-x-1">
              <Switch :default-checked="item.value" :disabled="item.override && item.tooltip" :style="{width: item.width + 'px'}" @update:checked="item.value = $event"/>
              <Tooltip v-if="item.tooltip" :content="item.tooltip">
                <Checkbox v-model="item.override" class="ml-2"/>
              </Tooltip>
            </FormControl>
            <FormControl v-if="item.type === 'SELECT'" class="flex flex-row items-start font-normal space-x-1">
              <Select v-model="item.value" :disabled="item.override && item.tooltip" :style="{width: item.width + 'px'}">
                <SelectTrigger>
                  <SelectValue placeholder="Select ..."/>
                </SelectTrigger>
                <SelectContent>
                  <SelectGroup>
                    <SelectItem v-for="option in item.defaultValues" :value="option">{{ option }}</SelectItem>
                  </SelectGroup>
                </SelectContent>
              </Select>
              <Tooltip v-if="item.tooltip" :content="item.tooltip">
                <Checkbox v-model="item.override" class="ml-2"/>
              </Tooltip>
            </FormControl>
            <FormControl v-if="item.type === 'NUMBER'" class="flex flex-row items-start font-normal space-x-1">
              <Input v-model="item.value" type="number" :disabled="item.override && item.tooltip" :style="{width: item.width + 'px'}"/>
              <Tooltip v-if="item.tooltip" :content="item.tooltip">
                <Checkbox v-model="item.override" class="ml-2"/>
              </Tooltip>
            </FormControl>
          </FormItem>
        </FormField>
      </div>
      <template #footer>
        <Button @click="handlerSave()">
          <Save :size="18" class="mr-2"/>
          {{ $t('common.save') }}
        </Button>
      </template>
    </Drawer>
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue'
import { Configuration } from '@/views/components/editor/flow/Configuration.ts'
import Drawer from '@/views/ui/drawer'
import Button from '@/views/ui/button'
import Tooltip from '@/views/ui/tooltip'
import { HelpCircle, Save } from 'lucide-vue-next'
import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Checkbox } from '@/components/ui/checkbox'
import { Textarea } from '@/components/ui/textarea'
import { Switch } from '@/components/ui/switch'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '@/components/ui/select'

export default defineComponent({
  name: 'FlowConfigure',
  components: {
    Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue,
    Switch,
    Textarea,
    Checkbox,
    Input,
    Drawer,
    Button,
    Tooltip,
    Save, HelpCircle,
    FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage
  },
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    data: {
      type: Object as () => Configuration | null
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
  methods: {
    handlerSave()
    {
      if (this.data) {
        const configure = {
          id: this.data.id,
          protocol: this.data.protocol,
          configures: this.filterConfigure(this.data.configure),
          type: this.data.nodeType
        }
        this.$emit('onChange', configure)
      }
    },
    filterConfigure(meta: any)
    {
      const result = {} as any
      meta.filter((item: { input: any; }) => item.input)
          .forEach((item: { origin: any; value: any; }) => {
            if (item.value !== null) {
              result[item.origin] = item.value
            }
          })
      return result
    },
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>
