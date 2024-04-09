<template>
  <Dialog :is-visible="visible" :title="title as string" :width="'60%'" @close="handlerCancel">
    <div class="grid w-full gap-2 pt-1 pl-3 pr-3">
      <FlowView v-if="configure" :info="configure"/>
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
import Alert from '@/views/ui/alert'
import { Input } from '@/components/ui/input'
import { PipelineModel } from '@/model/pipeline.ts'
import FlowView from '@/views/components/editor/flow/FlowView.vue'
import { Configuration, ConfigurationRequest } from '@/views/components/editor/flow/Configuration.ts'

export default defineComponent({
  name: 'PipelineFlow',
  components: {
    FlowView,
    Input,
    Dialog,
    Button,
    Alert
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
      type: Object as () => PipelineModel | null
    }
  },
  data()
  {
    return {
      loading: false,
      title: null as string | null,
      configure: null as Configuration | null
    }
  },
  created()
  {
    if (this.info) {
      this.title = this.$t('pipeline.common.flowInfo').replace('$VALUE', this.info.name as string)

      if (!this.configure) {
        this.configure = ConfigurationRequest.of()
      }
      const flowConfigure = JSON.parse(this.info.flowConfigure)
      flowConfigure.nodes.forEach((node: any) => {
        this.configure?.elements?.push({ id: node.id, type: node.type, label: node.label, position: node.position })
      })
      flowConfigure.edges.forEach((edge: any) => {
        this.configure?.elements?.push({ id: edge.id, source: edge.source, target: edge.target })
      })
      this.configure.transform = flowConfigure?.viewport
      this.configure.fitViewOnInit = true
    }
  },
  methods: {
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>