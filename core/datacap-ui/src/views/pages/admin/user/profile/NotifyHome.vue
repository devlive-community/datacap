<template>
  <a-card :bordered="false" :title="$t('user.common.notify')">
    <div class="text-sm text-gray-500">{{ $t('user.tip.notify') }}</div>
    <a-divider class="my-2"/>
    <div class="p-3">
      <NotifyForm/>
    </div>
  </a-card>
</template>

<script setup lang="ts">
import NotifyForm from '@/views/pages/admin/user/profile/components/NotifyForm.vue'

defineOptions({ name: 'NotifyHome' })
</script>
