<template>
  <div>
    <Drawer :title="$t('common.import') + $t('common.function')" :width="720" :closable="false"
            v-model="visible" :maskClosable="false" :z-index="9" :styles="{}">
      <Form :model="formState">
        <Tabs v-model="formState.mode" :animated="false">
          <TabPane :label="$t('common.content')" :name="FunctionsImportMode.txt">
            <FormItem :label="$t('common.content')" label-position="top">
              <Input type="textarea" show-word-limit v-model="formState.content" :placeholder="$t('tooltip.multipleLines')" :rows="12"/>
            </FormItem>
          </TabPane>
          <TabPane :label="$t('common.url')" :name="FunctionsImportMode.url">
            <Alert banner type="warning">
              {{ $t('alert.urlMode') }}
              <DescriptionList :col="1" style="margin-top: 20px;">
                <Description :term="$t('common.keyword')">
                  &nbsp;&nbsp;(http|https)://datacap.edurt.io/resources/functions/plugin/keywords.txt
                </Description>
                <Description :term="$t('common.operator')">
                  &nbsp;&nbsp;(http|https)://datacap.edurt.io/resources/functions/plugin/operators.txt
                </Description>
                <Description :term="$t('common.function')">
                  &nbsp;&nbsp;(http|https)://datacap.edurt.io/resources/functions/plugin/functions.txt
                </Description>
              </DescriptionList>
            </Alert>
            <FormItem :label="$t('common.content')" label-position="top">
              <Input v-model="formState.content"/>
            </FormItem>
          </TabPane>
        </Tabs>
        <Row :gutter="32">
          <Col span="12">
            <FormItem :label="$t('common.plugin')">
              <Select v-model="formState.plugin" multiple max-tag-count="3">
                <OptionGroup v-for="key in Object.keys(plugins)" v-bind:key="key" :label="key">
                  <Option v-for="plugin in plugins[key]" :value="plugin.name" :key="plugin.name">{{ plugin.name }}</Option>
                </OptionGroup>
              </Select>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem :label="$t('common.type')" name="type" label-position="top">
              <Select v-model="formState.type">
                <Option v-for="item in types" :value="item.value" :key="item.value">{{ item.label }}</Option>
              </Select>
            </FormItem>
          </Col>
        </Row>
      </Form>
      <div class="datacap-drawer-footer">
        <Button :disabled="created" style="margin-right: 8px" @click="handlerCancel()">{{ $t('common.cancel') }}</Button>
        <Button type="primary" :loading="created" @click="handlerImport()">{{ $t('common.submit') }}</Button>
      </div>
      <Spin size="large" fix :show="loading"></Spin>
    </Drawer>
  </div>
</template>
<script lang="ts">
import {defineComponent, reactive} from 'vue';
import {SourceService} from "@/services/SourceService";
import {createDefaultType, emptyImportEntity} from "@/views/system/function/GenerateFunction";
import FunctionsService from "@/services/settings/functions/FunctionsService";
import {useI18n} from "vue-i18n";
import {FunctionsImport} from "@/model/settings/functions/FunctionsImport";
import {FunctionsImportMode} from "@/enum/FunctionsImportMode";

export default defineComponent({
  name: 'ImportFunction',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    }
  },
  setup()
  {
    const i18n = useI18n();
    const types = createDefaultType(i18n);
    return {
      types
    }
  },
  data()
  {
    return {
      isUpdate: false,
      formState: null as unknown as FunctionsImport,
      loading: false,
      created: false,
      plugins: []
    }
  },
  created()
  {
    this.formState = reactive<FunctionsImport>(emptyImportEntity);
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
    },
    handlerImport()
    {
      this.created = true;
      FunctionsService.import(this.formState)
        .then((response) => {
          if (response.status) {
            this.$Message.success("Create successful");
            this.visible = false;
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => {
          this.created = false;
        });
    },
    handlerCancel()
    {
      this.visible = false;
    }
  },
  computed: {
    FunctionsImportMode()
    {
      return FunctionsImportMode
    },
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
