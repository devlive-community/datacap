<template>
  <Dialog :is-visible="visible" :width="'40%'" @close="handlerCancel">
    <template #title>{{ title }}</template>
    <CircularLoading v-if="loading" :show="loading"/>
    <div v-else-if="info">
      <VisualView :code="info.dataset?.code" :type="info.type" :configuration="JSON.parse(info.configure as string)" :query="JSON.parse(info.query as string)"/>
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
import { ReportModel } from '@/model/report'
import Dialog from '@/views/ui/dialog'
import Button from '@/views/ui/button'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import VisualView from '@/views/components/visual/VisualView.vue'

export default defineComponent({
  name: 'ReportView',
  components: {
    VisualView,
    CircularLoading,
    Dialog,
    Button
  },
  props: {
    isVisible: {
      type: Boolean
    },
    info: {
      type: Object as () => ReportModel | null
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
      title: null as string | null
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
        this.title = `${ this.$t('report.common.view').replace('$VALUE', this.info.name as string) }`
      }
    },
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>
