<template>
  <a-select v-model:value="language"
            :placeholder="$t('region.common.selectLanguage')"
            style="width: 120px;"
            @change="changeLanguage">
    <a-select-opt-group :label="$t('region.common.asia.default')">
      <a-select-option value="language_zh-cn" :label="$t('region.common.asia.chineseSimple')">
        {{ $t('region.common.asia.chineseSimple') }}
      </a-select-option>
    </a-select-opt-group>

    <a-select-opt-group :label="$t('region.common.northAmerica.default')">
      <a-select-option value="language_en" :label="$t('region.common.northAmerica.english')">
        {{ $t('region.common.northAmerica.english') }}
      </a-select-option>
    </a-select-opt-group>
  </a-select>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useI18nHandler } from '@/i18n/I18n'

// @ts-ignore
const { loadLocale } = useI18nHandler()
const language = ref('language_zh-cn')

const changeLanguage = async (value: string) => {
  const prefix = 'language_'
  if (typeof value === 'string' && value.startsWith(prefix)) {
    const locale = value.substring(prefix.length)
    await loadLocale(locale)
    // antd 内置组件文案通过 <a-config-provider :locale> 跟随 vue-i18n，
    // 不再需要 view-shadcn-ui 的 setLocale
    language.value = value
  }
}

onMounted(() => {
  const currentLocale = localStorage.getItem('locale')
  if (currentLocale) {
    language.value = `language_${ currentLocale }`
  }
})
</script>
