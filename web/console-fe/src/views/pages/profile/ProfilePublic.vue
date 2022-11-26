<template>
  <div>
    <Card style="width:100%; minHeight: 150px;">
      <template #title>
        {{ $t('setting.profile') }}
      </template>
      <Spin fix v-if="loading"/>
      <div v-else>
        <Layout>
          <Layout>
            <Content class="content" style="padding: 0 0 0 40px;">
              <Form :label-width="80">
                <FormItem :label="$t('common.username')">
                  <span>{{ formState.username }}</span>
                </FormItem>
                <FormItem :label="$t('common.createTime')">
                  <span>{{ formState.createTime }}</span>
                </FormItem>
              </Form>
            </Content>
            <Sider class="content" hide-trigger>
              <Avatar style="background-color: #87d068" size="64" icon="ios-person"/>
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

export default defineComponent({
  data()
  {
    return {
      loading: false,
      formState: {} as User
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
<style scoped>
.content {
  background-color: #FFFFFF;
}
</style>
