<template>
  <a-card :bordered="false" :title="$t('user.common.profile')">
    <div class="text-sm text-gray-500">{{ $t('user.tip.profile') }}</div>
    <a-divider class="my-2"/>
    <div class="p-3">
      <ProfileForm/>
    </div>
  </a-card>
</template>

<script setup lang="ts">
import ProfileForm from '@/views/pages/admin/user/profile/components/ProfileForm.vue'

defineOptions({ name: 'ProfileHome' })
</script>
