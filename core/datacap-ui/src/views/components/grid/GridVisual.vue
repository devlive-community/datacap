<template>
  <Dialog :is-visible="visible" width="80%" @close="handlerCancel">
    <template #title>
      <div class="flex items-center space-x-2">
        <BarChart :size="18"/>
        <span>{{ $t('dataset.common.visual') }}</span>
      </div>
    </template>
    <div class="pl-4 pr-4">
      <Alert v-if="message" type="error" :description="message"/>
      <FormField name="name">
        <FormItem>
          <FormLabel class="mr-1 w-20 text-right">
            {{ $t('common.name') }}
          </FormLabel>
          <FormControl>
            <Input v-model="formState.name"/>
          </FormControl>
        </FormItem>
      </FormField>
      <FormField name="description">
        <FormItem>
          <FormLabel class="mr-1 w-20 text-right">
            {{ $t('common.description') }}
          </FormLabel>
          <FormControl>
            <Textarea v-model="formState.description"/>
          </FormControl>
        </FormItem>
      </FormField>
      <VisualEditor :loading="loading" :configuration="configuration" @commitOptions="handlerCommitOptions"/>
    </div>
    <template #footer>
      <div class="space-x-2">
        <Button size="sm" variant="outline" class="h-7 space-x-1" @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button size="sm" class="h-7 space-x-1" :loading="published" @click="handlerPublish">
          {{ $t('common.publish') }}
        </Button>
      </div>
    </template>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Dialog from '@/views/ui/dialog'
import { BarChart } from 'lucide-vue-next'
import Button from '@/views/ui/button'
import VisualEditor from '@/views/components/visual/VisualEditor.vue'
import { Configuration } from '@/views/components/visual/Configuration.ts'
import { GridConfigure } from '@/views/components/grid/GridConfigure.ts'
import { Textarea } from '@/components/ui/textarea'
import { FormControl, FormField, FormItem, FormLabel } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { ToastUtils } from '@/utils/toast.ts'
import router from '@/router'
import ReportService from '@/services/report'
import Alert from '@/views/ui/alert'

export default defineComponent({
  name: 'GridVisual',
  components: {
    Alert,
    Input,
    FormControl, FormField, FormLabel, FormItem,
    Textarea,
    VisualEditor,
    Button,
    BarChart,
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
      type: Boolean,
      default: false
    },
    configure: {
      type: Object as () => GridConfigure,
      default: () => null
    }
  },
  data()
  {
    return {
      loading: false,
      configuration: null as Configuration | null,
      published: false,
      message: null as string | null,
      formState: {
        name: '',
        realtime: true,
        type: 'QUERY',
        configure: null as string | null,
        query: this.configure?.query,
        description: '',
        source: {
          id: this.configure?.sourceId
        }
      }
    }
  },
  created()
  {
    if (this.configure) {
      this.configuration = new Configuration()
      this.configuration.headers = this.configure?.headers as never[]
      this.configuration.columns = this.configure?.columns as never[]
    }
  },
  methods: {
    handlerCommitOptions(value: any)
    {
      this.formState.configure = JSON.stringify(value)
    },
    handlerPublish()
    {
      this.validator()
      if (!this.message) {
        this.published = true
        ReportService.saveOrUpdate(this.formState)
                     .then(response => {
                       if (response.status) {
                         ToastUtils.success(this.$t('report.tip.publishSuccess').replace('$VALUE', this.formState.name))
                         router.push('/admin/report')
                       }
                       else {
                         ToastUtils.error(response.message)
                       }
                     })
                     .finally(() => this.published = false)
      }
    },
    handlerCancel()
    {
      this.visible = false
    },
    validator()
    {
      if (!this.formState.name) {
        this.message = this.$t('report.validator.name').toString()
      }
      else {
        this.message = null
      }
    }
  }
})
</script>
