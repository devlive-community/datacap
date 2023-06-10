<template>
  <div>
    <Drawer :title="title" :width="720" :closable="false" v-model="visible" :maskClosable="false" :z-index="9">
      <Form :model="formState" ref="formValidate" :rules="ruleValidate">
        <Row :gutter="32">
          <Col span="12">
            <FormItem :label="$t('common.name')" :label-width="80" prop="name">
              <Input v-model="formState['name']"/>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem :label="$t('common.code')" :label-width="80" prop="code">
              <Input v-model="formState['code']"/>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem :label="$t('common.url')" :label-width="80" prop="url">
              <Input v-model="formState['url']"/>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem :label="$t('common.group')" :label-width="80" prop="group">
              <Input v-model="formState['group']"/>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem :label="$t('common.sorted')" :label-width="80" prop="sorted">
              <InputNumber v-model="formState['sorted']"/>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem :label="$t('common.type')" :label-width="80" prop="type">
              <Select v-model="formState['type']">
                <Option value="VIEW">VIEW</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem :label="$t('common.parent')" :label-width="80" prop="parent">
              <Select v-model="formState['parent']">
                <Option v-for="menu in fullMenus" :value="menu.id" :key="menu.name">{{ menu.name }}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem :label="$t('common.i18nKey')" :label-width="80" prop="i18nKey">
              <Input v-model="formState['i18nKey']"/>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem :label="$t('common.icon')" :label-width="80" prop="icon">
              <Input v-model="formState['icon']"/>
            </FormItem>
          </Col>
        </Row>
        <FormItem :label="$t('common.description')" :label-width="80" prop="description">
          <Input type="textarea" :rows="3" v-model="formState['description']"/>
        </FormItem>
      </Form>
      <div class="datacap-drawer-footer">
        <Button :disabled="saving" style="margin-right: 8px" @click="handlerCancel()">{{ $t('common.cancel') }}</Button>
        <Button type="primary" :loading="saving" @click="handlerSave()">{{ $t('common.submit') }}</Button>
      </div>
      <Spin size="large" fix :show="loading"></Spin>
    </Drawer>
  </div>
</template>
<script lang="ts">
import {defineComponent, reactive} from 'vue';
import {useI18n} from "vue-i18n";
import defaultEntity from "@/entity/CommonEntity";
import MenuService from "@/services/admin/MenuService";
import {Filter} from "@/model/Filter";

const entity = defaultEntity;

export default defineComponent({
  name: 'MenuDetails',
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
      loading: false,
      saving: false,
      formState: null,
      title: null,
      fullMenus: [],
      ruleValidate: {
        name: [
          {required: true}
        ],
        description: [
          {required: true}
        ]
      }
    }
  },
  created()
  {
    this.handlerReset();
    this.formState = reactive<any>(this.formState);
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true;
      const i18n = useI18n();
      this.title = i18n.t('common.create');
      const filter: Filter = new Filter();
      filter.size = 1000;
      MenuService.getAll(filter)
        .then((response) => {
          if (response.status) {
            this.fullMenus = response.data.content;
            if (this.id > 0) {
              this.title = i18n.t('common.modify');
              MenuService.getById(this.id).then(response => {
                if (response.status) {
                  this.formState = reactive<any>(response.data);
                }
              })
            }
          }
        })
        .finally(() => {
          this.loading = false
        })
      this.title += i18n.t('common.menu');
    },
    handlerReset()
    {
      this.formState = entity
        .set('id', 0)
        .set('name', null)
        .set('description', null)
        .set('url', null)
        .set('group', null)
        .set('sorted', 0)
        .set('type', 'VIEW')
        .set('parent', null)
        .set('code', null)
        .set('active', true)
        .set('i18nKey', null)
        .set('icon', null)
        .build()['props'];
    },
    handlerSave()
    {
      this.$refs['formValidate'].validate((valid) => {
        if (valid) {
          this.saving = true;
          MenuService.saveOrUpdate(this.formState)
            .then(response => {
              if (response.status) {
                this.$Message.success("successful");
                this.handlerCancel();
              }
              else {
                this.$Message.error(response.message);
              }
            })
            .finally(() => {
              this.saving = false;
            });
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
