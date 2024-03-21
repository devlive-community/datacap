<template>
  <div>
    <AlertDialog :default-open="visible">
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle class="border-b -mt-4 pb-2">
            {{ `[ ${info?.name} ] ${$t('dataset.common.clearData')}` }}
          </AlertDialogTitle>
        </AlertDialogHeader>
        <Alert variant="destructive">
          <AlertTitle>{{ $t('dataset.tip.clearData') }}</AlertTitle>
        </Alert>
        <div class="flex">
          <Card class="left text-center w-1/2">
            <CardHeader class="border-b p-4">
              <CardTitle>{{ $t('dataset.common.totalRows') }}</CardTitle>
            </CardHeader>
            <CardContent class="mt-3">
              <p>{{ info?.totalRows }}</p>
            </CardContent>
          </Card>
          <Card class="ml-3 right text-center w-1/2">
            <CardHeader class="border-b p-4">
              <CardTitle>{{ $t('dataset.common.totalSize') }}</CardTitle>
            </CardHeader>
            <CardContent class="mt-3">
              <p>{{ info?.totalSize }}</p>
            </CardContent>
          </Card>
        </div>
        <AlertDialogFooter class="-mb-4 border-t pt-2">
          <Button variant="outline" @click="handlerCancel">{{ $t('common.cancel') }}</Button>
          <Button :disabled="loading" @click="handlerSubmit">
            <Loader2 v-if="loading" class="w-full justify-center animate-spin"/>
            {{ $t('dataset.common.clearData') }}
          </Button>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import DatasetService from '@/services/dataset'
import { DatasetModel } from '@/model/dataset'
import { ToastUtils } from '@/utils/toast'
import { AlertDialog, AlertDialogContent, AlertDialogFooter, AlertDialogHeader } from '@/components/ui/alert-dialog'
import { Alert, AlertTitle } from '@/components/ui/alert'
import { Loader2 } from 'lucide-vue-next'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'

export default defineComponent({
  name: 'DatasetClear',
  components: {
    CardContent, CardTitle, CardHeader, Card,
    Button,
    Loader2,
    AlertDialogFooter, AlertDialogHeader, AlertTitle, Alert, AlertDialog, AlertDialogContent
  },
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    info: {
      type: Object as () => DatasetModel | null
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
  data()
  {
    return {
      loading: false
    }
  },
  methods: {
    handlerSubmit()
    {
      if (this.info) {
        this.loading = true
        DatasetService.clearData(this.info.code)
            .then((response: { status: boolean; }) => {
              if (response.status) {
                ToastUtils.success(`${this.$t('dataset.common.clearData')} [ ${this.info?.name} ] ${this.$t('common.successfully')}`)
                this.handlerCancel()
              }
              else {
                ToastUtils.error(`${this.$t('dataset.common.clearData')} [ ${this.info?.name} ] ${this.$t('common.fail')}`)
              }
            })
            .finally(() => this.loading = false)
      }
    },
    handlerCancel()
    {
      this.visible = false
    }
  }
});
</script>
