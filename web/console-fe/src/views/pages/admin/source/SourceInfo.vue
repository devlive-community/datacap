<template>
  <a-modal v-model:visible="visible" :title="title" :footer="null" width="60%" :closable="false">
    <a-row :gutter="16">
      <a-col :span="4">
      </a-col>
      <a-col :span="5">
        <a-card :bordered="false" style="text-align: center" :bodyStyle="{'padding': '5px'}">
          <a-card-meta>
            <template #title>
              <a-avatar :size="40" :style="{'background-color': !testInfo.percent ? '#CCC' : testInfo.connected ? '#52c41a' : '#ff4d4f'}">
                <template #icon>
                  <question-circle-outlined/>
                </template>
              </a-avatar>
            </template>
            <template #description>{{ !formState['type'] ? '_' : formState['type'] }}</template>
          </a-card-meta>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card :bordered="false" style="text-align: center" :bodyStyle="{'padding': '5px'}">
          <a-card-meta>
            <template #description>
              <a-progress :percent="testInfo.percent" :show-info="false"
                          :status="testInfo.connected ? 'success' : 'exception'">
              </a-progress>
            </template>
          </a-card-meta>
        </a-card>
      </a-col>
      <a-col :span="5">
        <a-card :bordered="false" style="text-align: center" :bodyStyle="{'padding': '5px'}">
          <a-card-meta>
            <template #title>
              <a-avatar :size="40" :style="{'background-color': !testInfo.percent ? '#CCC' : testInfo.connected ? '#52c41a' : '#ff4d4f'}">
                <template #icon>
                  <database-outlined/>
                </template>
              </a-avatar>
            </template>
            <template #description>Database</template>
          </a-card-meta>
        </a-card>
      </a-col>
      <a-col :span="3">
      </a-col>
    </a-row>
    <a-form :model="formState" :validate-messages="validateMessages" v-bind="layout" @finish="handlerSave()">
      <a-tabs v-model:activeKey="activeKey" centered>
        <a-tab-pane key="type" style="text-align: center;">
          <template #tab>
            <span>
              <appstore-outlined/> Type
            </span>
          </template>
          <a-radio-group v-model:value="formState.type">
            <a-row :gutter="[16,8]">
              <a-col v-for="plugin in plugins" :span="6" v-bind:key="plugin.name">
                <a-radio-button :value="plugin.name">
                  {{ plugin.name }}
                </a-radio-button>
              </a-col>
            </a-row>
          </a-radio-group>
        </a-tab-pane>
        <a-tab-pane :disabled="!formState.type" key="configure">
          <template #tab>
            <span>
              <setting-outlined/> Configure
            </span>
          </template>
          <a-form-item :name="['name']" label="Name" :rules="[{ required: true }]">
            <a-input v-model:value="formState.name"/>
          </a-form-item>
          <!-- <a-form-item :name="['protocol']" label="Protocol" :rules="[{ required: true }]">
            <a-select ref="select" v-model:value="formState.protocol" style="width: 120px">
              <a-select-option value="HTTP">HTTP</a-select-option>
              <a-select-option value="HTTPS">HTTPS</a-select-option>
              <a-select-option value="SSH">SSH</a-select-option>
            </a-select>
          </a-form-item> -->
          <a-form-item :name="['host']" label="Host" :rules="[{ required: true }]">
            <a-input v-model:value="formState.host"/>
          </a-form-item>
          <a-form-item :name="['port']" label="Port" :rules="[{type: 'number', min: 0, max: 65535}]">
            <a-input-number v-model:value="formState.port"/>
          </a-form-item>
        </a-tab-pane>
        <a-tab-pane :disabled="!formState.type" key="authorization">
          <template #tab>
            <span>
              <lock-outlined/> Authorization
            </span>
          </template>
          <a-form-item :name="['username']" label="UserName">
            <a-input v-model:value="formState.username"/>
          </a-form-item>
          <a-form-item :name="['password']" label="Password">
            <a-input v-model:value="formState.password"/>
          </a-form-item>
        </a-tab-pane>
        <a-tab-pane :disabled="!formState.type" key="advanced">
          <template #tab>
            <span>
              <group-outlined/> Advanced
            </span>
          </template>
          <a-form-item :name="['catalog']" label="Catalog">
            <a-input v-model:value="formState.catalog"/>
          </a-form-item>
          <a-form-item :name="['database']" label="Database">
            <a-input v-model:value="formState.database"/>
          </a-form-item>
        </a-tab-pane>
      </a-tabs>
      <a-form-item :wrapper-col="{ ...layout.wrapperCol, offset: 8 }" style="margin-top: 20px; margin-bottom: -5px;">
        <a-space style="width: 100%">
          <a-button key="cancel" danger size="small" :disabled="connectionLoading" @click="handlerCancel()">Cancel
          </a-button>
          <a-button key="test" type="primary" size="small" :loading="connectionLoading" @click="handlerTest()">Test
          </a-button>
          <a-button key="submit" type="primary" size="small" :disabled="!testInfo.connected" html-type="submit">Save</a-button>
        </a-space>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {SourceModel} from "@/model/SourceModel";
import {SourceService} from "@/services/SourceService";
import {emptySource} from "@/views/pages/admin/source/SourceGenerate";
import {message} from "ant-design-vue";
import {defineComponent, reactive, ref} from "vue";

interface TestInfo
{
  connected: boolean,
  percent: number
}

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
  setup()
  {
    const layout = {
      labelCol: {span: 6},
      wrapperCol: {span: 12},
    };
    const validateMessages = {
      required: '${label} is required!',
      number: {
        range: '${label} must be between ${min} and ${max}',
      },
    };

    return {
      activeKey: ref('type'),
      validateMessages,
      layout
    };
  },
  components: {},
  data()
  {
    return {
      title: '',
      isUpdate: false,
      formState: {},
      plugins: [],
      testInfo: {} as TestInfo,
      connectionLoading: false,
    }
  },
  created()
  {
    if (this.id <= 0) {
      this.title = 'Create New Source';
      this.formState = reactive(emptySource);
    }
    else {
      this.title = 'Modify Source';
      this.isUpdate = true;
    }
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      if (this.id > 0) {
        new SourceService().getById(this.id)
          .then(response => {
            if (response.status) {
              this.formState = reactive(response.data);
            }
          });
      }
      new SourceService().getPlugins()
        .then(response => {
          if (response.status) {
            this.plugins = response.data;
          }
        });
    },
    handlerCancel()
    {
      this.visible = false;
    },
    handlerSave()
    {
      new SourceService()
        .saveAndUpdate(this.formState as SourceModel, this.isUpdate)
        .then((response) => {
          if (response.status) {
            message.success("Create successful");
            this.visible = false;
          }
        });
    },
    handlerTest()
    {
      this.connectionLoading = true;
      new SourceService()
        .testConnection(this.formState as SourceModel)
        .then((response) => {
          this.testInfo.percent = 100;
          if (response.status) {
            message.success("Test successful");
            this.testInfo.connected = true;
          }
          else {
            message.error(response.message);
            this.testInfo.connected = false;
          }
        })
        .finally(() => {
          this.connectionLoading = false;
        });
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
  }
});
</script>
