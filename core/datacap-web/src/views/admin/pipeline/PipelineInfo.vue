<template>
  <div>
    <CircularLoading v-if="loading"
                     :show="loading">
    </CircularLoading>
    <FlowEditor v-else
                :data="contextData"
                @onCommit="handlerSave">
    </FlowEditor>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import FlowEditor from "@/components/editor/flow/FlowEditor.vue";
import {SourceService} from "@/services/SourceService";
import {Configuration} from "@/components/editor/flow/Configuration";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import PipelineService from "@/services/user/PipelineService";
import router from "@/router";

export default defineComponent({
  name: "PipelineInfo",
  components: {CircularLoading, FlowEditor},
  data()
  {
    return {
      loading: false,
      contextData: null as Configuration[]
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true;
      this.contextData = [];
      new SourceService()
        .getSources(1, 1000)
        .then((response) => {
          if (response.status) {
            response.data
              ?.content
              ?.filter((item: { pipelines: any; }) => item.pipelines != null)
              .forEach((item: { pipelines: any[]; id: number; name: string; type: string; protocol: string; }) => {
                item.pipelines.forEach((pipeline: { fields: any; type: string; }) => {
                  const configuration = new Configuration();
                  configuration.id = item.id;
                  configuration.name = item.name;
                  configuration.type = item.type;
                  configuration.nodeType = pipeline.type === 'INPUT' ? 'input' : 'output';
                  configuration.configure = pipeline.fields;
                  configuration.protocol = item.protocol;
                  this.contextData.push(configuration);
                })
              })
          }
        })
        .finally(() => {
          this.loading = false;
        })
    },
    handlerSave(value: any)
    {
      PipelineService.submit(value)
        .then((response) => {
          if (response.status) {
            this.$Message.success(`${this.$t('common.publish')} [ ${response.data} ] ${this.$t('common.success')}`)
            router.push('/admin/pipeline')
          }
          else {
            this.$Message.error(response.message)
          }
        })
    }
  }
});
</script>
<style scoped>
.content-center {
  text-align: center;
}
</style>
