<template>
  <a-card :bordered="false" :title="$t('user.common.modifyPassword')">
    <div class="text-sm text-gray-500">{{ $t('user.tip.modifyPassword') }}</div>
    <a-divider class="my-2"/>
    <div class="p-3">
      <PasswordForm/>
    </div>
  </a-card>
</template>

<script setup lang="ts">
import PasswordForm from '@/views/pages/admin/user/security/components/PasswordForm.vue'

defineOptions({ name: 'PasswordHome' })
</script>
