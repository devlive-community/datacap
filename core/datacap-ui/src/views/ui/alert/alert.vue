<template>
  <Alert :class="typeClass">
    <AlertTitle v-if="title">{{ title }}</AlertTitle>
    <AlertDescription>
      <span v-if="description">{{ description }}</span>
      <slot v-else name="description"/>
    </AlertDescription>
  </Alert>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert'
import '@/views/ui/alert/style.css'

enum Type
{
  success = 'alert-success',
  error = 'alert-error',
  info = 'alert-info',
  warning = 'alert-warning'
}

export default defineComponent({
  name: 'DcAlert',
  components: {
    Alert, AlertDescription, AlertTitle
  },
  props: {
    title: {
      type: String
    },
    description: {
      type: String
    },
    type: {
      type: String,
      default: 'info'
    }
  },
  computed: {
    typeClass()
    {
      return {
        [(Type as any)[this.type]]: true
      }
    }
  }
})
</script>
