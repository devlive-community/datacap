<template>
  <div>
    <AlertDialog :open="visible" :default-open="visible">
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>
            {{ $t('dashboard.common.delete') + ' [ ' + data?.name + ' ]' }}
          </AlertDialogTitle>
          <AlertDialogDescription>
            <Alert variant="destructive" class="mt-3">
              {{ $t('dashboard.tip.deleteTip1') }}
            </Alert>
            <Alert variant="destructive" class="mt-3">
              {{ $t('dashboard.tip.deleteTip2') }}
            </Alert>
            <Alert class="mt-3">
              {{ $t('dashboard.tip.deleteTip3').replace('$NAME', data?.name) }}
              <Input v-model="inputValue" class="mt-3"/>
            </Alert>
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <Button variant="outline" @click="handlerCancel">
            {{ $t('common.cancel') }}
          </Button>
          <Button :disabled="inputValue !== data?.name || loading" @click="handlerDelete">
            <Loader2 v-if="loading" class="w-full justify-center animate-spin"/>
            {{ $t('dashboard.common.delete') }}
          </Button>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue'
import DashboardService from '@/services/dashboard'
import { DashboardModel } from '@/model/dashboard'
import { ToastUtils } from '@/utils/toast'
import { AlertDialog, AlertDialogContent, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle } from '@/components/ui/alert-dialog'
import { Alert } from '@/components/ui/alert'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Loader2 } from 'lucide-vue-next'

export default defineComponent({
  name: 'DashboardDelete',
  components: {
    Loader2,
    Button,
    Input,
    Alert,
    AlertDialogTitle, AlertDialogHeader, AlertDialogFooter, AlertDialogContent, AlertDialog
  },
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    data: {
      type: Object as () => DashboardModel
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
      loading: false,
      inputValue: null
    }
  },
  methods: {
    handlerDelete()
    {
      this.loading = true;
      DashboardService.deleteById(this.data?.id as number)
          .then((response) => {
            if (response.status) {
              ToastUtils.success(`${this.$t('dashboard.common.delete')} [ ${this.data?.name} ] ${this.$t('common.successfully')}`)
              this.handlerCancel()
            }
          })
          .finally(() => this.loading = false)
    },
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>
