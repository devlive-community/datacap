<template>
  <div>
    <AlertDialog :default-open="visible">
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle class="border-b -mt-4 pb-2">
            {{ $t('dataset.common.rebuild') + ' [ ' + data?.name + ' ]' }}
          </AlertDialogTitle>
        </AlertDialogHeader>
        <Alert>
          <AlertTitle>{{ $t('dataset.tip.rebuildProgress') }}</AlertTitle>
        </Alert>
        <AlertDialogFooter class="-mb-4 border-t pt-2">
          <Button :disabled="loading" @click="handlerRebuild">
            <Loader2 v-if="loading" class="w-full justify-center animate-spin"/>
            {{ $t('dataset.common.rebuild') }}
          </Button>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import DatasetService from '@/services/dataset'
import { ToastUtils } from '@/utils/toast'
import { DatasetModel } from '@/model/dataset'
import { AlertDialog, AlertDialogContent, AlertDialogFooter, AlertDialogHeader } from '@/components/ui/alert-dialog'
import { Button } from '@/components/ui/button'
import { Alert, AlertTitle } from '@/components/ui/alert';
import { Loader2 } from 'lucide-vue-next';

export default defineComponent({
  name: 'DatasetRebuild',
  components: {Loader2, AlertTitle, Alert, AlertDialogContent, AlertDialogFooter, Button, AlertDialogHeader, AlertDialog},
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    data: {
      type: Object as () => DatasetModel | null
    }
  },
  data()
  {
    return {
      loading: false
    }
  },
  methods: {
    handlerRebuild()
    {
      if (this.data) {
        this.loading = true
        DatasetService.rebuild(this.data.id)
            .then((response) => {
              if (response.status) {
                ToastUtils.success(`${this.$t('dataset.common.rebuild')} [ ${this.data?.name} ] ${this.$t('common.successfully')}`)
                this.handlerCancel()
              }
            })
            .finally(() => {
              this.loading = false
            })
      }
    },
    handlerCancel()
    {
      this.visible = false
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
  }
});
</script>
