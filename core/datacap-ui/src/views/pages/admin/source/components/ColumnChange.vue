<template>
  <Dialog :is-visible="visible" :title="$t('source.common.newColumn')" :width="'40%'">
    <CircularLoading v-if="loading" :show="loading"/>
    <div v-else class="h-96 overflow-y-auto">
      <div v-for="item in formState.columns" class="mt-1">
        <Collapsible :default-open="item.opened">
          <div class="flex items-center justify-between bg-color-gray">
            <CollapsibleTrigger class="p-2 cursor-pointer" as-child>
              <div class="flex items-center text-sm font-semibold">{{ item.name }}</div>
            </CollapsibleTrigger>
          </div>
          <CollapsibleContent class="space-y-2 mt-1 pl-3 pr-3 pb-3">
            <div class="grid w-full grid-cols-2 gap-4 pt-2 pl-3 pr-3">
              <FormField name="columnName">
                <FormItem class="space-y-1">
                  <FormLabel>{{ $t('source.common.columnName') }}</FormLabel>
                  <FormMessage/>
                  <Input v-model="item.name"/>
                </FormItem>
              </FormField>
              <FormField name="columnType">
                <FormItem class="space-y-1">
                  <FormLabel>{{ $t('source.common.columnType') }}</FormLabel>
                  <FormMessage/>
                  <Input v-model="item.type"/>
                </FormItem>
              </FormField>
              <FormField name="columnLength">
                <FormItem class="space-y-1">
                  <FormLabel>{{ $t('source.common.columnLength') }}</FormLabel>
                  <FormMessage/>
                  <Input type="number" v-model="item.length"/>
                </FormItem>
              </FormField>
              <FormField name="columnDefaultValue">
                <FormItem class="space-y-1">
                  <FormLabel>{{ $t('source.common.columnDefaultValue') }}</FormLabel>
                  <FormMessage/>
                  <Input v-model="item.defaultValue"/>
                </FormItem>
              </FormField>
            </div>
            <div class="grid w-full grid-cols-3 gap-4 pt-2 pl-3 pr-3">
              <FormField name="columnPrimaryKey">
                <FormItem class="space-y-1">
                  <FormLabel>{{ $t('source.common.columnPrimaryKey') }}</FormLabel>
                  <FormMessage/>
                  <Switch class="ml-4" :value="item.primaryKey" @changeValue="item.primaryKey = $event"/>
                </FormItem>
              </FormField>
              <FormField name="columnAutoIncrement">
                <FormItem class="space-y-1">
                  <FormLabel>{{ $t('source.common.columnAutoIncrement') }}</FormLabel>
                  <FormMessage/>
                  <Switch class="ml-4" :value="item.autoIncrement" @changeValue="item.autoIncrement = $event"/>
                </FormItem>
              </FormField>
              <FormField name="columnIsNullable">
                <FormItem class="space-y-1">
                  <FormLabel>{{ $t('source.common.columnIsNullable') }}</FormLabel>
                  <FormMessage/>
                  <Switch class="ml-4" :value="item.isNullable" @changeValue="item.isNullable = $event"/>
                </FormItem>
              </FormField>
            </div>
            <div class="pt-2 pl-3 pr-3">
              <FormField name="columnComment">
                <FormItem class="space-y-1">
                  <FormLabel>{{ $t('source.common.columnComment') }}</FormLabel>
                  <FormMessage/>
                  <Textarea v-model="item.comment"/>
                </FormItem>
              </FormField>
            </div>
          </CollapsibleContent>
        </Collapsible>
      </div>
    </div>
    <template #footer>
      <div class="space-x-5">
        <Button variant="outline" size="sm" @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button size="sm" :loading="submitting" :disabled="submitting" @click="handlerSave()">
          {{ $t('common.save') }}
        </Button>
      </div>
    </template>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Dialog from '@/views/ui/dialog'
import { StructureModel } from '@/model/structure'
import { SqlType, TableModel, TableRequest } from '@/model/table'
import Button from '@/views/ui/button'
import { ColumnModel } from '@/model/column'
import { Textarea } from '@/components/ui/textarea'
import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@/components/ui/collapsible'
import { Minus, Plus } from 'lucide-vue-next'
import { Input } from '@/components/ui/input'
import Switch from '@/views/ui/switch'
import { ToastUtils } from '@/utils/toast'
import { toNumber } from 'lodash'
import TableService from '@/services/table'
import ColumnService from '@/services/column'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'

export default defineComponent({
  name: 'ColumnChange',
  components: {
    CircularLoading,
    Switch,
    Input,
    FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage,
    Minus, Plus,
    Textarea,
    Collapsible, CollapsibleContent, CollapsibleTrigger,
    Button,
    Dialog
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
      type: Object as () => StructureModel | null
    }
  },
  data()
  {
    return {
      loading: false,
      submitting: false,
      formState: null as unknown as TableModel
    }
  },
  created()
  {
    this.formState = TableRequest.of()
    this.formState.type = SqlType.MODIFY
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      if (this.info) {
        this.loading = true
        ColumnService.getById(toNumber(this.info.applyId))
                     .then(response => {
                       if (response.status) {
                         const data = response.data
                         const column: ColumnModel = {
                           name: data.name,
                           type: data.dataType,
                           length: data.maximumLength,
                           comment: data.comment,
                           defaultValue: data.defaultValue,
                           primaryKey: data.isKey === 'PRI',
                           autoIncrement: data.extra === 'auto_increment',
                           isNullable: data.isNullable,
                           opened: true
                         }
                         if (this.formState.columns) {
                           this.formState.columns.push(column)
                         }
                       }
                     })
                     .finally(() => this.loading = false)
      }
    },
    handlerSave()
    {
      if (this.info) {
        this.submitting = true
        TableService.manageColumn(toNumber(this.info.tableId), this.formState)
                    .then(response => {
                      if (response.data) {
                        if (response.data.isSuccessful) {
                          const columns = this.formState?.columns?.map(item => item.name).join(', ') as string
                          ToastUtils.success(this.$t('source.tip.changeColumnSuccess').replace('$VALUE', columns))
                          this.handlerCancel()
                        }
                        else {
                          ToastUtils.error(response.message)
                        }
                      }
                    })
                    .finally(() => this.submitting = false)
      }
    },
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>
