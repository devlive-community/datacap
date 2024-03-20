<template>
  <div>
    <AlertDialog :default-open="visible">
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle class="border-b -mt-4 pb-2">{{ title }}</AlertDialogTitle>
        </AlertDialogHeader>
        <div v-if="formState">
          <div v-if="columnType === 'FILTER'">
            <FormField class="flex items-center" name="expression">
              <FormItem class="flex-1">
                <div class="flex items-center">
                  <FormLabel class="mr-1 w-40 text-right">
                    {{ $t('common.expression') }}
                  </FormLabel>
                  <FormControl>
                    <Select v-model="formState.expression">
                      <SelectTrigger class="w-full">
                        <SelectValue :placeholder="$t('dataset.tip.selectExpressionTip')"/>
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem :value="Expression.IS_NULL">{{ $t('dataset.common.columnExpressionIsNull') }}</SelectItem>
                        <SelectItem :value="Expression.IS_NOT_NULL">{{ $t('dataset.common.columnExpressionIsNotNull') }}</SelectItem>
                        <SelectItem :value="Expression.IS_LIKE">{{ $t('dataset.common.columnExpressionIsLike') }}</SelectItem>
                        <SelectItem :value="Expression.IS_NOT_LIKE">{{ $t('dataset.common.columnExpressionIsNotLike') }}</SelectItem>
                        <SelectItem :value="Expression.EQ">{{ $t('dataset.common.columnExpressionEquals') }}</SelectItem>
                        <SelectItem :value="Expression.NE">{{ $t('dataset.common.columnExpressionNotEquals') }}</SelectItem>
                        <SelectItem :value="Expression.GT">{{ $t('dataset.common.columnExpressionGreaterThan') }}</SelectItem>
                        <SelectItem :value="Expression.GTE">{{ $t('dataset.common.columnExpressionGreaterThanOrEquals') }}</SelectItem>
                        <SelectItem :value="Expression.LT">{{ $t('dataset.common.columnExpressionLessThan') }}</SelectItem>
                        <SelectItem :value="Expression.LTE">{{ $t('dataset.common.columnExpressionLessThanOrEquals') }}</SelectItem>
                      </SelectContent>
                    </Select>
                  </FormControl>
                </div>
              </FormItem>
            </FormField>
            <FormField v-if="formState.expression && formState.expression !== Expression.IS_NULL && formState.expression !== Expression.IS_NOT_NULL" class="flex items-center"
                       name="value">
              <FormItem class="flex-1 mt-3">
                <div class="flex items-center">
                  <FormLabel class="mr-1 w-40 text-right">
                    {{ $t('common.value') }}
                  </FormLabel>
                  <FormControl>
                    <Input v-model="formState.value"/>
                  </FormControl>
                </div>
              </FormItem>
            </FormField>
          </div>
          <div v-else>
            <FormField v-if="columnType === 'METRIC'" class="flex items-center" name="expression">
              <FormItem class="flex-1">
                <div class="flex items-center">
                  <FormLabel class="mr-1 w-40 text-right">
                    {{ $t('common.expression') }}
                  </FormLabel>
                  <FormControl>
                    <Select v-model="formState.expression">
                      <SelectTrigger class="w-full">
                        <SelectValue :placeholder="$t('dataset.tip.selectExpressionTip')"/>
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem v-if="formState.type === ColumnType.NUMBER" :value="Expression.SUM"> {{ $t('dataset.common.columnExpressionSum') }}</SelectItem>
                        <SelectItem :value="Expression.COUNT">{{ $t('dataset.common.columnExpressionCount') }}</SelectItem>
                        <SelectItem :value="Expression.MAX">{{ $t('dataset.common.columnExpressionMax') }}</SelectItem>
                        <SelectItem :value="Expression.MIN">{{ $t('dataset.common.columnExpressionMin') }}</SelectItem>
                        <SelectItem v-if="formState.type === ColumnType.NUMBER" :value="Expression.AVG">{{ $t('dataset.common.columnExpressionAvg') }}</SelectItem>
                      </SelectContent>
                    </Select>
                  </FormControl>
                </div>
              </FormItem>
            </FormField>
            <FormField class="flex items-center" name="alias">
              <FormItem class="flex-1 mt-3">
                <div class="flex items-center">
                  <FormLabel class="mr-1 w-40 text-right">
                    {{ $t('common.alias') }}
                  </FormLabel>
                  <FormControl>
                    <Input v-model="formState.alias"/>
                  </FormControl>
                </div>
              </FormItem>
            </FormField>
            <FormField class="flex items-center" name="order">
              <FormItem class="flex-1 mt-3">
                <div class="flex items-center">
                  <FormLabel class="mr-1 w-[25%] text-right">
                    {{ $t('common.sort') }}
                  </FormLabel>
                  <FormControl>
                    <ToggleGroup v-model="formState.order" type="single">
                      <ToggleGroupItem value="">{{ $t('dataset.common.columnSortNone') }}</ToggleGroupItem>
                      <ToggleGroupItem value="ASC">{{ $t('dataset.common.columnOrderAsc') }}</ToggleGroupItem>
                      <ToggleGroupItem value="DESC">{{ $t('dataset.common.columnOrderDesc') }}</ToggleGroupItem>
                    </ToggleGroup>
                  </FormControl>
                </div>
              </FormItem>
            </FormField>
            <FormField v-if="columnType === 'DIMENSION'" class="flex items-center" name="function">
              <FormItem class="flex-1 mt-3">
                <div class="flex items-center">
                  <FormLabel class="mr-1 w-40 text-right">
                    {{ $t('dataset.common.customFunction') }}
                  </FormLabel>
                  <FormControl>
                    <Input v-model="formState.function"/>
                  </FormControl>
                </div>
              </FormItem>
            </FormField>
          </div>
        </div>
        <AlertDialogFooter class="-mb-4 border-t pt-2">
          <Button @click="handlerCancel">{{ $t('common.cancel') }}</Button>
          <Button @click="handlerCommit">{{ $t('common.save') }}</Button>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import { cloneDeep } from 'lodash'
import { ColumnType, Type } from '@/views/pages/admin/dataset/Type'
import { Expression } from '@/views/pages/admin/dataset/Expression'
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger
} from '@/components/ui/alert-dialog'
import { Button } from '@/components/ui/button'
import { FormControl, FormDescription, FormField, FormItem, FormLabel } from '@/components/ui/form'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Input } from '@/components/ui/input'
import { ToggleGroup, ToggleGroupItem } from '@/components/ui/toggle-group'
import { Model } from '../../model/model'

export default defineComponent({
  name: 'DatasetColumnConfigure',
  components: {
    ToggleGroup, ToggleGroupItem,
    Input,
    Button,
    AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger,
    SelectGroup, SelectTrigger, SelectContent, SelectItem, Select, SelectLabel, SelectValue,
    FormDescription, FormControl, FormLabel, FormField, FormItem
  },
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    columnType: {
      type: String as unknown as PropType<Type | null>,
      default: () => Type.DIMENSION
    },
    content: {
      type: Object,
      default: () => null
    },
    configure: {
      type: Object as () => any
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
    },
    Expression()
    {
      return Expression
    },
    ColumnType()
    {
      return ColumnType
    }
  },
  data()
  {
    return {
      title: '',
      formState: null as Model | null
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.formState = {} as Model
      if (this.formState) {
        let prefix = `${this.$t('dataset.common.columnModeMetric')}`
        if (this.columnType === Type.DIMENSION) {
          prefix = `${this.$t('dataset.common.columnModeDimension')}`
        }
        else if (this.columnType === Type.FILTER) {
          prefix = `${this.$t('dataset.common.columnModeFilter')}`
        }
        this.title = `${prefix} [ ${this.content.aliasName ? this.content.aliasName : this.content.name} ] ${this.$t('common.configure')}`
        if (this.configure) {
          const cloneValue = cloneDeep(this.configure) as Model
          this.formState = cloneValue
        }
        else {
          this.formState.id = this.content.id
        }
      }
    },
    handlerCommit()
    {
      if (this.formState) {
        this.formState.mode = this.columnType as unknown as string
        this.$emit('commit', this.formState)
        this.handlerCancel()
      }
    },
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>
