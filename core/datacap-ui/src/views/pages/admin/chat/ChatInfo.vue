<template>
  <Dialog :is-visible="visible" :title="$t('common.chat')">
    <div class="space-y-2 pl-3 pr-3">
      <FormField name="name">
        <FormItem class="space-y-1">
          <FormLabel>{{ $t('common.name') }}</FormLabel>
          <FormMessage/>
          <Input v-model="formState.name"/>
        </FormItem>
      </FormField>
      <FormField name="avatar">
        <FormItem class="space-y-1">
          <FormLabel>{{ $t('common.avatar') }}</FormLabel>
          <FormMessage/>
          <Input v-model="formState.avatar"/>
        </FormItem>
      </FormField>
      <FormField name="description">
        <FormItem class="space-y-1">
          <FormLabel>{{ $t('common.description') }}</FormLabel>
          <FormMessage/>
          <Textarea v-model="formState.description"/>
        </FormItem>
      </FormField>
    </div>
    <template #footer>
      <div class="space-x-5">
        <Button variant="outline" size="sm" @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button size="sm" :loading="loading" :disabled="loading" @click="handlerSave()">
          {{ $t('common.save') }}
        </Button>
      </div>
    </template>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Dialog from '@/views/ui/dialog'
import Button from '@/views/ui/button'
import { Textarea } from '@/components/ui/textarea'
import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { ToastUtils } from '@/utils/toast'
import ChatService from '@/services/chat.ts'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { ChatModel, ChatRequest } from '@/model/chat.ts'

export default defineComponent({
  name: 'ChatInfo',
  components: {
    CircularLoading,
    Input,
    FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage,
    Textarea,
    Button,
    Dialog
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
  props: {
    isVisible: {
      type: Boolean
    }
  },
  data()
  {
    return {
      loading: false,
      formState: null as unknown as ChatModel
    }
  },
  created()
  {
    this.formState = ChatRequest.of()
  },
  methods: {
    handlerSave()
    {
      if (this.formState) {
        this.loading = true
        ChatService.saveOrUpdate(this.formState)
                   .then(response => {
                     if (response.status) {
                       ToastUtils.success(this.$t('common.success'))
                       this.handlerCancel()
                     }
                     else {
                       ToastUtils.error(response.message)
                     }
                   })
                   .finally(() => this.loading = false)
      }
    },
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>