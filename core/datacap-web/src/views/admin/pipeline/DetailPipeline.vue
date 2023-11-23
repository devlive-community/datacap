<template>
  <div>
    <Modal v-model="visible"
           :title="$t('common.create') + $t('common.pipeline')"
           :width="'60%'"
           :closable="false"
           :maskClosable="false"
           :styles="{}">
      <Form :model="formState"
            :show-message="false"
            :label-width="100">
        <Alert v-if="errors"
               type="error">
          <p v-for="error in String(errors).split('<br/>')"
             :key="error">
            {{ error }}
          </p>
        </Alert>
        <Tabs :animated="false"
              style="min-height: 200px;">
          <TabPane icon="md-cafe"
                   style="height: 100%;"
                   :label="$t('common.description')">
            <FormItem :label="$t('common.sql')"
                      required
                      label-position="top">
              <Input v-model="formState.content"
                     type="textarea"
                     show-word-limit
                     :rows="16">
              </Input>
            </FormItem>
          </TabPane>
          <TabPane icon="md-appstore"
                   style="height: 100%;"
                   :label="$t('common.from')">
            <FormItem :label="$t('common.source')"
                      required
                      label-position="top">
              <Select v-model="formState.from.source"
                      style="width:200px"
                      transfer
                      @on-change="handlerSourceChange('SOURCE')">
                <Option v-for="item in sourceItems"
                        :value="item"
                        :key="item.id">
                  {{ item.name }}
                </Option>
              </Select>
            </FormItem>
            <FormItemPropertyPipeline v-if="pipelineFromItem"
                                      :pipeline-from-items="pipelineFromItem[0]['fields']">
            </FormItemPropertyPipeline>
          </TabPane>
          <TabPane icon="md-medkit"
                   :label="$t('common.to')">
            <FormItem :label="$t('common.source')"
                      required
                      label-position="top">
              <Select v-model="formState.to.source"
                      style="width:200px"
                      transfer
                      @on-change="handlerSourceChange('SINK')">
                <Option v-for="item in sinkItems"
                        :value="item"
                        :key="item.id">
                  {{ item.name }}
                </Option>
              </Select>
            </FormItem>
            <FormItemPropertyPipeline v-if="pipelineToItem"
                                      :pipeline-from-items="pipelineToItem[0]['fields']">
            </FormItemPropertyPipeline>
          </TabPane>
        </Tabs>
      </Form>
      <template #footer>
        <Button style="margin-right: 8px"
                :disabled="submitted"
                @click="handlerCancel()">
          {{ $t('common.cancel') }}
        </Button>
        <Button type="primary"
                :loading="submitted"
                :disabled="!(formState.from.source?.id && formState.to.source?.id && formState.content)"
                @click="handlerSave()">
          {{ $t('common.submit') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {PipelineMetaModel, PipelineModel} from '@/model/Pipeline';
import {defineComponent} from 'vue';
import {SourceService} from "@/services/SourceService";
import {clone, cloneDeep} from "lodash";
import FormItemPropertyPipeline from "@/views/admin/pipeline/components/FormItemPropertyPipeline.vue";
import PipelineService from '@/services/user/PipelineService';

const source = 'SOURCE';
const sink = 'SINK';

export default defineComponent({
  name: 'DetailsPipeline',
  components: {FormItemPropertyPipeline},
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    }
  },
  data()
  {
    return {
      loading: false,
      sourceItems: [],
      sinkItems: [],
      pipelineFromItem: null,
      pipelineToItem: null,
      formState: null as PipelineModel,
      submitted: false,
      errors: null
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      if (!this.formState) {
        this.formState = new PipelineModel()
      }
      // Loading source
      this.loading = true;
      new SourceService()
        .getSources(1, 1000)
        .then((response) => {
          if (response.status) {
            this.sourceItems = this.filterPipeline(response.data.content, source)
            this.sinkItems = this.filterPipeline(response.data.content, sink)
          }
        })
        .finally(() => {
          this.loading = false;
        })
    },
    handlerSourceChange(type: string)
    {
      if (type === source) {
        const pipelines = this.formState.from.source['pipelines']
        if (pipelines) {
          this.pipelineFromItem = this.filterPipelineFromItem(pipelines, source)
          this.formState.from.protocol = this.pipelineFromItem[0]['protocol']
        }
      }
      if (type === sink) {
        const pipelines = this.formState.to.source['pipelines']
        if (pipelines) {
          this.pipelineToItem = this.filterPipelineFromItem(pipelines, sink)
          this.formState.to.protocol = this.pipelineToItem[0]['protocol']
        }
      }
    },
    handlerSave()
    {
      this.submitted = true
      this.formState.from.id = this.formState.from.source.id
      this.formState.from.configures = this.filterConfigure(this.formState.from, source)

      this.formState.to.id = this.formState.to.source.id
      this.formState.to.configures = this.filterConfigure(this.formState.to, sink);

      PipelineService.submit(cloneDeep(this.formState))
        .then((response) => {
          if (response.status) {
            this.$Message.success(`${this.$t('common.publish')} [ ${response.data} ] ${this.$t('common.success')}`)
            this.handlerCancel()
          }
          else {
            this.errors = response.message
          }
        })
        .finally(() => {
          this.submitted = false
        })
    },
    handlerCancel()
    {
      this.visible = false;
    },
    /**
     * Filters the given pipelines array based on the specified type.
     *
     * @param {Array} pipelines - The array of pipelines to filter.
     * @param {string} type - The type to filter the pipelines by.
     * @return {Array} - The filtered array of pipelines.
     */
    filterPipelineFromItem(pipelines: [], type: string): Array<any>
    {
      return clone(pipelines.filter((item: { type: string; }) => item.type === type))
    },
    filterPipeline(pipelines: [], type: string): Array<any>
    {
      const array = pipelines.filter((item: { pipelines: any; }) => item.pipelines != null)
        .filter((item: { pipelines: any; }) => item.pipelines.filter((item: { type: string; }) => item.type === type).length > 0)
      return clone(array)
    },
    filterConfigure(meta: PipelineMetaModel, type: string)
    {
      const result = {};
      meta.source['pipelines']
        .filter((item: { type: string; }) => item.type === type)[0]['fields']
        .filter((item: { input: any; }) => item.input)
        .forEach((item: { origin: any; value: any; }) => {
          const key = item.origin;
          const value = item.value;
          if (value !== null) {
            result[key] = value;
          }
        })
      return result
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
});
</script>
