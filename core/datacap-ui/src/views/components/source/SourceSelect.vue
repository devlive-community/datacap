<template>
  <div>
    <Select v-model="applySource" @update:modelValue="handlerChangeValue">
      <SelectTrigger class="w-full">
        <SelectValue :placeholder="$t('source.tip.selectSource')"/>
      </SelectTrigger>
      <SelectContent>
        <Loader2 v-if="loading" class="w-full justify-center animate-spin"/>
        <SelectGroup v-else>
          <SelectItem v-for="item in items" :value="`${item.id}:${item.type}:${item.code}`" :disabled="!item.available">
            <TooltipProvider>
              <Tooltip>
                <TooltipTrigger>{{ `${item.name} (${item.protocol})` }}</TooltipTrigger>
                <TooltipContent>{{ item.type }}</TooltipContent>
              </Tooltip>
            </TooltipProvider>
          </SelectItem>
        </SelectGroup>
      </SelectContent>
    </Select>
  </div>
</template>

<script lang="ts">
import SourceService from '@/services/source'
import { defineComponent } from 'vue'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '@/components/ui/select'
import { SourceModel } from '@/model/source'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'
import { Loader2 } from 'lucide-vue-next'

export default defineComponent({
  name: 'SourceSelect',
  components: {
    Loader2,
    TooltipContent, TooltipTrigger, Tooltip, TooltipProvider,
    Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue
  },
  data()
  {
    return {
      items: [] as SourceModel[],
      loading: false,
      applySource: undefined
    }
  },
  created()
  {
    this.handlerInitialize(1, 100)
  },
  methods: {
    handlerInitialize(page: number, size: number)
    {
      this.loading = true
      SourceService.getSources(page, size)
          .then((response) => {
            if (response.status) {
              this.items = response.data.content
            }
          })
          .finally(() => this.loading = false)
    },
    handlerChangeValue()
    {
      this.$emit('changeValue', this.applySource)
    }
  }
});
</script>
