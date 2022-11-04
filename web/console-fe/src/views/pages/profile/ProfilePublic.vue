<template>
  <a-layout-content>
    <a-card>
      <template #title>
        {{ $t('setting.profile') }}
      </template>
      <a-spin :spinning="loading">
        <a-layout style="background-color: #ffffff">
          <a-layout-content>
            <a-form :model="formState" v-bind="layout">
              <a-form-item :name="['name']" :label="$t('common.username')">
                <span>{{ formState.username }}</span>
              </a-form-item>
              <a-form-item :name="['createTime']" :label="$t('common.createTime')">
                <span>{{ formState.createTime }}</span>
              </a-form-item>
            </a-form>
          </a-layout-content>
          <a-layout-sider style="background-color: #ffffff; margin-left: 60px;">
            <a-avatar :size="64">
              <template #icon>
                <UserOutlined/>
              </template>
            </a-avatar>
          </a-layout-sider>
        </a-layout>
      </a-spin>
    </a-card>
  </a-layout-content>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import {User} from "@/model/User";
import UserService from "@/services/UserService";

const layout = {
  labelCol: {span: 8},
  wrapperCol: {span: 16},
};

export default defineComponent({
  data()
  {
    return {
      loading: false,
      formState: {} as User,
      layout
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true;
      UserService.getInfo()
        .then(response => {
          if (response.status) {
            this.formState = response.data;
          }
        })
        .finally(() => {
          this.loading = false;
        });
    }
  }
});
</script>
