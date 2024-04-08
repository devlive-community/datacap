<template>
  <Dialog :is-visible="visible" :title="title as string" :width="'40%'">
    <div class="grid w-full gap-2 pt-1 pl-3 pr-3">
      <Alert type="error" :description="$t('source.tip.truncateTable1')"/>
      <Alert type="error" :description="$t('source.tip.truncateTable2')"/>
      <Alert type="error" :description="$t('source.tip.truncateTable3')"/>
      <Alert type="error" :description="$t('source.tip.truncateTable4')"/>
    </div>
    <CircularLoading v-if="loading" :show="loading"/>
    <div v-else>
      <Divider class="mt-2 mb-4"/>
      <AceEditor :value="formState.statement" :read-only="true"/>
    </div>
    <template #footer>
      <div class="space-x-5">
        <Button variant="outline" size="sm" @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button size="sm" color="#ed4014" :loading="submitting" :disabled="submitting" @click="handlerSubmit(false)">
          {{ $t('source.common.truncateData') }}
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
import { SqlType, TableModel, TableRequest } from '@/model/table'
import Button from '@/views/ui/button'
import Alert from '@/views/ui/alert'
import Divider from '@/views/ui/divider'
import { toNumber } from 'lodash'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import AceEditor from '@/views/components/editor/AceEditor.vue'

export default defineComponent({
  name: 'TableTruncate',
  components: {
    AceEditor,
    CircularLoading,
    Dialog,
    Button,
    Alert,
    Divider
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
      title: null as string | null,
      formState: null as unknown as TableModel
    }
  },
  created()
  {
    this.formState = TableRequest.of()
    this.formState.type = SqlType.TRUNCATE
    if (this.info) {
      this.title = this.$t('source.common.truncateTableData').replace('$VALUE', this.info.title as string)
      this.handlerSubmit(true)
    }
  },
  methods: {
    handlerSubmit(preview: boolean)
    {
      if (this.info && this.formState) {
        if (preview) {
          this.loading = true
        }
        else {
          this.submitting = true
        }
        this.formState.preview = preview
        TableService.getData(toNumber(this.info.applyId), this.formState)
                    .then(response => {
                      if (response.status) {
                        if (preview) {
                          this.formState.statement = response.data.content
                        }
                        else {
                          ToastUtils.success(this.$t('source.tip.truncateTableDataSuccess').replace('$VALUE', this.info?.title as string))
                          this.handlerCancel()
                        }
                      }
                      else {
                        ToastUtils.success(response.message)
                      }
                    })
                    .finally(() => {
                      if (preview) {
                        this.loading = false
                      }
                      else {
                        this.submitting = false
                      }
                    })
      }
    },
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>
