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
import { SqlColumn, SqlType, TableFilter, TableFilterRequest } from '@/model/table'
import TableService from '@/services/table'
import { toNumber } from 'lodash'
import { ToastUtils } from '@/utils/toast'
import Dialog from '@/views/ui/dialog'
import Button from '@/views/ui/button'
import AceEditor from '@/views/components/editor/AceEditor.vue'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'

export default defineComponent({
  name: 'TableCellInfo',
  components: {
    CircularLoading,
    AceEditor,
    Button,
    Dialog
  },
  props: {
    isVisible: {
      type: Boolean
    },
    tableId: {
      type: Number
    },
    columns: {
      type: Array<SqlColumn>
    },
    type: {
      type: Object as () => SqlType
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
  created()
  {
    this.handlerInitialize()
  },
  data()
  {
    return {
      loading: false,
      submitting: false,
      contentDML: null as string | null,
      configure: null as unknown as TableFilter
    }
  },
  methods: {
    handlerInitialize()
    {
      this.configure = TableFilterRequest.of()
      this.loading = true
      if (this.type === SqlType.UPDATE) {
        this.configure.columns = this.columns
      }
      else {
        this.configure.newColumns = this.columns
      }
      this.configure.type = this.type
      this.configure.preview = true
      TableService.putData(toNumber(this.tableId), this.configure)
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
      TableService.putData(toNumber(this.tableId), this.configure)
                  .then(response => {
                    if (response.status && response.data && response.data.isSuccessful) {
                      ToastUtils.success(this.$t('source.tip.updateSuccess'))
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
