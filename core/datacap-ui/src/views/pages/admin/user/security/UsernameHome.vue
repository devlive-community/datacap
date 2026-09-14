<template>
  <a-card :bordered="false" :title="$t('user.common.modifyUsername')">
    <div class="text-sm text-gray-500">{{ $t('user.tip.modifyUsername') }}</div>
    <a-divider class="my-2"/>
    <div class="p-3">
      <UsernameForm/>
    </div>
  </a-card>
</template>

<script setup lang="ts">
import UsernameForm from '@/views/pages/admin/user/security/components/UsernameForm.vue'

defineOptions({ name: 'UsernameHome' })
</script>
