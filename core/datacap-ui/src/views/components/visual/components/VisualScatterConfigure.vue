<template>
  <div v-if="configuration && formState" class="space-y-2">
    <FormField class="flex items-center" name="xAxis">
      <FormItem class="flex-1">
        <div class="flex items-center">
          <FormLabel class="mr-1 w-2/3 text-right">
            {{ $t('dataset.common.visualConfigureXAxis') }}
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
            {{ $t('dataset.common.visualConfigureYAxis') }}
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
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FormControl, FormDescription, FormField, FormItem, FormLabel } from '@/components/ui/form'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Configuration, IChart } from '@/views/components/visual/Configuration.ts'
import { cloneDeep, keys } from 'lodash'

export default defineComponent({
  name: 'VisualScatterConfigure',
  components: {
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
        yAxis: undefined
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
