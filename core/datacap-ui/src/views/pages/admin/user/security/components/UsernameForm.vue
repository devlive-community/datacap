<template>
  <div>
    <CircularLoading v-if="loading.default" :show="loading.default"/>
    <form class="space-y-8" v-else-if="formState">
      <FormField name="oldUsername">
        <FormItem>
          <FormLabel>{{ $t('user.common.oldUsername') }}</FormLabel>
          <FormControl>
            <Input :disabled="true" v-model="formState.oldUsername"/>
          </FormControl>
          <FormDescription>{{ $t('user.tip.oldUsername') }}</FormDescription>
        </FormItem>
      </FormField>
      <FormField name="newUsername">
        <FormItem class="space-y-1">
          <FormLabel>{{ $t('user.common.newUsername') }}</FormLabel>
          <FormControl>
            <Input v-model="formState.newUsername"/>
          </FormControl>
          <FormDescription>{{ $t('user.tip.newUsername') }}</FormDescription>
        </FormItem>
      </FormField>
      <FormField name="password">
        <FormItem class="space-y-1">
          <FormLabel>{{ $t('user.common.password') }}</FormLabel>
          <FormControl>
            <Input v-model="formState.password"/>
          </FormControl>
          <FormDescription>{{ $t('user.tip.password') }}</FormDescription>
        </FormItem>
      </FormField>
      <div class="flex justify-start">
        <Button @click="handlerSubmit()">
          <Loader2 v-if="loading.submitting" class="w-full justify-center animate-spin mr-3"/>
          {{ $t('common.save') }}
        </Button>
      </div>
    </form>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import UserService from '@/services/user'
import { UsernameModel } from '@/model/user'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { ToastUtils } from '@/utils/toast'
import Common from '@/utils/common'
import { Loader2 } from 'lucide-vue-next'
import { Button } from '@/components/ui/button'

export default defineComponent({
  name: 'UsernameForm',
  components: {
    Button,
    Loader2,
    CircularLoading,
    Input,
    FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage
  },
  data()
  {
    return {
      loading: {
        default: false,
        submitting: false
      },
      formState: null as UsernameModel | null
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loading.default = true
      UserService.getInfo()
          .then(response => {
            if (response.status) {
              if (response.data) {
                this.formState = {oldUsername: response.data.username, newUsername: undefined, password: undefined}
              }
            }
          })
          .finally(() => this.loading.default = false)
    },
    handlerSubmit()
    {
      this.loading.submitting = true
      UserService.changeUsername(this.formState as UsernameModel)
          .then((response) => {
            if (response.status) {
              ToastUtils.success(this.$t('user.tip.changeUsernameSuccessfully') as string)
              localStorage.removeItem(Common.token)
              localStorage.removeItem(Common.menu)
              this.$router.push('/auth/signin')
            }
            else {
              ToastUtils.error(response.message)
            }
          })
          .finally(() => this.loading.submitting = false)
    }
  }
});
</script>
