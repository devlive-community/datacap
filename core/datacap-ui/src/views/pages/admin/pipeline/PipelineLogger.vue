<template>
  <Dialog :is-visible="visible" :title="title as string" :width="'60%'">
    <CircularLoading v-if="loading" :show="loading"/>
    <div class="overflow-y-auto h-[500px] pl-2 pr-2" v-else>
      <div v-for="(log, index) in logs" :key="index" style="white-space: nowrap;">
        <div v-html="log" style="margin-bottom: 5px; font-size: 16px"/>
      </div>
    </div>
    <template #footer>
      <Button variant="outline" size="sm" @click="handlerCancel">
        {{ $t('common.cancel') }}
      </Button>
    </template>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Dialog from '@/views/ui/dialog'
import Button from '@/views/ui/button'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { PipelineModel } from '@/model/pipeline.ts'
import PipelineService from '@/services/pipeline'
import { toNumber } from 'lodash'
import { AnsiUp } from 'ansi_up'
import { ToastUtils } from '@/utils/toast.ts'

export default defineComponent({
  name: 'PipelineLogger',
  components: {
    CircularLoading,
    Dialog,
    Button
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
      type: Object as () => PipelineModel | null
    }
  },
  data()
  {
    return {
      title: null as string | null,
      loading: false,
      logs: Array<string>()
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      if (this.info) {
        this.title = `${ this.$t('pipeline.common.loggerInfo').replace('$VALUE', this.info.name as string) }`
        this.loading = true
        PipelineService.getLogger(toNumber(this.info.id))
                       .then(response => {
                         if (response.status) {
                           const ansi = new AnsiUp()
                           const array = response.data
                           for (const i in array) {
                             this.logs.push(ansi.ansi_to_html(array[i]))
                           }
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
