<template>
  <Dialog :is-visible="visible" :title="$t('source.common.syncMetadata')" :width="'40%'">
    <div class="pl-3 pr-3 space-y-2">
      <Alert type="error">
        <template #description>{{ $t('source.tip.syncMetadata1') }}</template>
      </Alert>
      <Alert type="error">
        <template #description>{{ $t('source.tip.syncMetadata2') }}</template>
      </Alert>
      <Alert type="info">
        <template #description>{{ $t('source.tip.syncMetadata3').replace('$NAME', info?.name as string) }}</template>
      </Alert>
      <Input v-model="inputValue"/>
    </div>
    <template #footer>
      <div class="space-x-5">
        <Button variant="outline" size="sm" @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button size="sm" :loading="loading" :disabled="loading || inputValue !== info?.name" @click="handlerSubmit()">
          {{ $t('source.common.syncMetadata') }}
        </Button>
      </div>
    </template>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Dialog from '@/views/ui/dialog'
import { SourceModel } from '@/model/source'
import SourceService from '@/services/source'
import { ToastUtils } from '@/utils/toast'
import Button from '@/views/ui/button'
import Alert from '@/views/ui/alert'
import { Input } from '@/components/ui/input'

export default defineComponent({
  name: 'SourceMetadata',
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
      type: Object as () => SourceModel | null
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
  methods: {
    handlerSubmit()
    {
      if (this.info) {
        this.loading = true
        SourceService.syncMetadata(this.info.id as number)
                     .then((response) => {
                       if (response.status) {
                         ToastUtils.success(this.$t('source.tip.syncMetadata4').replace('$NAME', this.info?.name as string))
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
