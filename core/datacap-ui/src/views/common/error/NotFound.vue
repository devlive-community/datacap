<template>
  <BaseLayout>
    <div class="h-screen">
      <a-result status="404"
                :title="$t('common.pageNotFound')"
                :sub-title="$t('common.pageNotFoundTip')">
        <template #extra>
          <a-button v-if="$route.redirectedFrom"
                    @click="$router.push($route.redirectedFrom.fullPath)">
            {{ $t('common.backTo') }}
          </a-button>
          <a-button type="primary" @click="$router.push('/')">
            {{ $t('common.backToHome') }}
          </a-button>
        </template>
      </a-result>
    </div>
  </BaseLayout>
</template>

<script setup lang="ts">
import BaseLayout from '@/views/layouts/base/BaseLayout.vue'

defineOptions({ name: 'NotFound' })
</script>
