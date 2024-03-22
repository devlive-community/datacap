<template>
  <Select v-model="language" @update:modelValue="setLangCondition">
    <SelectTrigger class="w-[150px]">
      <SelectValue :placeholder="$t('region.common.selectLanguage')"/>
    </SelectTrigger>
    <SelectContent>
      <SelectGroup>
        <SelectLabel>{{ $t('region.common.asia.default') }}</SelectLabel>
        <SelectItem class="pl-6" value="language_zh_cn">{{ $t('region.common.asia.chineseSimple') }}</SelectItem>
        <SelectLabel>{{ $t('region.common.northAmerica.default') }}</SelectLabel>
        <SelectItem class="pl-6" value="language_en">{{ $t('region.common.northAmerica.english') }}</SelectItem>
      </SelectGroup>
    </SelectContent>
  </Select>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '@/components/ui/select'
import { useI18n } from 'vue-i18n'

export default defineComponent({
  name: 'LanguageSwitcher',
  components: {
    Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue
  },
  setup()
  {
    const {locale} = useI18n()
    const setLangCondition = (language: string) => {
      const prefix = 'language_'
      if (language.startsWith(prefix)) {
        locale.value = language.substring(prefix.length)
      }
      else {
        locale.value = language
      }
    }
    return {
      setLangCondition
    }
  },
  data()
  {
    return {
      language: 'language_zh_cn'
    }
  }
})
</script>
