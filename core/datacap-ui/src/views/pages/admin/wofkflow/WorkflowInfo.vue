<template>
  <div class="w-full h-screen min-h-screen" style="height: 100%;">
    <a-card>
      <template #title>
        <div class="ml-2">{{ $t('workflow.text.create') }}</div>
      </template>

      <template #extra>
        <div class="flex items-center space-x-4">
          <div class="flex items-center space-x-2">
            <span class="text-sm">{{ $t('common.executor') }}</span>

            <a-select v-model:value="formState.executor" :style="{ minWidth: '160px' }">
              <a-select-option v-for="executor in installedExecutors"
                               :key="executor.name"
                               :value="executor.name">
                {{ executor.name }}
              </a-select-option>
            </a-select>
          </div>

          <a-button type="primary" :disabled="!(workflowState?.validation?.length === 0)" @click="visible = true">
            {{ $t('common.publish') }}
          </a-button>
        </div>
      </template>

      <div class="relative h-screen">
        <a-spin :spinning="loading">
          <ShadcnWorkflowEditor v-if="configuration && !loading"
                                v-model="workflowState"
                                :categories="configuration.categories"
                                :nodes="configuration.nodes"
                                :connections="[]"
                                :configureWidth="380">
          </ShadcnWorkflowEditor>
        </a-spin>
      </div>
    </a-card>
  </div>

  <a-modal v-model:open="visible" :title="$t('workflow.text.configure')" :footer="null">
    <a-form :model="formState" layout="vertical" @finish="onSubmit">
      <a-form-item name="name"
                   :label="$t('workflow.text.name')"
                   :rules="[
                       { required: true, message: $t('workflow.validator.name.required') },
                   ]">
        <a-input v-model:value="formState.name" :placeholder="$t('workflow.placeholder.name')"/>
      </a-form-item>

      <div class="flex justify-end">
        <a-button type="primary" html-type="submit">{{ $t('common.submit') }}</a-button>
      </div>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import ConfigurationService from '@/services/configure'
import WorkflowService from '@/services/workflow'
import PluginService from '@/services/plugin'
import HttpUtils from '@/utils/http.ts'
import { RouterUtils } from '@/utils/route.ts'

export interface Configuration
{
  categories: any[]
  nodes: any[]
}

defineOptions({ name: 'WorkflowInfo' })

const route = useRoute()
const router = useRouter()
const { t } = useI18n()

const loading = ref(false)
const visible = ref(false)
const configuration = ref<Configuration | null>(null)
const workflowState = ref<any | null>(null)
const installedExecutors = ref<any[]>([])
const formState = ref({
  code: null as string | null,
  executor: 'Local',
  name: '',
  configure: null as any
})

const handleInitialize = () => {
  const code = RouterUtils.getParam(route, 'code')
  loading.value = true

  // Prepare the base API calls that are always needed
  const baseApiCalls = [
    ConfigurationService.getExecutor(),
    PluginService.getPlugins()
  ]

  // If code exists, add the workflow API call
  const apiCalls = code
      ? [...baseApiCalls, WorkflowService.getByCode(code)]
      : baseApiCalls

  HttpUtils.all(apiCalls)
           .then(HttpUtils.spread((...responses: any[]) => {
             // Handle executor configuration
             const [executor, plugin, ...rest] = responses

             if (executor.status && executor.data) {
               configuration.value = executor.data
             }

             // Handle plugins
             if (plugin.status && plugin.data) {
               installedExecutors.value = plugin.data.filter(
                   (v: { type: string }) => v.type === 'EXECUTOR'
               )
             }

             // Handle workflow data if code exists
             if (code && rest.length > 0) {
               const workflow = rest[0]
               if (workflow.status && workflow.data) {
                 workflowState.value = workflow.data.configure
                 // Update form state with existing workflow data
                 formState.value.executor = workflow.data.executor || 'Local'
                 formState.value.name = workflow.data.name || ''
                 formState.value.configure = workflow.data.configure || null
                 formState.value.code = workflow.data.code || null
               }
               else {
                 message.error(workflow.message)
               }
             }
           }))
           .catch((error: any) => {
             message.error(error.message || 'Failed to initialize workflow')
           })
           .finally(() => {
             loading.value = false
           })
}

const onSubmit = () => {
  formState.value.configure = workflowState.value.data
  WorkflowService.saveOrUpdate(formState.value)
                 .then((response) => {
                   if (response.status) {
                     message.success(`${ t('workflow.tip.publishSuccess').replace('$VALUE', response.data.name) }`)
                     router.push('/admin/workflow')
                     visible.value = false
                   }
                   else {
                     message.error(response.message)
                   }
                 })
}

handleInitialize()
</script>
