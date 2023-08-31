<template>
  <div>
    <Modal v-model="visible"
           :title="$t('common.create') + $t('common.pipeline')"
           :width="'70%'"
           :closable="false"
           :maskClosable="false"
           :styles="{}">
      <Form :model="formState"
            :show-message="false"
            :label-width="80">
        <Tabs :animated="false"
              style="min-height: 200px;">
          <TabPane icon="md-appstore"
                   style="height: 100%;"
                   :label="$t('common.from')">
            <FormItem :label="$t('common.source')"
                      label-position="top">
              <Select v-model="formState.from.source"
                      style="width:200px"
                      @on-change="handlerSourceChange('SOURCE')">
                <Option v-for="item in sourceItems"
                        :value="item"
                        :key="item.id">
                  {{ item.name }}
                </Option>
              </Select>
            </FormItem>
            <FormItem v-if="pipelineFromItem"
                      :label="$t('common.configure')"
                      label-position="top">
              <div v-for="item in pipelineFromItem[0]['fields']"
                   :key="item"
                   style="margin-top: 10px;">
                <FormItem v-if="item.input"
                          :required="item.required"
                          :prop="item.field"
                          :label-width="100"
                          :label="item.field">
                  <Row>
                    <Col span="18">
                      <Input v-model="item.value">
                      </Input>
                    </Col>
                  </Row>
                </FormItem>
              </div>
            </FormItem>
          </TabPane>
          <TabPane icon="md-appstore"
                   :label="$t('common.to')">
            <FormItem :label="$t('common.source')"
                      label-position="top">
              <Select v-model="formState.to.source"
                      style="width:200px"
                      @on-change="handlerSourceChange('SINK')">
                <Option v-for="item in sourceItems"
                        :value="item"
                        :key="item.id">
                  {{ item.name }}
                </Option>
              </Select>
            </FormItem>
            <FormItem v-if="pipelineToItem"
                      :label="$t('common.configure')"
                      label-position="top">
              <div v-for="item in pipelineToItem[0]['fields']"
                   :key="item"
                   style="margin-top: 10px;">
                <FormItem v-if="item.input"
                          :required="item.required"
                          :prop="item.field"
                          :label-width="100"
                          :label="item.field">
                  <Row>
                    <Col span="18">
                      <Input v-model="item.value">
                      </Input>
                    </Col>
                  </Row>
                </FormItem>
              </div>
            </FormItem>
          </TabPane>
        </Tabs>
      </Form>
      <template #footer>
        <Button style="margin-right: 8px"
                @click="handlerCancel()">
          {{ $t('common.cancel') }}
        </Button>
        <Button type="primary"
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
import {ObjectCommon} from '@/common/ObjectCommon';
import {clone} from "lodash";

const source = 'SOURCE';
const sink = 'SINK';

export default defineComponent({
  name: 'DetailsPipeline',
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
      pipelineFromItem: null,
      pipelineToItem: null,
      formState: null as PipelineModel
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
            this.sourceItems = response.data.content
              .filter((item: { pipelines: any; }) => {
                return item.pipelines != null
              })
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
        }
      }
      if (type === sink) {
        const pipelines = this.formState.to.source['pipelines']
        if (pipelines) {
          this.pipelineToItem = this.filterPipelineFromItem(pipelines, sink)
        }
      }
    },
    handlerSave()
    {
      this.formState.from.id = this.formState.from.source.id
      this.formState.from.configures = this.filterConfigure(this.formState.from, source)
      this.formState.from.source = null

      this.formState.to.id = this.formState.to.source.id
      this.formState.to.configures = this.filterConfigure(this.formState.to, sink);
      this.formState.to.source = null
      console.log(this.formState)
      this.handlerCancel()
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
    filterConfigure(meta: PipelineMetaModel, type: string)
    {
      return meta.source['pipelines']
        .filter((item: { type: string; }) => item.type === type)[0]['fields']
        .filter((item: { input: any; }) => item.input)
        .map((item: { origin: any; field: any; value: any; }) => ObjectCommon.getDynamicObject(item.origin ? item.origin : item.field, item.value))
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
<style scoped>
.datacap-drawer-footer {
  width: 100%;
  position: absolute;
  bottom: 0;
  left: 0;
  border-top: 1px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  background: #fff;
}
</style>
