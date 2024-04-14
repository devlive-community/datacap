<template>
  <div v-if="configuration && formState" class="space-y-2">
    <FormField class="flex items-center" name="xAxis">
      <FormItem class="flex-1">
        <div class="flex items-center">
          <FormLabel class="mr-1 w-2/3 text-right">
            {{ $t('dataset.common.visualConfigureCategoryField') }}
          </FormLabel>
          <FormControl>
            <Select v-model="formState.xAxis" :disabled="configuration.headers.length === 0">
              <SelectTrigger class="w-full">
                <SelectValue placeholder="Select"/>
              </SelectTrigger>
              <SelectContent>
                <SelectItem v-for="item in configuration.headers" :value="item as string">
                  {{ item }}
                </SelectItem>
              </SelectContent>
            </Select>
          </FormControl>
        </div>
      </FormItem>
    </FormField>
    <FormField class="flex items-center" name="yAxis">
      <FormItem class="flex-1">
        <div class="flex items-center mt-2 text-right">
          <FormLabel class="mr-1 w-2/3">
            {{ $t('dataset.common.visualConfigureValueField') }}
          </FormLabel>
          <FormControl>
            <Select v-model="formState.yAxis" :disabled="configuration.headers.length === 0">
              <SelectTrigger class="w-full">
                <SelectValue placeholder="Select"/>
              </SelectTrigger>
              <SelectContent>
                <SelectItem v-for="item in configuration.headers" :value="item as string">
                  {{ item }}
                </SelectItem>
              </SelectContent>
            </Select>
          </FormControl>
        </div>
      </FormItem>
    </FormField>
    <FormField class="flex items-center" name="outerRadius">
      <FormItem class="flex-1">
        <div class="flex items-center mt-2 text-right">
          <FormLabel class="mr-1 w-2/3">
            {{ $t('dataset.common.visualConfigureOuterRadius') }}
          </FormLabel>
          <FormControl>
            <Slider v-model="formState.outerRadius" :default-value="formState.outerRadius" :max="10" :step="0.1" :min="0.01"/>
          </FormControl>
        </div>
      </FormItem>
    </FormField>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '@/components/ui/select'
import { FormControl, FormDescription, FormField, FormItem, FormLabel } from '@/components/ui/form'
import { Slider } from '@/components/ui/slider'
import { Configuration, IChart } from '@/views/components/visual/Configuration.ts'
import { cloneDeep, keys } from 'lodash'

export default defineComponent({
  name: 'VisualPieConfigure',
  components: {
    Slider,
    SelectGroup, SelectTrigger, SelectContent, SelectItem, Select, SelectLabel, SelectValue,
    FormDescription, FormControl, FormLabel, FormField, FormItem
  },
  props: {
    configuration: {
      type: Object as () => Configuration
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
      this.formState = {
        xAxis: undefined,
        yAxis: undefined,
        outerRadius: [0.8]
      }
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
