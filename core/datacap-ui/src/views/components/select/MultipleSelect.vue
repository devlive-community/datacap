<template>
  <DropdownMenu>
    <DropdownMenuTrigger as-child>
      <Button variant="outline" class="col-span-3">
        {{ $t('function.tip.selectPluginHolder') }}
        <span v-if="plugin"> [ {{ plugin.split(',').length }} ]</span>
      </Button>
    </DropdownMenuTrigger>
    <DropdownMenuContent class="w-56 max-h-48 overflow-y-auto">
      <DropdownMenuCheckboxItem class="cursor-pointer" v-for="plugin in plugins" v-model:checked="plugin.checked" @update:checked="setChecked">
        {{ (plugin as any).name }}
      </DropdownMenuCheckboxItem>
    </DropdownMenuContent>
  </DropdownMenu>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { DropdownMenu, DropdownMenuCheckboxItem, DropdownMenuContent, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuTrigger } from '@/components/ui/dropdown-menu'
import { Button } from '@/components/ui/button'

export default defineComponent({
  name: 'MultipleSelect',
  components: {
    DropdownMenu, DropdownMenuCheckboxItem, DropdownMenuContent, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuTrigger,
    Button
  },
  props: {
    plugins: {
      type: Array,
      default: []
    }
  },
  data()
  {
    return {
      plugin: null as string | null | undefined
    }
  },
  created()
  {
    this.setChecked()
  },
  methods: {
    setChecked()
    {
      this.plugin = (this.plugins as { checked: boolean; name: string }[])
          .filter(it => it.checked)
          .map(it => it.name)
          .join(',')
      this.$emit('changeValue', this.plugin)
    }
  }
})
</script>
