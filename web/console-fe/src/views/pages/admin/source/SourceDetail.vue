<template>
  <div>
    <Modal v-model="visible" :title="title" :footer="null" width="70%" :closable="false" :mask-closable="false">
      <Row :gutter="16">
        <Col :span="4"/>
        <Col :span="5">
          <Card :bordered="false" dis-hover>
            <div style="text-align:center">
              <Avatar :size="40" :style="{'background-color': !testInfo.percent ? '#CCC' : testInfo.connected ? '#52c41a' : '#ff4d4f'}" icon="ios-person"/>
              <p>{{ !formState['type'] ? '_' : formState['type'] }}</p>
            </div>
          </Card>
        </Col>
        <Col :span="6">
          <Card :bordered="false" dis-hover>
            <div style="text-align:center">
              <Progress :percent="testInfo.percent" :status="testInfo.connected ? 'success' : 'wrong'">
                <span></span>
              </Progress>
            </div>
          </Card>
        </Col>
        <Col :span="5">
          <Card :bordered="false" dis-hover>
            <div style="text-align:center">
              <Avatar :size="40" :style="{'background-color': !testInfo.percent ? '#CCC' : testInfo.connected ? '#52c41a' : '#ff4d4f'}" icon="ios-cube"/>
              <p>{{ $t('common.source') }}</p>
            </div>
          </Card>
        </Col>
        <Col :span="3"/>
      </Row>
      <Form :model="formState" :label-width="80">
        <Tabs v-model="activeKey">
          <TabPane :label="$t('common.source')" name="type" icon="md-apps">
            <RadioGroup v-if="plugins" v-model="formState.type" type="button">
              <div v-for="key in Object.keys(plugins)" v-bind:key="key">
                <Divider orientation="left">{{ key }} ({{ plugins[key].length }})</Divider>
                <Space wrap :size="[8, 16]">
                  <Tooltip v-for="plugin in plugins[key]" :content="plugin.description" transfer v-bind:key="plugin.name">
                    <Radio :label="plugin.name + ' ' + plugin.type"/>
                  </Tooltip>
                </Space>
              </div>
            </RadioGroup>
          </TabPane>
          <TabPane :disabled="!formState.type" name="configure" :label="$t('common.configure')" icon="md-cog">
            <div style="margin-top: 10px;">
              <Row>
                <Col :span="5"/>
                <Col :span="14">
                  <FormItem :label="$t('common.name')" prop="name">
                    <Input type="text" v-model="formState.name"/>
                  </FormItem>
                  <FormItem :label="$t('common.host')" prop="host">
                    <Input type="text" v-model="formState.host"/>
                  </FormItem>
                  <FormItem :label="$t('common.port')" prop="port">
                    <InputNumber :max="65535" :min="1" v-model="formState.port"/>
                  </FormItem>
                </Col>
                <Col :span="5"/>
              </Row>
            </div>
          </TabPane>
          <TabPane :disabled="!formState.type" name="authorization" :label="$t('common.authorization')" icon="md-lock">
            <div style="margin-top: 10px;">
              <Row>
                <Col :span="5"/>
                <Col :span="14">
                  <FormItem :label="$t('common.username')" prop="username">
                    <Input type="text" v-model="formState.username"/>
                  </FormItem>
                  <FormItem :label="$t('common.password')" prop="password">
                    <Input type="password" v-model="formState.password"/>
                  </FormItem>
                  <FormItem label="SSL" prop="ssl">
                    <Switch v-model="formState.ssl"/>
                  </FormItem>
                </Col>
                <Col :span="5"/>
              </Row>
            </div>
          </TabPane>
          <TabPane :disabled="!formState.type" name="advanced" :label="$t('common.advanced')" icon="md-archive">
            <div style="margin-top: 10px;">
              <Row>
                <Col :span="5"/>
                <Col :span="14">
                  <FormItem :label="$t('common.catalog')" prop="catalog">
                    <Input type="text" v-model="formState.catalog"/>
                  </FormItem>
                  <FormItem :label="$t('common.database')" prop="database">
                    <Input type="text" v-model="formState.database"/>
                  </FormItem>
                </Col>
                <Col :span="5"/>
              </Row>
            </div>
          </TabPane>
          <TabPane :disabled="!formState.type" name="custom" :label="$t('common.custom')" icon="md-hammer">
            <div style="margin-top: 10px;">
              <FormItem>
                <Button size="small" type="primary" shape="circle" icon="md-add" @click="handlerPlusConfigure()"/>
              </FormItem>
              <FormItem v-for="(element, index) in configure" :key="index" style="margin-bottom: 5px;">
                <Row :gutter="12">
                  <Col :span="10">
                    <FormItem>
                      <Input v-model="element.field">
                        <template #prepend>
                          <span>{{ $t('common.field') }}</span>
                        </template>
                      </Input>
                    </FormItem>
                  </Col>
                  <Col :span="10">
                    <FormItem>
                      <Input v-model="element.value">
                        <template #prepend>
                          <span>{{ $t('common.value') }}</span>
                        </template>
                      </Input>
                    </FormItem>
                  </Col>
                  <Col :span="2">
                    <Button size="small" type="error" shape="circle" icon="md-remove" @click="handlerMinusConfigure(element)"/>
                  </Col>
                </Row>
              </FormItem>
            </div>
          </TabPane>
        </Tabs>
      </Form>
      <template #footer>
        <Button key="cancel" type="error" size="small" :disabled="connectionLoading" @click="handlerCancel()">
          {{ $t('common.cancel') }}
        </Button>
        <Button key="test" type="primary" size="small" :loading="connectionLoading" @click="handlerTest()">
          {{ $t('common.test') }}
        </Button>
        <Button key="submit" type="primary" size="small" :disabled="!testInfo.connected" @click="handlerSave()">
          {{ $t('common.save') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {SourceModel} from "@/model/SourceModel";
import {SourceService} from "@/services/SourceService";
import {emptySource} from "@/views/pages/admin/source/SourceGenerate";
import {defineComponent, reactive, ref} from "vue";
import {Configure} from "@/model/Configure";
import {Arrays} from "@/common/Arrays";
import {clone} from 'lodash'

interface TestInfo
{
  connected: boolean,
  percent: number
}

export default defineComponent({
  name: "SourceDetail",
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
      formState: {} as SourceModel,
      plugins: null,
      testInfo: {} as TestInfo,
      connectionLoading: false,
      configure: [] as Configure[]
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
              this.formState.type = this.formState.type + ' ' + this.formState.protocol;
              if (response.data.configures) {
                Object.keys(response.data.configures).forEach((value) => {
                  const configure: Configure = {
                    field: value,
                    value: response.data.configures[value]
                  };
                  this.configure.push(configure);
                });
              }
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
    handlerImport()
    {
      this.formState.configures = Arrays.arrayToObject(this.configure);
      const applyConfigure = clone(this.formState);
      const temp = clone(this.formState.type).split(' ');
      applyConfigure.type = temp[0];
      applyConfigure.protocol = temp[1];
      new SourceService()
        .saveAndUpdate(applyConfigure, this.isUpdate)
        .then((response) => {
          if (response.status) {
            this.$Message.success("Create successful");
            this.visible = false;
          }
        });
    },
    handlerTest()
    {
      this.connectionLoading = true;
      this.formState.configures = Arrays.arrayToObject(this.configure);
      const applyConfigure = clone(this.formState);
      const temp = clone(this.formState.type).split(' ');
      applyConfigure.type = temp[0];
      applyConfigure.protocol = temp[1];
      new SourceService()
        .testConnection(applyConfigure)
        .then((response) => {
          this.testInfo.percent = 100;
          if (response.status) {
            this.$Message.success("Test successful");
            this.testInfo.connected = true;
          }
          else {
            this.$Message.error(response.message);
            this.testInfo.connected = false;
          }
        })
        .finally(() => {
          this.connectionLoading = false;
        });
    },
    handlerPlusConfigure()
    {
      const configure: Configure = {field: '', value: ''};
      this.configure.push(configure);
    },
    handlerMinusConfigure(configure: Configure)
    {
      const index = this.configure.indexOf(configure);
      if (index !== -1) {
        this.configure.splice(index, 1);
      }
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
