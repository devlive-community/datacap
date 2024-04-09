<template>
  <Dialog :is-visible="visible" :title="title as string" :width="'40%'" @close="handlerCancel">
    <div class="grid w-full gap-2 pt-1 pl-3 pr-3">
      <Alert type="error" :description="$t('snippet.tip.deleteAlert1')"/>
      <Alert type="error" :description="$t('snippet.tip.deleteAlert2')"/>
      <Alert type="error" :description="$t('snippet.tip.deleteAlert3')"/>
      <Alert :description="$t('snippet.tip.deleteAlert4').replace('$VALUE', info?.name as string)"/>
    </div>
    <div class="pl-3 pr-3">
      <Input v-model="inputValue"/>
    </div>
    <template #footer>
      <div class="space-x-5">
        <Button variant="outline" size="sm" @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button size="sm" color="#ed4014" :loading="loading" :disabled="loading || inputValue !== info?.name" @click="handlerSubmit">
          {{ $t('snippet.common.delete') }}
        </Button>
      </div>
    </template>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Dialog from '@/views/ui/dialog'
import SnippetService from '@/services/snippet'
import { ToastUtils } from '@/utils/toast'
import Button from '@/views/ui/button'
import Alert from '@/views/ui/alert'
import { toNumber } from 'lodash'
import { SnippetModel } from '@/model/snippet'
import { Input } from '@/components/ui/input'

export default defineComponent({
  name: 'SnippetDelete',
  components: {
    Input,
    Dialog,
    Button,
    Alert
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
    },
    info: {
      type: Object as () => SnippetModel | null
    }
  },
  data()
  {
    return {
      loading: false,
      title: null as string | null,
      inputValue: undefined
    }
  },
  created()
  {
    if (this.info) {
      this.title = this.$t('snippet.common.deleteInfo').replace('$VALUE', this.info.name as string)
    }
  },
  methods: {
    handlerSubmit()
    {
      if (this.info) {
        this.loading = true
        SnippetService.deleteById(toNumber(this.info.id))
                      .then(response => {
                        if (response.status) {
                          ToastUtils.success(this.$t('snippet.tip.deleteSuccess').replace('$VALUE', this.info?.name as string))
                          this.handlerCancel()
                        }
                        else {
                          ToastUtils.success(response.message)
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