<template>
  <Select v-model="language" @update:modelValue="handlerChangeLang">
    <SelectTrigger class="w-[150px]">
      <SelectValue :placeholder="$t('common.selectLanguage')"/>
    </SelectTrigger>
    <SelectContent>
      <SelectGroup>
        <SelectLabel>{{ $t('common.region.asia.common') }}</SelectLabel>
        <SelectItem class="pl-6" value="language_zh">{{ $t('common.region.asia.chineseSimple') }}</SelectItem>
        <SelectLabel>{{ $t('common.region.northAmerica.common') }}</SelectLabel>
        <SelectItem class="pl-6" value="language_en">{{ $t('common.region.northAmerica.english') }}</SelectItem>
      </SelectGroup>
    </SelectContent>
  </Select>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '@/components/ui/select'

export default defineComponent({
  name: 'LanguageSwitcher',
  components: {
    Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue
  },
  data()
  {
    return {
      language: 'language_zh'
    }
  },
  methods: {
    handlerChangeLang()
    {
      this.$emit('changeLanguage', this.language)
    }
  }
})
</script>
