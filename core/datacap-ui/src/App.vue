<template>
  <a-config-provider :theme="antdTheme" :locale="antdLocale">
    <div id="datacap">
      <RouterView/>
    </div>
  </a-config-provider>
</template>

<script setup lang="ts">
import { useTheme } from '@/composables/useTheme'
import { useAntdLocale } from '@/composables/useAntdLocale'

// 全局主题（明/暗）与 antd 语言，随 useTheme / vue-i18n 响应式变化
const { antdTheme } = useTheme()
const { antdLocale } = useAntdLocale()
</script>
