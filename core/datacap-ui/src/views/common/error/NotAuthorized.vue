<template>
  <BaseLayout>
    <div class='h-screen'>
      <a-result status="403"
                :title="$t('common.pageNotAuthorized')"
                :sub-title="$t('common.pageNotAuthorizedTip')">
        <template #extra>
          <a-button @click="$router.push(String($route.query.redirect || '/'))">
            {{ $t('common.backTo') }}
          </a-button>
          <a-button type="primary" @click="$router.push('/')">
            {{ $t('common.backToHome') }}
          </a-button>
          <a-button @click="$router.push('/auth/signin')">
            {{ $t('common.backToSignin') }}
          </a-button>
        </template>
      </a-result>
    </div>
  </BaseLayout>
</template>

<script setup lang="ts">
import BaseLayout from '@/views/layouts/base/BaseLayout.vue'

defineOptions({ name: 'NotAuthorized' })
</script>
