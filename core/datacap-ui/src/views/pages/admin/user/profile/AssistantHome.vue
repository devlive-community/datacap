<template>
  <div class="w-full">
    <div>
      <h3 class="text-lg font-medium">{{ $t('user.common.assistant') }}</h3>
      <p class="text-sm text-muted-foreground">{{ $t('user.tip.assistant') }}</p>
    </div>
    <Separator class="my-4"/>
    <AssistantForm/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Separator } from '@/components/ui/separator'
import AssistantForm from '@/views/pages/admin/user/profile/components/AssistantForm.vue'

export default defineComponent({
  name: 'AssistantHome',
  components: {
    AssistantForm,
    Separator
  }
});
</script>
