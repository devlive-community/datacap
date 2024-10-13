<template>
  <div class="w-full h-screen">
    <DataCapCard>
      <template #title>{{ $t('pipeline.common.create') }}</template>
      <CircularLoading v-if="loading" :show="loading"/>
      <FlowEditor v-else :data="contextData" @onCommit="handlerSave"/>
    </DataCapCard>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { DataCapCard } from '@/views/ui/card'
import FlowEditor from '@/views/components/editor/flow/FlowEditor.vue'
import SourceService from '@/services/source'
import PipelineService from '@/services/pipeline'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { Configuration } from '@/views/components/editor/flow/Configuration.ts'
import { ToastUtils } from '@/utils/toast.ts'
import router from '@/router'
import { FilterModel } from '@/model/filter.ts'

export default defineComponent({
  name: 'PipelineInfo',
  components: {
    CircularLoading,
    FlowEditor,
    DataCapCard
  },
  data()
  {
    return {
      loading: false,
      contextData: [] as Configuration[]
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      this.contextData = []
      const filter = new FilterModel()
      SourceService.getAll(filter)
                   .then((response) => {
                     if (response.status && response.data) {
                       response.data.content.filter((item: any) => item.pipelines)
                               .flatMap((item: any) => item.pipelines.map((pipeline: any) => ({
                                 id: item.id,
                                 name: item.name,
                                 type: item.type,
                                 nodeType: pipeline.type === 'INPUT' ? 'input' : 'output',
                                 configure: pipeline.fields,
                                 protocol: item.protocol
                               })))
                               .forEach((configuration: Configuration) => this.contextData.push(configuration))
                     }
                   })
                   .finally(() => this.loading = false)
    },
    handlerSave(value: any)
    {
      PipelineService.submit(value)
                     .then((response) => {
                       if (response.status) {
                         ToastUtils.success(`${ this.$t('pipeline.tip.publishSuccess').replace('$VALUE', response.data) }`)
                         router.push('/admin/pipeline')
                       }
                       else {
                         ToastUtils.error(response.message)
                       }
                     })
    }
  }
})
</script>
