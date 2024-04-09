<template>
  <Dialog :is-visible="visible" :title="title as string" :width="'40%'" @close="handlerCancel">
    <div class="pl-3 pr-3 space-y-2">
      <Alert type="error">
        <template #description>{{ $t('report.tip.deleteAlert1') }}</template>
      </Alert>
      <Alert type="error">
        <template #description>{{ $t('report.tip.deleteAlert2') }}</template>
      </Alert>
      <Alert type="info">
        <template #description>{{ $t('report.tip.deleteAlert3').replace('$VALUE', info?.name as string) }}</template>
      </Alert>
      <Input v-model="inputValue"/>
    </div>
    <template #footer>
      <div class="space-x-5">
        <Button variant="outline" size="sm" @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button size="sm" variant="destructive" :loading="loading" :disabled="loading || inputValue !== info?.name" @click="handlerSubmit()">
          {{ title }}
        </Button>
      </div>
    </template>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Dialog from '@/views/ui/dialog'
import ReportService from '@/services/report'
import { ToastUtils } from '@/utils/toast'
import Button from '@/views/ui/button'
import Alert from '@/views/ui/alert'
import { Input } from '@/components/ui/input'
import { ReportModel } from '@/model/report'

export default defineComponent({
  name: 'ReportDelete',
  components: {
    Input,
    Alert,
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
      type: Object as () => ReportModel | null
    }
  },
  data()
  {
    return {
      title: null as string | null,
      loading: false,
      inputValue: ''
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      if (this.info) {
        this.title = `${ this.$t('report.common.deleteInfo').replace('$VALUE', this.info.name as string) }`
      }
    },
    handlerSubmit()
    {
      if (this.info) {
        this.loading = true
        ReportService.deleteById(this.info.id as number)
                     .then((response) => {
                       if (response.status) {
                         ToastUtils.success(this.$t('report.tip.deleteSuccess').replace('$VALUE', this.info?.name as string))
                         this.handlerCancel()
                       }
                       else {
                         ToastUtils.error(response.message)
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