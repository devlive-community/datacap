<template>
  <div class="relative min-h-screen">
    <a-spin :spinning="loading">
      <a-form :model="formState" @finish="onSubmit">
        <div class="space-y-2">
          <div v-for="item in formState.notifyConfigure"
               :key="item.type"
               class="flex items-center justify-between w-full">
            <span class="text-gray-500">
              {{ $t('notify.text.' + item.type.toLowerCase()) }}
            </span>
            <div class="flex items-center space-x-2">
              <a-switch v-model:checked="item.enabled"/>
              <a-button shape="circle" size="small" :disabled="!item.enabled" @click="visibleConfigure(true, item)">
                <ShadcnIcon icon="Cog" size="15"/>
              </a-button>
            </div>
          </div>
        </div>

        <a-button type="primary" html-type="submit" class="mt-3" :loading="submitting" :disabled="submitting">
          {{ $t('common.save') }}
        </a-button>
      </a-form>
    </a-spin>

    <NotifyConfigure v-if="visible"
                     :model-value="visible"
                     :item="info"
                     @update:model-value="visibleConfigure($event)"
                     @update:configure="updateConfigure"/>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import UserService from '@/services/user.ts'
import PluginService from '@/services/plugin.ts'
import { HttpUtils } from '@/utils/http.ts'
import NotifyConfigure from '@/views/pages/admin/user/profile/components/NotifyConfigure.vue'

defineOptions({ name: 'NotifyForm' })

const { t } = useI18n()

const visible = ref(false)
const info = ref<any>({})
const loading = ref(false)
const submitting = ref(false)
const plugins = ref<any[]>([])
const formState = ref<{ notifyConfigure: any[] }>({ notifyConfigure: [] })

const handlerInitialize = () => {
  loading.value = true
  const axios = new HttpUtils().getAxios()

  axios.all([UserService.getInfo(), PluginService.getPlugins()])
       .then(axios.spread((infoResponse: any, plugin: any) => {
         // 初始化默认配置
         formState.value = {
           notifyConfigure: [
             { type: 'Internal', enabled: false, services: [] }
           ]
         }

         // 处理插件数据
         if (plugin.status && plugin.data && Array.isArray(plugin.data)) {
           plugins.value = plugin.data.filter((v: { type: string }) => v.type === 'NOTIFY')
           plugins.value.forEach((item: { name: string }) => {
             if (item && item.name) {
               formState.value.notifyConfigure.push({ type: item.name, enabled: false, services: [] })
             }
           })
         }
         else {
           message.error(plugin.message)
         }

         // 处理用户信息数据：合并服务器配置与本地默认配置
         if (infoResponse.status && infoResponse.data) {
           const serverConfigure = infoResponse.data.notifyConfigure
           if (serverConfigure && Array.isArray(serverConfigure) && serverConfigure.length > 0) {
             const serverConfigMap: Record<string, any> = {}
             serverConfigure.forEach((config: any) => {
               if (config && config.type) {
                 serverConfigMap[config.type] = config
               }
             })

             formState.value.notifyConfigure = formState.value.notifyConfigure.map(localConfig => serverConfigMap[localConfig.type] || localConfig)

             serverConfigure.forEach((serverConfig: any) => {
               if (serverConfig && serverConfig.type) {
                 const exists = formState.value.notifyConfigure.some(localConfig => localConfig.type === serverConfig.type)
                 if (!exists) {
                   formState.value.notifyConfigure.push(serverConfig)
                 }
               }
             })
           }
         }
         else {
           message.error(infoResponse.message)
         }
       }))
       .finally(() => (loading.value = false))
}

const visibleConfigure = (opened?: boolean, item?: any) => {
  visible.value = !!opened
  info.value = item
}

const updateConfigure = (updatedItem: any) => {
  if (updatedItem && updatedItem.type) {
    const index = formState.value.notifyConfigure.findIndex((item: { type: string }) => item.type === updatedItem.type)
    if (index !== -1) {
      formState.value.notifyConfigure[index] = { ...updatedItem }
    }
  }
}

const onSubmit = () => {
  submitting.value = true
  UserService.changeNotify(formState.value)
             .then((response) => {
               if (response.status) {
                 message.success(t('common.successfully') as string)
               }
               else {
                 message.error(response.message)
               }
             })
             .finally(() => (submitting.value = false))
}

handlerInitialize()
</script>
