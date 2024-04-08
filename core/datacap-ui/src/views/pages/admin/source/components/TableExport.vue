<template>
  <Dialog :is-visible="visible" :title="title as string">
    <div class="grid w-full grid-cols-2 gap-4 pt-1 pl-3 pr-3">
      <FormField name="format">
        <FormItem class="space-y-2">
          <FormLabel>{{ $t('source.common.exportDataFormat') }}</FormLabel>
          <FormMessage/>
          <RadioGroup v-model="formState.format" :default-value="formState.format">
            <div class="flex items-center space-x-2">
              <RadioGroupItem id="CSV" value="CSV"/>
              <Label for="CSV">CSV</Label>
            </div>
          </RadioGroup>
        </FormItem>
      </FormField>
      <FormField name="count">
        <FormItem class="space-y-1">
          <FormLabel>{{ $t('source.common.exportDataCount') }}</FormLabel>
          <FormMessage/>
          <Input v-model="formState.count" :default-value="formState.count" type="number"/>
        </FormItem>
      </FormField>
    </div>
    <div v-if="formState.path" class="grid w-full pt-1 pl-3 pr-3">
      <FormField name="path">
        <FormItem class="space-y-1">
          <FormLabel>{{ $t('source.common.downloadPath') }}</FormLabel>
          <FormMessage/>
          <div class="flex items-center space-x-1">
            <Input :default-value="formState.path"/>
            <Button size="sm" @click="handlerDownload()">
              {{ $t('source.common.downloadFile') }}
            </Button>
          </div>
        </FormItem>
      </FormField>
    </div>
    <template #footer>
      <div class="space-x-5">
        <Button variant="outline" size="sm" @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button size="sm" :loading="loading" :disabled="loading" @click="handlerSave()">
          {{ $t('source.common.exportData') }}
        </Button>
      </div>
    </template>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Dialog from '@/views/ui/dialog'
import { StructureModel } from '@/model/structure.ts'
import TableService from '@/services/table'
import { ToastUtils } from '@/utils/toast'
import { TableExportModel, TableExportRequest } from '@/model/table'
import Button from '@/views/ui/button'
import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { toNumber } from 'lodash'
import { RadioGroup, RadioGroupItem } from '@/components/ui/radio-group'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'

export default defineComponent({
  name: 'TableExport',
  components: {
    Input,
    Label,
    Dialog,
    Button,
    FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage,
    RadioGroup, RadioGroupItem
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
      title: null as string | null,
      formState: null as unknown as TableExportModel
    }
  },
  created()
  {
    this.formState = TableExportRequest.of()
    if (this.info) {
      this.title = this.$t('source.common.exportDataTable').replace('$VALUE', this.info.title as string)
    }
  },
  methods: {
    handlerSave()
    {
      if (this.info) {
        this.loading = true
        TableService.exportData(toNumber(this.info.applyId), this.formState)
                    .then(response => {
                      if (response.status) {
                        this.formState.path = response.data
                      }
                      else {
                        ToastUtils.error(response.message)
                      }
                    })
                    .finally(() => this.loading = false)
      }
    },
    handlerDownload()
    {
      if (this.formState) {
        window.open(this.formState.path, '_target')
      }
    },
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>
