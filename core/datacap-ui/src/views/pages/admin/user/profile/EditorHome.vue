<template>
  <a-card :bordered="false" :title="$t('user.common.editor')">
    <div class="text-sm text-gray-500">{{ $t('user.tip.editor') }}</div>
    <a-divider class="my-2"/>
    <div class="p-3">
      <EditorForm/>
    </div>
  </a-card>
</template>

<script setup lang="ts">
import EditorForm from '@/views/pages/admin/user/profile/components/EditorForm.vue'

defineOptions({ name: 'EditorHome' })
</script>
