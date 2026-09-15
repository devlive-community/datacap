<template>
  <a-menu mode="inline" :selectedKeys="[route.path]" style="width: 200px;">
    <a-menu-item v-for="item in items" :key="item.href">
      <router-link :to="item.href">{{ item.title }}</router-link>
    </a-menu-item>
  </a-menu>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRoute } from 'vue-router'

defineOptions({ name: 'LayoutSidebar' })

const { t } = useI18n()
const route = useRoute()

const items = computed(() => [
  { title: t('user.common.profile'), href: '/admin/user/profile' },
  { title: t('user.common.notify'), href: '/admin/user/notify' },
  { title: t('user.common.info'), href: '/admin/user/info' },
  { title: t('user.common.log'), href: '/admin/user/log' },
  { title: t('user.common.editor'), href: '/admin/user/editor' },
  { title: t('user.common.assistant'), href: '/admin/user/assistant' },
  { title: t('user.common.modifyPassword'), href: '/admin/user/password' },
  { title: t('user.common.modifyUsername'), href: '/admin/user/username' }
])
</script>
