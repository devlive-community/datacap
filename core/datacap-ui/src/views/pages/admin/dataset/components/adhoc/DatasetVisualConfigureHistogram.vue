<template>
  <div>
    <FormField class="flex items-center">
      <FormItem class="flex-1">
        <div class="flex items-center">
          <FormLabel class="mr-1 w-2/3 text-right">
            {{ $t('dataset.common.visualConfigureCategoryLeftField') }}
          </FormLabel>
          <FormControl>
            <Select v-model="formState.xAxis" :disabled="columns.length === 0">
              <SelectTrigger class="w-full">
                <SelectValue placeholder="Select"/>
              </SelectTrigger>
              <SelectContent>
                <SelectItem v-for="item in columns" :value="item">
                  {{ item }}
                </SelectItem>
              </SelectContent>
            </Select>
          </FormControl>
        </div>
      </FormItem>
    </FormField>
    <FormField class="flex items-center">
      <FormItem class="flex-1">
        <div class="flex items-center mt-2 text-right">
          <FormLabel class="mr-1 w-2/3">
            {{ $t('dataset.common.visualConfigureCategoryRightField') }}
          </FormLabel>
          <FormControl>
            <Select v-model="formState.x2Axis" :disabled="columns.length === 0">
              <SelectTrigger class="w-full">
                <SelectValue placeholder="Select"/>
              </SelectTrigger>
              <SelectContent>
                <SelectItem v-for="item in columns" :value="item">
                  {{ item }}
                </SelectItem>
              </SelectContent>
            </Select>
          </FormControl>
        </div>
      </FormItem>
    </FormField>
    <FormField class="flex items-center">
      <FormItem class="flex-1">
        <div class="flex items-center mt-2 text-right">
          <FormLabel class="mr-1 w-2/3">
            {{ $t('dataset.common.visualConfigureValueField') }}
          </FormLabel>
          <FormControl>
            <Select v-model="formState.yAxis" :disabled="columns.length === 0">
              <SelectTrigger class="w-full">
                <SelectValue placeholder="Select"/>
              </SelectTrigger>
              <SelectContent>
                <SelectItem v-for="item in columns" :value="item">
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
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '@/components/ui/select'
import { FormControl, FormDescription, FormField, FormItem, FormLabel } from '@/components/ui/form'

export default defineComponent({
  name: 'DatasetVisualConfigureHistogram',
  components: {
    SelectGroup, SelectTrigger, SelectContent, SelectItem, Select, SelectLabel, SelectValue,
    FormDescription, FormControl, FormLabel, FormField, FormItem
  },
  props: {
    columns: {
      type: Array,
      default: () => []
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
      formState: {
        xAxis: null,
        x2Axis: null,
        yAxis: null
      }
    }
  },
  methods: {
    handlerCommit()
    {
      this.$emit('commit', this.formState)
    }
  }
})
</script>
