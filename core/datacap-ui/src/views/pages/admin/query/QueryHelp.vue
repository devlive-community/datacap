<template>
  <div class="w-full">
    <AlertDialog :default-open="visible" @update:open="handlerCancel">
      <AlertDialogContent class="w-full min-h-52">
        <AlertDialogHeader>
          <AlertDialogTitle class="border-b -mt-4 pb-2">{{ $t('query.common.help') }}</AlertDialogTitle>
        </AlertDialogHeader>
        <Tabs :default-value="getEnumName(helpType[0])" @update:modelValue="handlerTab">
          <TabsList class="w-full">
            <TabsTrigger v-for="item in helpType" :value="getEnumName(item)">
              {{ getEnumName(item) }}
            </TabsTrigger>
          </TabsList>
          <CircularLoading v-if="loading" :show="loading"/>
          <MdPreview v-else-if="helpReplyContent" :modelValue="helpReplyContent" style="padding: 0; width: 100%"/>
        </Tabs>
        <AlertDialogFooter class="border-t pt-2">
          <Button @click="handlerCancel">{{ $t('common.cancel') }}</Button>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue'
import UserService from '@/services/user'
import { UserModel, UserQuestionModel } from '@/model/user'
import { Button } from '@/components/ui/button'
import { AlertDialog, AlertDialogContent, AlertDialogFooter, AlertDialogHeader } from '@/components/ui/alert-dialog'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { isEmpty } from 'lodash'
import { HelpType } from '@/views/pages/admin/query/HelpType'
import MessageService from '@/services/message'
import { ToastUtils } from '@/utils/toast'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

export default defineComponent({
  name: 'QueryHelp',
  components: {
    CircularLoading,
    AlertDialogFooter, AlertDialog, AlertDialogHeader, AlertDialogContent,
    Tabs, TabsContent, TabsList, TabsTrigger,
    Button,
    MdPreview
  },
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    content: {
      type: String,
      default: () => ''
    },
    engine: {
      type: String,
      default: () => ''
    },
    message: {
      type: String,
      default: () => ''
    },
    helpType: {
      type: Array as () => Array<HelpType>,
      default: () => []
    }
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
  data()
  {
    return {
      loading: false,
      userInfo: null as UserModel | null,
      helpReplyContent: null as string | null
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    isEmpty,
    handlerInitialize()
    {
      UserService.getInfo()
          .then(response => {
            if (response.status) {
              this.userInfo = response.data
              this.handlerTab(this.helpType[0] as HelpType)
            }
          })
    },
    handlerTab(value: any)
    {
      this.loading = true
      const userQuestion: UserQuestionModel = {
        type: 'ChatGPT',
        content: this.content,
        transType: value,
        engine: this.engine,
        error: this.message,
        locale: this.$i18n.locale,
        newChat: true
      }
      MessageService.aiReply(userQuestion)
          .then(response => {
            if (response.status) {
              this.helpReplyContent = response.data.content
            }
            else {
              ToastUtils.error(response.message)
            }
          })
          .finally(() => this.loading = false)
    },
    handlerCancel()
    {
      this.visible = false
    },
    getEnumName(value: HelpType): string
    {
      return HelpType[value]
    }
  }
});
</script>