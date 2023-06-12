<template>
  <div>
    <Drawer :title="title" :width="720" :closable="false" v-model="visible" :maskClosable="false" :z-index="9">
      <Form :model="formState" ref="formValidate" :rules="ruleValidate">
        <FormItem v-if="formState.id > 0" :label="$t('common.id')" :label-width="80" prop="id">
          <Input v-model="formState.id" :disabled="formState.id > 0"/>
        </FormItem>
        <FormItem :label="$t('common.name')" :label-width="80" prop="name">
          <Input v-model="formState.name"/>
        </FormItem>
        <FormItem :label="$t('common.description')" :label-width="80" prop="description">
          <Input type="textarea" :rows="10" v-model="formState.description"/>
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
import {Role, RoleBuilder} from "@/services/admin/RoleModel";
import {useI18n} from "vue-i18n";
import RoleService from "@/services/admin/RoleService";

export default defineComponent({
  name: 'RoleDetails',
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
      formState: null as Role,
      title: null,
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
    const role: Role = new RoleBuilder().build();
    this.formState = reactive<Role>(role);
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      const i18n = useI18n();
      this.title = i18n.t('common.create');
      if (this.id > 0) {
        this.title = i18n.t('common.modify');
        this.loading = true;
        RoleService.getById(this.id)
          .then(response => {
            if (response.status) {
              this.formState = reactive<Role>(response.data);
            }
          })
          .finally(() => {
            this.loading = false;
          });
      }
      this.title += i18n.t('common.authority');
    },
    handlerSave()
    {
      this.$refs['formValidate'].validate((valid) => {
        if (valid) {
          this.saving = true;
          RoleService.saveOrUpdate(this.formState)
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
