<template>
  <div>
    <CircularLoading v-if="loading" :show="loading"/>
    <form class="space-y-8" v-else-if="formState">
      <FormField name="avatar">
        <FormItem>
          <FormLabel>{{ $t('user.common.avatar') }}</FormLabel>
          <FormControl>
            <div class="flex flex-row items-center justify-between">
              <div>
                <Avatar>
                  <AvatarImage :src="formState.avatarConfigure?.path as string" :alt="formState.username"/>
                  <AvatarFallback>{{ formState.username }}</AvatarFallback>
                </Avatar>
              </div>
              <div class="w-32 space-y-2 items-center">
                <Avatar :class="inputFile ? 'ml-2' : '-ml-1'">
                  <AvatarImage :src="inputFileBase64 as string"/>
                </Avatar>
                <div class="space-x-1">
                  <input type="file" id="fileInput" class="hidden" @change="handleFileChange"/>
                  <label v-if="!inputFile" for="fileInput" class="cursor-pointer">
                    <Tooltip :content="$t('common.file')">
                      <span
                          class="inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50 bg-primary text-primary-foreground shadow hover:bg-primary/90 h-6 w-6 p-1">
                        <Image/>
                      </span>
                    </Tooltip>
                  </label>
                  <div v-else class="space-x-1">
                    <Button :loading="uploading" :disabled="uploading" size="icon" class="w-6 h-6" @click="handlerUpload">
                      <Upload v-if="!uploading" :size="15"/>
                    </Button>
                    <Button :disabled="uploading" size="icon" variant="destructive" class="w-6 h-6" @click="inputFile = null">
                      <Trash :size="15"/>
                    </Button>
                  </div>
                </div>
              </div>
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
import { Label } from '@/components/ui/label'
import Tooltip from '@/views/ui/tooltip'
import Button from '@/views/ui/button'
import { Image, Trash, Upload } from 'lucide-vue-next'
import Common from '@/utils/common'

export default defineComponent({
  name: 'ProfileForm',
  components: {
    Image, Upload, Trash,
    Tooltip,
    Button,
    Label,
    Avatar, AvatarImage, AvatarFallback,
    CircularLoading,
    Input,
    FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage
  },
  data()
  {
    return {
      loading: false,
      formState: null as UserModel | null,
      inputFile: null as any,
      inputFileBase64: null as string | null,
      uploading: false
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
    },
    handleFileChange(event: Event)
    {
      const input = event.target as HTMLInputElement
      if (input.files && input.files.length > 0) {
        this.inputFile = input.files[0]
        Common.fileToBase64(this.inputFile)
              .then(response => {
                this.inputFileBase64 = response
              })
      }
    },
    handlerUpload()
    {
      if (this.inputFile) {
        this.uploading = true
        const formData = new FormData()
        formData.append('file', this.inputFile)
        UserService.uploadAvatar(formData)
                   .then(response => {
                     if (response.status) {
                       this.inputFile = null
                       this.inputFileBase64 = null
                       this.handlerInitialize()
                     }
                   })
                   .finally(() => this.uploading = false)
      }
    }
  }
})
</script>
