<template>
  <div class="relative min-h-screen">
    <a-spin :spinning="loading">
      <a-alert type="warning" class="mb-2">
        <template #message>
          {{ $t('common.plugin.systemVersion') }}
          <a-tag class="text-red-400">{{ version }}</a-tag>
        </template>
      </a-alert>

      <a-tabs v-show="!loading" v-model:activeKey="activeTab">
        <template #rightExtra>
          <a-popover trigger="click" placement="bottom">
            <template #content>
              <div class="p-2 space-y-2 flex flex-col">
                <a-form-item :label="$t('common.plugin.metadata.url')" :extra="$t('common.plugin.metadata.description')">
                  <a-input v-model:value="applyMetadataUrl"/>
                </a-form-item>

                <a-button type="primary" @click="onSave">{{ $t('common.save') }}</a-button>
              </div>
            </template>

            <a-button type="text" shape="circle" size="small">
              <template #icon>
                <SettingOutlined :style="{ fontSize: '15px' }"/>
              </template>
            </a-button>
          </a-popover>
        </template>

        <a-tab-pane v-for="type in pluginTypes" :key="type" :tab="type">
          <div class="relative">
            <a-space direction="vertical" :size="15" :style="{ width: '100%' }">
              <a-alert v-if="typeDescription(type)" :message="typeDescription(type)"/>

              <a-card v-for="plugin in pluginsByType(type)" :key="plugin.key" class="w-full">
                <a-row class="p-3 px-6 items-center">
                  <a-col :span="4">
                    <!-- Logo and Name -->
                    <div class="flex flex-col items-center space-y-2 justify-between">
                      <a-avatar class="bg-transparent"
                                :size="64"
                                :src="plugin.logo"
                                :alt="plugin.i18nFormat ? $t(plugin.label) : plugin.label"
                                @click="onVisibleInfo(plugin, true)">
                      </a-avatar>

                      <span class="text-base font-medium">
                        {{ plugin.i18nFormat ? $t(plugin.label) : plugin.label }}
                      </span>
                    </div>
                  </a-col>

                  <a-col :span="18">
                    <!-- Plugin -->
                    <div class="flex items-center space-x-4 justify-between">
                      <div class="pl-8 flex flex-wrap">
                        <!-- Description -->
                        <div class="flex flex-col space-y-4">
                          <span class="text-sm text-gray-500">
                            {{ plugin.i18nFormat ? $t(plugin.description) : plugin.description }}
                          </span>

                          <!-- Support Version -->
                          <div class="flex space-x-2 text-sm text-gray-500">
                            <div class="flex items-center space-x-2">
                              {{ $t('common.plugin.list.supportVersion') }} :
                            </div>

                            <a-tag v-for="ver in plugin.supportVersion" :key="ver" color="success">
                              {{ ver }}
                            </a-tag>
                          </div>

                          <!-- Version -->
                          <div class="flex space-x-2 text-sm text-gray-500">
                            <div class="space-x-1">
                              {{ $t('common.plugin.version') }} :
                              <a-tag>{{ plugin.version }}</a-tag>
                            </div>

                            <a-divider v-if="plugin.installed" type="vertical"/>

                            <div v-if="plugin.installed" class="space-x-1">
                              {{ $t('common.installVersion') }}:
                              <a-tag color="#00BFFF">{{ plugin.installVersion }}</a-tag>
                            </div>
                          </div>

                          <!-- Time -->
                          <div class="flex space-x-2 text-sm text-gray-500">
                            <div class="space-x-1">
                              {{ $t('common.releasedTime') }}：
                              <a-tag color="warning">{{ plugin.released }}</a-tag>
                            </div>

                            <a-divider v-if="plugin.installed" type="vertical"/>

                            <div v-if="plugin.installed" class="space-x-1">
                              {{ $t('common.installTime') }}：
                              <a-tag color="#00BFFF">{{ plugin.installTime }}</a-tag>
                            </div>
                          </div>

                          <!-- Other -->
                          <div class="flex space-x-2 text-sm text-gray-500">
                            <div class="space-x-1">
                              {{ $t('common.author') }}： {{ plugin.author }}
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </a-col>

                  <a-col :span="2">
                    <!-- Action -->
                    <a-button :type="plugin.installed ? 'default' : 'primary'"
                              :danger="plugin.installed"
                              :loading="plugin.loading"
                              @click="plugin.installed ? onUninstall(plugin) : onInstall(plugin)">
                      <template #icon>
                        <component :is="plugin.installed ? DeleteOutlined : PlusOutlined" :style="{ fontSize: '15px' }"/>
                      </template>
                      {{ plugin.installed ? $t('common.uninstall') : $t('common.install') }}
                    </a-button>
                  </a-col>
                </a-row>
              </a-card>
            </a-space>
          </div>
        </a-tab-pane>
      </a-tabs>
    </a-spin>
  </div>

  <PluginInfo v-if="infoVisible && info"
              :info="info"
              :is-visible="infoVisible"
              @close="onVisibleInfo(null, false)">
  </PluginInfo>
</template>

<script setup lang="ts">
import { computed, onBeforeMount, ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { useI18nHandler } from '@/i18n/I18n'
import { PackageUtils } from '@/utils/package.ts'
import PluginService from '@/services/plugin.ts'
import PluginInfo from '@/views/pages/store/components/PluginInfo.vue'
import { DeleteOutlined, PlusOutlined, SettingOutlined } from '@ant-design/icons-vue'

interface MetadataItem
{
  key: string
  label: string
  description: string
  logo: string
  type: string
  released: string
  i18nFormat: boolean
  version: string
  supportVersion: string[]
  author: string
  installed: boolean
  installTime: string
  installVersion: string
  url: string
  loading: boolean
}

interface Metadata
{
  key: string
  label: string
  description: string
  i18nFormat: boolean
  children: MetadataItem[]
}

defineOptions({ name: 'StoreHome' })

const DEFAULT_METADATA_URL = 'https://cdn.north.devlive.org/applications/datacap/metadata/metadata.json'

// URL validation helper
const isValidUrl = (urlString: string): boolean => {
  try {
    new URL(urlString)
    return true
  }
  catch {
    return false
  }
}

const { t } = useI18n()
// @ts-ignore
const { loadingState } = useI18nHandler()
const loading = ref(false)
const metadata = ref<Metadata | null>(null)
const version = ref(PackageUtils.get('version'))
const info = ref<MetadataItem | null>(null)
const infoVisible = ref(false)

// Default to first plugin type found
const activeTab = ref('')

// Get all unique plugin types
const pluginTypes = computed(() => {
  if (!metadata.value?.children?.length) {
    return []
  }

  const types = new Set(metadata.value.children.map(plugin => plugin.type))
  return Array.from(types)
})

// Group plugins by type
const pluginsByType = (type: string) => {
  if (!metadata.value?.children?.length) {
    return []
  }
  return metadata.value.children.filter(plugin => plugin.type === type)
}

// Get description for a type (could be extended to add custom descriptions per type)
const typeDescription = (type: string) => {
  const defaultDescriptions: Record<string, string> = {
    'FileSystem': 'File system plugins provide storage integrations',
    'Connector': 'Connector plugins enable data source connections',
    'Visualization': 'Visualization plugins provide data display capabilities'
    // Add more defaults for other types
  }

  return defaultDescriptions[type] || `${ type } plugins for DataCap`
}

const metadataUrl = computed({
  get: () => {
    const savedUrl = localStorage.getItem('metadataUrl')
    return savedUrl && isValidUrl(savedUrl) ? savedUrl : DEFAULT_METADATA_URL
  },
  set: (value: string) => {
    const trimmedUrl = value?.trim()
    if (trimmedUrl && isValidUrl(trimmedUrl)) {
      localStorage.setItem('metadataUrl', trimmedUrl)
    }
    else {
      localStorage.removeItem('metadataUrl')
    }
  }
})
const applyMetadataUrl = ref(metadataUrl.value)

watch(loadingState, async (newVal) => {
  if (!newVal && !metadata.value) {
    await loadMetadata()
  }
})

watch(pluginTypes, (newTypes) => {
  // Set active tab to first type when types are loaded
  if (newTypes.length && !activeTab.value) {
    activeTab.value = newTypes[0]
  }
})

const loadMetadata = async () => {
  loading.value = true
  try {
    const url = metadataUrl.value
    if (!isValidUrl(url)) {
      throw new Error('Invalid metadata URL')
    }

    const response = await fetch(url)
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${ response.status }`)
    }

    const data = await response.json()
    metadata.value = data

    const installResponse = await PluginService.getPlugins()
    if (!installResponse.status) {
      message.error(installResponse.message)
      return
    }

    // Bind installation information
    metadata.value!.children.forEach(item => {
      item.loading = false
      installResponse.data.some((installedPlugin: { key: string, loadTime: string, version: string }) => {
        if (installedPlugin.key === item.key) {
          item.installed = true
          item.installTime = installedPlugin.loadTime
          item.installVersion = installedPlugin.version
          return true
        }
        return false
      })
    })
  }
  catch (error: any) {
    if (error instanceof TypeError) {
      message.error(t('common.tip.pageNotNetwork'))
    }
    else {
      message.error(`${ t('common.pageNotFoundTip') }: ${ error.message }`)
    }

    // Reset to default URL if current URL is invalid
    if (!isValidUrl(metadataUrl.value)) {
      metadataUrl.value = DEFAULT_METADATA_URL
      await loadMetadata()
    }
  }
  finally {
    loading.value = false
  }
}

// Install plugin
const onInstall = async (item: MetadataItem) => {
  if (item.loading) {
    return
  }
  try {
    item.loading = true
    const installResponse = await PluginService.install({ name: item.key, url: item.url })
    if (installResponse.status) {
      message.success(t('common.installSuccess'))
    }
    else {
      message.error(installResponse.message)
    }

    await loadMetadata()
  }
  finally {
    item.loading = false
  }
}

// Uninstall plugin
const onUninstall = async (item: MetadataItem) => {
  if (item.loading) {
    return
  }
  try {
    item.loading = true
    const unInstallResponse = await PluginService.uninstall(item.label)
    if (unInstallResponse.status) {
      message.success(t('common.uninstallSuccess'))
    }
    else {
      message.error(unInstallResponse.message)
    }

    await loadMetadata()
  }
  finally {
    item.loading = false
  }
}

const onSave = async () => {
  try {
    const url = applyMetadataUrl.value.trim()
    if (!isValidUrl(url)) {
      message.error('Invalid Url')
      return
    }

    metadataUrl.value = url
    message.success(t('common.successfully'))

    await loadMetadata()
  }
  catch (error) {
    message.error(t('common.error'))
  }
}

const onVisibleInfo = (item: MetadataItem | null, opened?: boolean) => {
  infoVisible.value = !!opened
  if (opened) {
    info.value = item
  }
}

onBeforeMount(() => {
  if (!loadingState.value) {
    loadMetadata()
  }
})
</script>
