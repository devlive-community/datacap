<template>
  <Dialog :is-visible="visible" :title="title as string" :width="'40%'">
    <CircularLoading v-if="loading" :show="loading"/>
    <div v-else>
      <VisualTable :configuration="configuration" :submitted="false"/>
    </div>
    <template #footer>
      <Button variant="outline" size="sm" @click="handlerCancel">
        {{ $t('common.cancel') }}
      </Button>
    </template>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Dialog from '@/views/ui/dialog'
import Button from '@/views/ui/button'
import { HistoryModel } from '@/model/history'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import AuditService from '@/services/audit'
import { toNumber } from 'lodash'
import { Configuration, ConfigurationRequest } from '@/views/components/visual/Configuration'
import VisualTable from '@/views/components/visual/components/VisualTable.vue'

export default defineComponent({
  name: 'HistoryData',
  components: {
    VisualTable,
    CircularLoading,
    Dialog,
    Button
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
      type: Object as () => HistoryModel | null
    }
  },
  data()
  {
    return {
      title: null as string | null,
      loading: false,
      configuration: null as Configuration | null
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
        this.title = `${ this.$t('query.common.historyDataInfo').replace('$VALUE', this.info.id as unknown as string) }`
        this.loading = true
        AuditService.getData(toNumber(this.info.id))
                    .then(response => {
                      if (response.status) {
                        this.configuration = ConfigurationRequest.of(response)
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
