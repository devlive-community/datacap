<template>
  <Dialog :is-visible="visible" :title="$t('source.common.filterData')">
    <div class="space-y-2 pl-3 pr-3">
      <FormField name="condition">
        <FormItem class="space-y-1">
          <FormLabel>{{ $t('source.common.filterCondition') }}</FormLabel>
          <FormMessage/>
          <Select v-model="formState.condition">
            <SelectTrigger>
              <SelectValue placeholder="Select"/>
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectItem value="AND">AND</SelectItem>
                <SelectItem value="OR">OR</SelectItem>
              </SelectGroup>
            </SelectContent>
          </Select>
        </FormItem>
      </FormField>
      <Divider class="mt-2 mb-4"/>
      <div v-for="(item, index) in formState.filters">
        <div class="grid w-full grid-cols-3 gap-2 pt-1 pl-3 pr-3">
          <FormField name="column">
            <FormItem class="space-y-1">
              <FormMessage/>
              <Select v-model="item.index" @update:modelValue="handlerFetchOperations($event, item)">
                <SelectTrigger>
                  <SelectValue placeholder="Select"/>
                </SelectTrigger>
                <SelectContent>
                  <SelectGroup>
                    <SelectItem v-for="(column, index) in columns" :value="index.toString()" class="cursor-pointer">{{ column }}</SelectItem>
                  </SelectGroup>
                </SelectContent>
              </Select>
            </FormItem>
          </FormField>
          <FormField name="operator">
            <FormItem class="space-y-1">
              <FormMessage/>
              <Select v-model="item.operator" :disabled="!item.index">
                <SelectTrigger>
                  <SelectValue placeholder="Select"/>
                </SelectTrigger>
                <SelectContent>
                  <SelectGroup>
                    <SelectItem v-for="operation in item.operations" :value="Object.keys(OPERATOR)[Object.values(OPERATOR).indexOf(operation)]" class="cursor-pointer">
                      {{ operation }}
                    </SelectItem>
                  </SelectGroup>
                </SelectContent>
              </Select>
            </FormItem>
          </FormField>
          <FormField name="value">
            <FormItem class="space-y-1 flex items-center">
              <FormMessage/>
              <div class="flex space-x-2 items-center">
                <Input v-model="item.value" :disabled="!item.operator"/>
                <Button size="icon" color="#ed4014" class="rounded-full w-4 h-4" @click="handlerRemoveFilter(index)">
                  <Minus/>
                </Button>
              </div>
            </FormItem>
          </FormField>
        </div>
      </div>
      <FormField name="button">
        <FormItem class="space-y-1">
          <FormLabel>{{ $t('source.common.filterCondition') }}</FormLabel>
          <Button size="sm" class="ml-2" @click="handlerAddFilter">
            {{ $t('source.common.addFilter') }}
          </Button>
        </FormItem>
      </FormField>
    </div>
    <template #footer>
      <div class="space-x-5">
        <Button variant="outline" size="sm" @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button size="sm" @click="handlerApplyFilter">
          {{ $t('common.apply') }}
        </Button>
      </div>
    </template>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { ColumnFilter, ColumnFilterRequest, Operator } from '@/model/table'
import { cloneDeep } from 'lodash'
import Dialog from '@/views/ui/dialog'
import Button from '@/views/ui/button'
import Divider from '@/views/ui/divider'
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Input } from '@/components/ui/input'
import { Minus } from 'lucide-vue-next'

export default defineComponent({
  name: 'TableRowFilter',
  components: {
    Minus,
    Input,
    FormMessage,
    Dialog,
    Button,
    Divider,
    FormControl, FormField, FormItem, FormLabel,
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
    columns: {
      type: Array,
      default: [] as number[]
    },
    types: {
      type: Array,
      default: [] as number[]
    },
    configure: {
      type: Object as () => any
    }
  },
  created()
  {
    if (this.configure) {
      this.formState = cloneDeep(this.configure)
    }
  },
  data()
  {
    return {
      OPERATOR: Operator,
      formState: {
        condition: 'AND',
        filters: [] as any[]
      }
    }
  },
  methods: {
    handlerAddFilter()
    {
      const filter: ColumnFilter = ColumnFilterRequest.of()
      this.formState.filters.push(filter)
    },
    handlerRemoveFilter(index: number)
    {
      this.formState.filters.splice(index, 1)
    },
    handlerFetchOperations(value: any, filter: ColumnFilter)
    {
      const type = this.types[value]
      filter.column = this.columns[value] as string
      if (type === 'Long' || type === 'Double' || type === 'Integer') {
        filter.operations = [Operator.EQ, Operator.NEQ, Operator.GT, Operator.LT, Operator.GTE, Operator.LTE]
      }
      else {
        filter.operations = [Operator.EQ, Operator.NEQ, Operator.LIKE, Operator.NLIKE, Operator.NULL, Operator.NNULL]
      }
    },
    handlerApplyFilter()
    {
      this.$emit('apply', this.formState)
      this.handlerCancel()
    },
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>
