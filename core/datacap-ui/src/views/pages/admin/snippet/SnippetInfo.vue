<template>
  <Drawer :is-visible="visible" :width="'10%'" @close="handlerCancel">
    <template #title>{{ title }}</template>
    <div class="mt-3 space-y-2">
      <FormField name="name">
        <FormItem>
          <FormLabel class="font-normal">{{ $t('common.name') }}</FormLabel>
          <FormControl>
            <Input v-model="formState.name"/>
          </FormControl>
        </FormItem>
      </FormField>
      <FormField name="description">
        <FormItem>
          <FormLabel class="font-normal">{{ $t('common.description') }}</FormLabel>
          <FormControl>
            <Textarea v-model="formState.description"/>
          </FormControl>
        </FormItem>
      </FormField>
      <FormField v-slot="{ componentField }" name="content">
        <FormItem>
          <FormLabel>{{ $t('common.content') }}</FormLabel>
          <FormControl>
            <AceEditor :value="formState.code" v-bind="componentField" @update:value="formState.code = $event"/>
          </FormControl>
        </FormItem>
      </FormField>
    </div>
    <template #footer>
      <div class="space-x-5">
        <Button variant="outline" size="sm" @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button size="sm" :loading="loading" :disabled="loading" @click="handlerSubmit()">
          {{ $t('common.save') }}
        </Button>
      </div>
    </template>
  </Drawer>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Drawer from '@/views/ui/drawer'
import { FormControl, FormField, FormItem, FormLabel } from '@/components/ui/form'
import { SnippetModel, SnippetRequest } from '@/model/snippet'
import { cloneDeep } from 'lodash'
import Button from '@/views/ui/button'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import AceEditor from '@/views/components/editor/AceEditor.vue'
import SnippetService from '@/services/snippet'
import { ToastUtils } from '@/utils/toast'

export default defineComponent({
  name: 'SnippetInfo',
  components: {
    AceEditor,
    Textarea,
    Input,
    FormLabel, FormField, FormControl, FormItem,
    Drawer,
    Button
  },
  props: {
    isVisible: {
      type: Boolean
    },
    info: {
      type: Object as () => SnippetModel | null
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
      formState: null as unknown as SnippetModel,
      title: null as string | null
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.title = this.$t('snippet.common.create')
      if (this.info) {
        this.formState = cloneDeep(this.info)
        if (this.info.id) {
          this.title = `${ this.$t('function.common.modify').replace('$VALUE', this.info.name as string) }`
        }
      }
      else {
        this.formState = SnippetRequest.of()
      }
    },
    handlerSubmit()
    {
      this.loading = true
      SnippetService.saveOrUpdate(this.formState)
                    .then((response) => {
                      if (response.status) {
                        ToastUtils.success(this.$t('snippet.tip.createSuccess').replace('$VALUE', this.formState.name as string))
                        this.handlerCancel()
                      }
                    })
                    .finally(() => this.loading = false)
    },
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>
