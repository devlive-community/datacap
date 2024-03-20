<template>
  <div>
    <AlertDialog :default-open="visible">
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle class="border-b -mt-4 pb-2">
            {{ `[ ${info.name} ] ${$t('dataset.common.syncData')}` }}
          </AlertDialogTitle>
        </AlertDialogHeader>
        <Alert>
          <AlertTitle>
            {{ $t('dataset.tip.syncData') }}
          </AlertTitle>
        </Alert>
        <AlertDialogFooter class="-mb-4 border-t pt-2">
          <Button variant="outline" @click="handlerCancel">{{ $t('common.cancel') }}</Button>
          <Button :disabled="loading" @click="handlerSubmit">
            <Loader2 v-if="loading" class="w-full justify-center animate-spin"/>
            {{ $t('dataset.common.syncData') }}
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
import { AlertDialog, AlertDialogContent, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle } from '@/components/ui/alert-dialog'
import { Button } from '@/components/ui/button'
import { Loader2 } from 'lucide-vue-next'
import { Alert, AlertTitle } from '@/components/ui/alert'

export default defineComponent({
  name: 'DatasetSync',
  components: {
    AlertTitle, Alert,
    Loader2,
    Button,
    AlertDialog, AlertDialogContent, AlertDialogTitle, AlertDialogFooter, AlertDialogHeader
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
        DatasetService.syncData(this.info.id)
            .then(response => {
              if (response.status) {
                ToastUtils.success(`${this.$t('dataset.common.syncData')} [ ${this.info?.name} ] ${this.$t('common.successfully')}`)
                this.handlerCancel()
              }
              else {
                ToastUtils.error(`${this.$t('dataset.common.syncData')} [ ${this.info?.name} ] ${this.$t('common.fail')}`)
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
})
</script>