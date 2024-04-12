<template>
  <div>
    <Select v-model="applySource" :default-value="value ? value as string : undefined" @update:modelValue="handlerChangeValue">
      <SelectTrigger class="w-full">
        <SelectValue :placeholder="$t('source.tip.selectSource')"/>
      </SelectTrigger>
      <SelectContent>
        <Loader2 v-if="loading" class="w-full justify-center animate-spin"/>
        <SelectGroup v-else>
          <SelectItem v-for="item in items" :value="`${item.id}:${item.type}:${item.code}`" :disabled="!item.available" class="cursor-pointer">
            <TooltipProvider>
              <Tooltip :content="item.type">
                {{ `${ item.name } (${ item.protocol })` }}
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
import Tooltip from '@/views/ui/tooltip'
import { Loader2 } from 'lucide-vue-next'
import { FilterModel } from '@/model/filter.ts'

export default defineComponent({
  name: 'SourceSelect',
  components: {
    Loader2,
    Tooltip,
    Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue
  },
  props: {
    value: {
      type: Object as () => String | undefined
    }
  },
  setup()
  {
    const filter: FilterModel = new FilterModel()
    return {
      filter
    }
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
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      SourceService.getAll(this.filter)
                   .then((response) => {
                     if (response.status) {
                       this.items = response.data.content
                       if (this.value) {
                         this.applySource = this.value as any
                       }
                     }
                   })
                   .finally(() => this.loading = false)
    },
    handlerChangeValue()
    {
      this.$emit('changeValue', this.applySource)
    }
  }
})
</script>
