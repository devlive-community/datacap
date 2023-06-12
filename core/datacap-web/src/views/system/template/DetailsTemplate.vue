<template>
  <div>
    <Drawer :title="$t('common.create') + $t('common.template')" :width="720" :closable="false"
            v-model="visible" :maskClosable="false" :z-index="9" :styles="{}">
      <Form :model="formState">
        <Row :gutter="32">
          <Col span="12">
            <FormItem :label="$t('common.name')">
              <Input :disabled="formState.system" v-model="formState.name"/>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem :label="$t('common.plugin')">
              <Select v-model="formState.plugin" multiple>
                <OptionGroup v-for="key in Object.keys(plugins)" v-bind:key="key" :label="key">
                  <Option v-for="plugin in plugins[key]" :value="plugin.name" :key="plugin.name">{{ plugin.name }}</Option>
                </OptionGroup>
              </Select>
            </FormItem>
          </Col>
        </Row>
        <FormItem :label="$t('common.description')" label-position="top">
          <Input type="textarea" v-model="formState.description" :rows="4"/>
        </FormItem>
        <FormItem :label="$t('common.template')" name="content" label-position="top">
          <Input v-model="formState.content" show-word-limit type="textarea" :rows="8"/>
        </FormItem>
      </Form>
      <div class="datacap-drawer-footer">
        <Button style="margin-right: 8px" @click="handlerCancel()">{{ $t('common.cancel') }}</Button>
        <Button type="primary" @click="handlerSave()">{{ $t('common.submit') }}</Button>
      </div>
      <Spin size="large" fix :show="loading"></Spin>
    </Drawer>
  </div>
</template>
<script lang="ts">
import {defineComponent, reactive} from 'vue';
import {TemplateSql} from "@/model/template/Sql";
import {emptyEntity} from "@/views/system/template/GenerateTemplate";
import TemplateSqlService from "@/services/template/TemplateSqlService";
import {SourceService} from "@/services/SourceService";
import {clone} from "lodash";

export default defineComponent({
  name: 'TemplateSqlDetails',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    id: {
      type: Number,
      default: () => 0
    }
  },
  data()
  {
    return {
      isUpdate: false,
      formState: null as unknown as TemplateSql,
      loading: false,
      plugins: []
    }
  },
  created()
  {
    this.formState = reactive<TemplateSql>(emptyEntity);
    this.isUpdate = this.id === 0 ? false : true;
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      new SourceService().getPlugins()
        .then(response => {
          if (response.status) {
            this.plugins = response.data;
          }
        });
      if (this.id > 0) {
        this.loading = true;
        TemplateSqlService.getById(this.id)
          .then(response => {
            if (response.status) {
              const data = response.data;
              data.plugin = data.plugin.split(',');
              this.formState = reactive<TemplateSql>(data);
            }
          })
          .finally(() => {
            this.loading = false;
          });
      }
    },
    handlerSave()
    {
      const entity = clone(this.formState);
      entity.plugin = this.formState.plugin.join(',');
      TemplateSqlService.saveAndUpdate(entity, this.isUpdate)
        .then((response) => {
          if (response.status) {
            this.$Message.success("Create successful");
            this.visible = false;
          }
          else {
            this.$Message.error(response.message);
          }
        });
    },
    handlerCancel()
    {
      this.visible = false;
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
