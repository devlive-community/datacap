<template>
  <a-card :title="$t('user.common.assistant')" :bordered="false">
    <template #extra>
      <span class="text-sm text-gray-500">{{ $t('user.tip.assistant') }}</span>
    </template>

    <a-divider class="my-2"/>
    <div class="p-3">
      <AssistantForm/>
    </div>
  </a-card>
</template>

<script setup lang="ts">
import AssistantForm from '@/views/pages/admin/user/profile/components/AssistantForm.vue'

defineOptions({ name: 'AssistantHome' })
</script>
