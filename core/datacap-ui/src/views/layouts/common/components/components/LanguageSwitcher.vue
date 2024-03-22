<template>
  <Select v-model="language" @update:modelValue="setLangCondition">
    <SelectTrigger class="w-[150px]">
      <SelectValue :placeholder="$t('common.selectLanguage')"/>
    </SelectTrigger>
    <SelectContent>
      <SelectGroup>
        <SelectLabel>{{ $t('common.region.asia.common') }}</SelectLabel>
        <SelectItem class="pl-6" value="language_zh_cn">{{ $t('common.region.asia.chineseSimple') }}</SelectItem>
        <SelectLabel>{{ $t('common.region.northAmerica.common') }}</SelectLabel>
        <SelectItem class="pl-6" value="language_en">{{ $t('common.region.northAmerica.english') }}</SelectItem>
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
