<template>
  <div>
    <Drawer :title="$t('common.create') + $t('common.function')" :width="720" :closable="false"
            v-model="visible" :maskClosable="false" :z-index="9" :styles="{}">
      <Form :model="formState">
        <Row :gutter="32">
          <Col span="12">
            <FormItem :label="$t('common.name')">
              <Input v-model="formState.name"/>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem :label="$t('common.plugin')">
              <Select v-model="formState.plugin" multiple max-tag-count="3">
                <OptionGroup v-for="key in Object.keys(plugins)" v-bind:key="key" :label="key">
                  <Option v-for="plugin in plugins[key]" :value="plugin.name" :key="plugin.name">{{ plugin.name }}</Option>
                </OptionGroup>
              </Select>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="32">
          <Col span="12">
            <FormItem :label="$t('common.content')" name="content" label-position="top">
              <Input v-model="formState.content" show-word-limit type="textarea" :rows="3"/>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem :label="$t('common.description')" label-position="top">
              <Input type="textarea" v-model="formState.description" show-word-limit :rows="3"/>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="32">
          <Col span="12">
            <FormItem :label="$t('common.type')" name="type" label-position="top">
              <Select v-model="formState.type">
                <Option v-for="item in types" :value="item.value" :key="item.value">{{ item.label }}</Option>
              </Select>
            </FormItem>
          </Col>
        </Row>
        <FormItem :label="$t('common.example')" label-position="top">
          <Input type="textarea" v-model="formState.example" :rows="5"/>
        </FormItem>
      </Form>
      <div class="datacap-drawer-footer">
        <Button :disabled="created" style="margin-right: 8px" @click="handlerCancel()">{{ $t('common.cancel') }}</Button>
        <Button type="primary" :loading="created" @click="handlerSave()">{{ $t('common.submit') }}</Button>
      </div>
      <Spin size="large" fix :show="loading"></Spin>
    </Drawer>
  </div>
</template>
<script lang="ts">
import {defineComponent, reactive} from 'vue';
import {SourceService} from "@/services/SourceService";
import {Functions} from "@/model/settings/functions/Functions";
import {createDefaultType, emptyEntity} from "@/views/pages/admin/settings/functions/FunctionsGenerate";
import FunctionsService from "@/services/settings/functions/FunctionsService";
import {useI18n} from "vue-i18n";

export default defineComponent({
  name: 'FunctionsDetails',
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
      formState: null as unknown as Functions,
      loading: false,
      created: false,
      plugins: []
    }
  },
  created()
  {
    this.formState = reactive<Functions>(emptyEntity);
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
        FunctionsService.getById(this.id)
          .then(response => {
            if (response.status) {
              this.formState = reactive<Functions>(response.data);
            }
          })
          .finally(() => {
            this.loading = false;
          });
      }
    },
    handlerSave()
    {
      this.created = true;
      FunctionsService.saveAndUpdate(this.formState, this.isUpdate)
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
