<template>
  <div v-if="configuration && formState" class="space-y-2">
    <FormField v-for="item in fields" :name="item.field as string">
      <FormItem>
        <FormLabel>{{ item.label }}</FormLabel>
        <FormControl>
          <Switch v-if="item.type === 'SWITCH'" class="ml-2" :default-checked="formState[item.field as keyof IChart] as any"
                  @update:checked="formState[item.field as keyof IChart] = $event as any"/>
          <Tooltip v-else-if="item.type === 'SLIDER'" :content="formState[item.field as keyof IChart] ? formState[item.field as keyof IChart] : [item.value] as any">
            <Slider v-model="formState[item.field as keyof IChart] as any" class="ml-2 w-[95%]" :default-value="[item.value]" :min="item.min" :max="item.max" :step="item.step"
                    @update:modelValue="formState[item.field as keyof IChart] = $event as any"/>
          </Tooltip>
          <Select v-else v-model="formState[item.field as keyof IChart] as string" :disabled="configuration.headers.length === 0">
            <SelectTrigger class="w-full">
              <SelectValue :placeholder="`Select ${item.label}`"/>
            </SelectTrigger>
            <SelectContent>
              <SelectItem v-if="item.values" v-for="data in item.values" :value="data as string">{{ data }}</SelectItem>
              <SelectItem v-else v-for="item in configuration.headers" :value="item as string">{{ item }}</SelectItem>
            </SelectContent>
          </Select>
        </FormControl>
      </FormItem>
    </FormField>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FormControl, FormDescription, FormField, FormItem, FormLabel } from '@/components/ui/form'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '@/components/ui/select'
import { ChartField, Configuration, IChart } from '@/views/components/visual/Configuration.ts'
import { cloneDeep, keys } from 'lodash'
import { Switch } from '@/components/ui/switch'
import { Slider } from '@/components/ui/slider'
import Tooltip from '@/views/ui/tooltip'

export default defineComponent({
  name: 'VisualConfigure',
  components: {
    Slider,
    Switch,
    SelectGroup, SelectTrigger, SelectContent, SelectItem, Select, SelectLabel, SelectValue,
    FormDescription, FormControl, FormLabel, FormField, FormItem,
    Tooltip
  },
  props: {
    configuration: {
      type: Object as () => Configuration
    },
    fields: {
      type: Array as () => ChartField[],
      default: []
    }
  },
  watch: {
    formState: {
      handler: 'handlerCommit',
      deep: true
    }
  },
  data()
  {
    return {
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
      this.fields.forEach(field => {
        if (field.field) {
          obj[field.field] = undefined
        }
      })
      this.formState = obj
    }
  },
  methods: {
    handlerCommit()
    {
      this.$emit('change', this.formState)
    }
  }
})
</script>
