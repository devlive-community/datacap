<template>
  <Dialog :is-visible="visible" :title="$t('source.common.previewDML')" :width="'40%'">
    <CircularLoading v-if="loading" :show="loading"/>
    <AceEditor v-else :value="contentDML as string" :read-only="true"/>
    <template #footer>
      <div class="space-x-5">
        <Button variant="outline" size="sm" @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button size="sm" :disabled="submitting" :loading="submitting" @click="handlerSave()">
          {{ $t('common.submit') }}
        </Button>
      </div>
    </template>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AceEditor from '@/views/components/editor/AceEditor.vue'
import Dialog from '@/views/ui/dialog'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import TableService from '@/services/table'
import { SqlColumn, SqlType, TableFilter, TableFilterRequest } from '@/model/table'
import { ToastUtils } from '@/utils/toast'
import Button from '@/views/ui/button'

export default defineComponent({
  name: 'TableRowDelete',
  components: {
    CircularLoading,
    Dialog,
    AceEditor,
    Button
  },
  props: {
    isVisible: {
      type: Boolean
    },
    columns: {
      type: Array<SqlColumn>
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
      submitting: false,
      contentDML: null as string | null,
      configure: null as unknown as TableFilter,
      code: null as string | null
    }
  },
  created()
  {
    const code = this.$route?.params.table as string
    if (code) {
      this.code = code
    }
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.configure = TableFilterRequest.of()
      this.loading = true
      const originalColumns = Array<SqlColumn>()
      this.columns?.forEach((item: any) => originalColumns.push({ original: item }))
      this.configure.columns = originalColumns
      this.configure.type = SqlType.DELETE
      this.configure.preview = true
      TableService.putData(this.code as string, this.configure)
                  .then(response => {
                    if (response.status && response.data && response.data.isSuccessful) {
                      this.contentDML = response.data.content
                    }
                    else {
                      ToastUtils.error(response.message)
                    }
                  })
                  .finally(() => this.loading = false)
    },
    handlerSave()
    {
      this.submitting = false
      this.configure.preview = false
      TableService.putData(this.code as string, this.configure)
                  .then(response => {
                    if (response.status && response.data && response.data.isSuccessful) {
                      ToastUtils.success(this.$t('source.tip.deleteSuccess'))
                      this.handlerCancel()
                    }
                    else {
                      ToastUtils.error(response.data.message)
                    }
                  })
                  .finally(() => this.submitting = false)
    },
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>
