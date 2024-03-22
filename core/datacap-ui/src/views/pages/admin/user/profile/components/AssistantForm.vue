<template>
  <div>
    <CircularLoading v-if="loading.default" :show="loading.default"/>
    <form class="space-y-8" v-else-if="formState">
      <FormField name="host">
        <FormItem>
          <FormLabel>{{ $t('user.common.host') }}</FormLabel>
          <FormControl>
            <Input v-model="formState.host"/>
          </FormControl>
          <FormDescription>{{ $t('user.tip.host') }}</FormDescription>
        </FormItem>
      </FormField>
      <FormField name="token">
        <FormItem class="space-y-1">
          <FormLabel>{{ $t('user.common.token') }}</FormLabel>
          <FormControl>
            <Input v-model="formState.token"/>
          </FormControl>
          <FormDescription>{{ $t('user.tip.token') }}</FormDescription>
        </FormItem>
      </FormField>
      <FormField name="timeout">
        <FormItem class="space-y-1">
          <FormLabel>{{ $t('user.common.timeout') }}</FormLabel>
          <FormControl>
            <Input type="number" v-model="formState.timeout"/>
          </FormControl>
          <FormDescription>{{ $t('user.tip.timeout') }}</FormDescription>
        </FormItem>
      </FormField>
      <FormField name="contentCount">
        <FormItem class="space-y-1">
          <FormLabel>{{ $t('user.common.contentCount') }}</FormLabel>
          <FormControl>
            <Input type="number" v-model="formState.contentCount"/>
          </FormControl>
          <FormDescription>{{ $t('user.tip.contentCount') }}</FormDescription>
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
import { UserChatModel } from '@/model/user'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { ToastUtils } from '@/utils/toast'
import Common from '@/utils/common'
import { Loader2 } from 'lucide-vue-next'
import { Button } from '@/components/ui/button'

export default defineComponent({
  name: 'AssistantForm',
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
      formState: null as UserChatModel | null
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
              const configure = response.data.chatConfigure
              if (response.data && configure) {
                this.formState = JSON.parse(configure) as UserChatModel
              }
            }
          })
          .finally(() => this.loading.default = false)
    },
    handlerSubmit()
    {
      this.loading.submitting = true
      UserService.changeChart(this.formState as UserChatModel)
          .then((response) => {
            if (response.status) {
              ToastUtils.success(this.$t('common.successfully') as string)
              localStorage.setItem(Common.userEditorConfigure, JSON.stringify(this.formState))
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
