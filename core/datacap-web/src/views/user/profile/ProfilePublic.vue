<template>
  <div>
    <Card style="width:100%; minHeight: 150px;"
          dis-hover>
      <template #title>
        {{ $t('setting.profile') }}
      </template>
      <Spin fix
            v-if="loading">
      </Spin>
      <div v-else>
        <Layout>
          <Layout>
            <Content class="content"
                     style="padding: 0 0 0 40px;">
              <Form :label-width="80">
                <FormItem :label="$t('common.username')">
                  <span>{{ formState.username }}</span>
                </FormItem>
                <FormItem :label="$t('common.createTime')">
                  <span>{{ formState.createTime }}</span>
                </FormItem>
              </Form>
            </Content>
            <Sider class="content"
                   style="text-align: center;"
                   hide-trigger>
              <Avatar v-if="authUser.avatar"
                      size="64"
                      :src="formState.avatarConfigure['path']">
              </Avatar>&nbsp;
              <Avatar v-else
                      size="64"
                      style="background-color: #87d068">
                {{ authUser.username }}
              </Avatar>&nbsp;
              <Upload style="margin-top: 10px;"
                      :headers="{'Authorization': auth.type + ' ' + auth.token}"
                      :format="['jpg', 'png', 'jpeg']"
                      :on-success="handlerUploadSuccess"
                      action="/api/v1/user/uploadAvatar">
                <Button>{{ $t('common.uploadAvatar') }}</Button>
              </Upload>
            </Sider>
          </Layout>
        </Layout>
      </div>
    </Card>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import {User} from "@/model/User";
import UserService from "@/services/UserService";
import Common from "@/common/Common";
import {ResponseModel} from "@/model/ResponseModel";
import {AuthResponse} from "@/model/AuthResponse";

export default defineComponent({
  setup()
  {
    const authUser = JSON.parse(localStorage.getItem(Common.token) || '{}') as AuthResponse;
    return {
      authUser
    }
  },
  data()
  {
    return {
      loading: false,
      formState: {} as User,
      auth: null
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      this.auth = JSON.parse(localStorage.getItem(Common.token) || '{}');
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
    },
    handlerUploadSuccess(response: ResponseModel)
    {
      if (response.status) {
        const configure = this.applyPlugin.configures.filter(configure => configure.field === 'file')
        configure[0].value.push(response.data)
      }
    }
  }
});
</script>
<style scoped>
.content {
  background-color: #FFFFFF;
}
</style>
