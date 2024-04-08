<template>
  <Switch :default-checked="value" @update:checked="handlerChange"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Switch } from '@/components/ui/switch'

export default defineComponent({
  name: 'DcSwitch',
  components: { Switch },
  props: {
    value: {
      type: Boolean
    }
  },
  methods: {
    handlerChange(value: boolean)
    {
      this.$emit('changeValue', value)
    }
  }
})
</script>
