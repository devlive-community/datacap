<template>
  <div>
    <CircularLoading v-if="loading.default" :show="loading.default"/>
    <form class="space-y-8" v-else-if="formState">
      <FormField name="fontSize">
        <FormItem>
          <FormLabel>{{ $t('user.common.fontSize') }}</FormLabel>
          <FormControl>
            <Input v-model="formState.fontSize" type="number"/>
          </FormControl>
          <FormDescription>{{ $t('user.tip.fontSize') }}</FormDescription>
        </FormItem>
      </FormField>
      <FormField type="radio" name="theme">
        <FormItem class="space-y-1">
          <FormLabel>{{ $t('user.common.theme') }}</FormLabel>
          <FormDescription>{{ $t('user.tip.theme') }}</FormDescription>
          <FormMessage/>
          <RadioGroup v-model="formState.theme" :default-value="formState.theme" class="grid w-full grid-cols-3 gap-8 pt-2">
            <FormItem v-for="theme of themes" :key="theme">
              <FormLabel class="[&:has([data-state=checked])>div]:border-primary">
                <FormControl>
                  <RadioGroupItem :value="theme" class="sr-only"/>
                </FormControl>
                <div class="items-center rounded-md border-4 border-muted p-1 hover:border-accent">
                  <VAceEditor lang="mysql" :theme="theme" :style="{height: '85px'}" :value="value" :options="{readOnly: true}"/>
                </div>
                <span class="block w-full p-2 text-center font-normal">{{ theme }}</span>
              </FormLabel>
            </FormItem>
          </RadioGroup>
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
import { UserEditor } from '@/model/user'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { RadioGroup, RadioGroupItem } from '@/components/ui/radio-group'
import { VAceEditor } from 'vue3-ace-editor'
import themes from './AceEditor'
import { Button } from '@/components/ui/button'
import Common from '@/utils/common'
import { ToastUtils } from '@/utils/toast'
import { Loader2 } from 'lucide-vue-next'

export default defineComponent({
  name: 'EditorForm',
  components: {
    Loader2,
    Button,
    CircularLoading,
    Input,
    FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage,
    RadioGroup, RadioGroupItem,
    VAceEditor
  },
  data()
  {
    return {
      loading: {
        default: false,
        submitting: false
      },
      formState: null as UserEditor | null,
      themes: themes.themes,
      value: 'SHOW TABLES\nSELECT * FROM table\nCREATE TABLE table\nDROP TABLE table\nALTER TABLE table'
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
              const configure = response.data.editorConfigure
              if (response.data && configure) {
                this.formState = configure as UserEditor
              }
            }
          })
          .finally(() => this.loading.default = false)
    },
    handlerSubmit()
    {
      this.loading.submitting = true
      UserService.changeEditor(this.formState as UserEditor)
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
