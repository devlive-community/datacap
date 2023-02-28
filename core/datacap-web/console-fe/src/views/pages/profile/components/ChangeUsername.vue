<template>
  <div>
    <Modal v-model="visible" :title="$t('setting.changeUsername')" :closable="false" :mask-closable="false" @cancel="handlerCancel()">
      <Form ref="formCustom" :model="formState" :label-width="80">
        <FormItem :label="$t('common.username')" prop="username">
          {{formState.oldUsername}}
        </FormItem>
        <FormItem :label="$t('setting.newUsername')" name="newUsername">
          <Input type="text" v-model="formState.newUsername"></Input>
        </FormItem>
        <FormItem :label="$t('common.password')" name="password">
          <Input type="password" v-model="formState.password"></Input>
        </FormItem>
      </Form>
      <template #footer>
        <Button danger @click="handlerCancel">{{ $t('common.cancel') }}</Button>
        <Button type="primary" :loading="loading" @click="handlerChangeUsername()">{{ $t('common.save') }}</Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent, reactive} from 'vue';
import UserService from "@/services/UserService";
import Common from "@/common/Common";
import router from "@/router";
import {User} from "@/model/User";
import {AuthResponse} from "@/model/AuthResponse";
import {UserName} from "@/model/UserName";

export default defineComponent({
  name: 'ChangeUsername',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    }
  },
  setup()
  {
    let username;
    const authUser = JSON.parse(localStorage.getItem(Common.token) || '{}') as AuthResponse;
    if (authUser) {
      username = authUser.username;
    }

    const formState = reactive<UserName>({
      oldUsername: username,
      newUsername: '',
      password: ''
    });
    return {
      formState
    }
  },
  data()
  {
    return {
      loading: false
    }
  },
  methods: {
    handlerCancel()
    {
      this.visible = false;
    },
    handlerChangeUsername()
    {
      this.loading = true;
      UserService.changeUsername(this.formState)
        .then((response) => {
          if (response.status) {
            this.$Message.success('Success');
            localStorage.removeItem(Common.token);
            router.push('/auth/signin');
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => {
          this.loading = false;
        });
    }
  },
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible;
      },
      set(value: boolean)
      {
        this.$emit('close', value);
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
