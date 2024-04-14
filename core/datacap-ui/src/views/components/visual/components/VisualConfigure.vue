<template>
  <Dialog :is-visible="visible" :title="$t('common.configure')" width="40%" @close="handlerCancel">
    <div v-if="configuration && formState" class="space-y-2 pl-3 pr-3">
      <Tabs v-model="activeGroup">
        <TabsList class="grid w-full grid-cols-2">
          <TabsTrigger v-for="group in fieldGroup" :key="group.label" :value="group.label as string">{{ group.label }}</TabsTrigger>
        </TabsList>
        <TabsContent :value="activeGroup as any" class="grid grid-cols-3 gap-4">
          <FormField v-for="item in fieldGroup.find(value => value.label === activeGroup)?.fields" :name="item.field as string">
            <FormItem>
              <FormLabel>{{ item.label }}</FormLabel>
              <FormControl>
                <Switch v-if="item.type === 'SWITCH'" class="ml-2" :default-checked="formState[item.field as keyof IChart] as any"
                        @update:checked="formState[item.field as keyof IChart] = $event as any"/>
                <Tooltip v-else-if="item.type === 'SLIDER'" :content="formState[item.field as keyof IChart] ? formState[item.field as keyof IChart] : [item.value] as any">
                  <Slider v-model="formState[item.field as keyof IChart] as any" class="ml-2 w-[95%]" :default-value="[item.value]" :min="item.min" :max="item.max"
                          :step="item.step"
                          @update:modelValue="formState[item.field as keyof IChart] = $event as any"/>
                </Tooltip>
                <Select v-else v-model="formState[item.field as keyof IChart] as string" :disabled="configuration.headers.length === 0">
                  <SelectTrigger class="w-full">
                    <SelectValue :placeholder="`Select ${item.label}`"/>
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem v-if="item.values" class="cursor-pointer" v-for="data in item.values" :value="data.value as string">{{ data.label }}</SelectItem>
                    <SelectItem v-else v-for="item in configuration.headers" class="cursor-pointer" :value="item as string">{{ item }}</SelectItem>
                  </SelectContent>
                </Select>
              </FormControl>
            </FormItem>
          </FormField>
        </TabsContent>
      </Tabs>
    </div>
    <template #footer>
      <div class="space-x-5">
        <Button variant="outline" size="sm" @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button size="sm" @click="handlerSubmit">
          {{ $t('common.apply') }}
        </Button>
      </div>
    </template>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FormControl, FormDescription, FormField, FormItem, FormLabel } from '@/components/ui/form'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '@/components/ui/select'
import { ChartFieldGroup, Configuration, IChart } from '@/views/components/visual/Configuration.ts'
import { cloneDeep, keys } from 'lodash'
import { Switch } from '@/components/ui/switch'
import { Slider } from '@/components/ui/slider'
import Tooltip from '@/views/ui/tooltip'
import Dialog from '@/views/ui/dialog'
import Button from '@/views/ui/button'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'

export default defineComponent({
  name: 'VisualConfigure',
  components: {
    Tabs, TabsTrigger, TabsList, TabsContent,
    Button,
    Dialog,
    Slider,
    Switch,
    SelectGroup, SelectTrigger, SelectContent, SelectItem, Select, SelectLabel, SelectValue,
    FormDescription, FormControl, FormLabel, FormField, FormItem,
    Tooltip
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
    configuration: {
      type: Object as () => Configuration
    },
    fieldGroup: {
      type: Array as () => ChartFieldGroup[],
      default: []
    },
    isVisible: {
      type: Boolean
    }
  },
  data()
  {
    return {
      activeGroup: this.fieldGroup[0]?.label,
      formState: null as IChart | null
    }
  },
  created()
  {
    if (this.configuration && keys(this.configuration.chartConfigure).length > 0) {
      this.formState = cloneDeep(this.configuration.chartConfigure) as IChart
    }
    else {
      const obj = {} as any
      this.fieldGroup.forEach(group => {
        group?.fields?.forEach(field => {
          if (field.field) {
            obj[field.field] = undefined
          }
        })
      })
      this.formState = obj
    }
  },
  methods: {
    handlerCancel()
    {
      this.visible = false
    },
    handlerSubmit()
    {
      this.$emit('change', this.formState)
      this.handlerCancel()
    }
  }
})
</script>
