<template>
  <div>
    <CircularLoading v-if="loading" :show="loading"/>
    <form class="space-y-8" v-else-if="formState">
      <FormField name="avatar">
        <FormItem>
          <FormLabel>{{ $t('user.common.avatar') }}</FormLabel>
          <FormControl>
            <div>
              <Avatar>
                <AvatarImage :src="formState.avatarConfigure?.path" :alt="formState.username"/>
                <AvatarFallback>{{ formState.username }}</AvatarFallback>
              </Avatar>
            </div>
          </FormControl>
          <FormDescription>{{ $t('user.tip.avatar') }}</FormDescription>
        </FormItem>
      </FormField>
      <FormField name="username">
        <FormItem>
          <FormLabel>{{ $t('user.common.username') }}</FormLabel>
          <FormControl>
            <Input :disabled="true" v-model="formState.username"/>
          </FormControl>
          <FormDescription>{{ $t('user.tip.username') }}</FormDescription>
        </FormItem>
      </FormField>
      <FormField name="createTime">
        <FormItem class="space-y-1">
          <FormLabel>{{ $t('user.common.createTime') }}</FormLabel>
          <FormControl>
            <Input :disabled="true" v-model="formState.createTime"/>
          </FormControl>
          <FormDescription>{{ $t('user.tip.createTime') }}</FormDescription>
        </FormItem>
      </FormField>
    </form>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import UserService from '@/services/user'
import { UserModel } from '@/model/user'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'

export default defineComponent({
  name: 'ProfileForm',
  components: {
    Avatar, AvatarImage, AvatarFallback,
    CircularLoading,
    Input,
    FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage
  },
  data()
  {
    return {
      loading: false,
      formState: null as UserModel | null
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      UserService.getInfo()
          .then(response => {
            if (response.status) {
              this.formState = response.data
            }
          })
          .finally(() => this.loading = false)
    }
  }
});
</script>
