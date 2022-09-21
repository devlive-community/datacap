<template>
  <a-modal v-model:visible="visible" :title="title" :footer="null" width="60%">
    <a-form :model="formState" :validate-messages="validateMessages" v-bind="layout" @finish="handlerSave($event)">
      <a-tabs v-model:activeKey="activeKey">
        <a-tab-pane key="type">
          <template #tab>
            <span>
              <appstore-outlined /> Type
            </span>
          </template>
          <a-form-item :name="['type']" :rules="[{ required: true }]">
            <a-row :gutter="[16,16]">
              <a-col :span="6" style="text-align: center;">
                <a-radio-group v-model:value="formState.type">
                  <a-radio-button value="MySQL">MySQL</a-radio-button>
                </a-radio-group>
              </a-col>
            </a-row>
          </a-form-item>
        </a-tab-pane>
        <a-tab-pane :disabled="!formState.type" key="configure">
          <template #tab>
            <span>
              <setting-outlined /> Configure
            </span>
          </template>
          <a-form-item :name="['name']" label="Name" :rules="[{ required: true }]">
            <a-input v-model:value="formState.name" />
          </a-form-item>
          <a-form-item :name="['protocol']" label="Protocol" :rules="[{ required: true }]">
            <a-select ref="select" v-model:value="formState.protocol" style="width: 120px">
              <a-select-option value="HTTP">HTTP</a-select-option>
              <a-select-option value="HTTPS">HTTPS</a-select-option>
              <a-select-option value="SSH">SSH</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item :name="['host']" label="Host" :rules="[{ required: true }]">
            <a-input v-model:value="formState.host" />
          </a-form-item>
          <a-form-item :name="['port']" label="Port" :rules="[{type: 'number', min: 0, max: 65535}]">
            <a-input-number v-model:value="formState.port" />
          </a-form-item>
        </a-tab-pane>
        <a-tab-pane :disabled="!formState.type" key="authorization">
          <template #tab>
            <span>
              <lock-outlined /> Authorization
            </span>
          </template>
          <a-form-item :name="['username']" label="UserName">
            <a-input v-model:value="formState.username" />
          </a-form-item>
          <a-form-item :name="['password']" label="Password">
            <a-input v-model:value="formState.password" />
          </a-form-item>
        </a-tab-pane>
        <a-tab-pane :disabled="!formState.type" key="advanced">
          <template #tab>
            <span>
              <group-outlined /> Advanced
            </span>
          </template>
          <a-form-item :name="['catalog']" label="Catalog">
            <a-input v-model:value="formState.catalog" />
          </a-form-item>
          <a-form-item :name="['database']" label="Database">
            <a-input v-model:value="formState.database" />
          </a-form-item>
        </a-tab-pane>
      </a-tabs>
      <a-form-item :wrapper-col="{ ...layout.wrapperCol, offset: 8 }">
        <a-space style="width: 100%">
          <a-button key="cancel" danger size="small" @click="handlerCancel()">Cancel</a-button>
          <a-button key="test" type="primary" size="small" @click="handlerTest()">Test</a-button>
          <a-button key="submit" type="primary" size="small" :disabled="isCreated" html-type="submit">Save</a-button>
        </a-space>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from "vue";
import { SourceModel } from "@/model/SourceModel";
import { SourceService } from "@/services/SourceService";
import { message } from "ant-design-vue";
import { emptySource } from "@/views/pages/admin/source/SourceGenerate";

export default defineComponent({
  name: "SourceInfoView",
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
  setup() {
    const layout = {
      labelCol: { span: 6 },
      wrapperCol: { span: 12 },
    };
    const validateMessages = {
      required: '${label} is required!',
      number: {
        range: '${label} must be between ${min} and ${max}',
      },
    };
    const formState = reactive(emptySource);
    return {
      activeKey: ref('type'),
      validateMessages,
      formState,
      layout
    };
  },
  components: {},
  data() {
    return {
      title: '',
      isCreated: true
    }
  },
  created() {
    if (this.id <= 0) {
      this.title = 'Create New Source'
    }
    else {
      this.title = 'Modify Source'
    }
  },
  methods: {
    handlerCancel() {
      this.visible = false;
    },
    handlerSave(values: SourceModel) {
      new SourceService()
        .saveAndUpdate(values)
        .then((response) => {
          if (response.status) {
            message.success("Create successful");
            this.visible = false;
          }
        });
    },
    handlerTest() {
      new SourceService()
        .testConnection(this.formState)
        .then((response) => {
          if (response.status) {
            message.success("Test successful");
            this.isCreated = false;
          } else {
            message.error(response.message);
          }
        });
    }
  },
  computed: {
    visible: {
      get(): boolean {
        return this.isVisible
      },
      set(value: boolean) {
        this.$emit('close', value)
      }
    }
  }
});
</script>
