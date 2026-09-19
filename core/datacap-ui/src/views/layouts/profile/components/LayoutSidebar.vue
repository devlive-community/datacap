<template>
  <div class="dc-profile-sidebar">
    <div class="dc-profile-sidebar__head">
      <div class="dc-profile-sidebar__badge">
        <SettingOutlined :style="{ fontSize: '18px' }"/>
      </div>
      <div class="min-w-0">
        <div class="dc-profile-sidebar__title">{{ $t('common.system') }}</div>
        <div class="dc-profile-sidebar__subtitle">{{ $t('user.tip.settings') }}</div>
      </div>
    </div>

    <a-menu mode="inline"
            class="dc-profile-sidebar__menu"
            :selectedKeys="[route.path]">
      <a-menu-item v-for="item in items"
                   :key="item.href">
        <router-link :to="item.href" class="dc-profile-sidebar__link">
          <component :is="item.icon" class="dc-profile-sidebar__link-icon"/>
          <span>{{ item.title }}</span>
        </router-link>
      </a-menu-item>
    </a-menu>

    <div class="dc-profile-sidebar__promo">
      <div class="dc-profile-sidebar__promo-art">
        <DatabaseOutlined :style="{ fontSize: '26px', color: '#fff' }"/>
      </div>
      <div class="dc-profile-sidebar__promo-title">{{ $t('user.tip.promoTitle') }}</div>
      <div class="dc-profile-sidebar__promo-desc">{{ $t('user.tip.promoDesc') }}</div>
      <div class="dc-profile-sidebar__promo-arrow">
        <ArrowRightOutlined :style="{ fontSize: '12px' }"/>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRoute } from 'vue-router'
import {
  ArrowRightOutlined,
  BellOutlined,
  CodeOutlined,
  DatabaseOutlined,
  IdcardOutlined,
  LockOutlined,
  ProfileOutlined,
  RobotOutlined,
  SettingOutlined,
  TrophyOutlined,
  UserOutlined
} from '@ant-design/icons-vue'

defineOptions({ name: 'LayoutSidebar' })

const { t } = useI18n()
const route = useRoute()

const items = computed(() => [
  { title: t('user.common.profile'), href: '/admin/user/profile', icon: UserOutlined },
  { title: t('user.common.notify'), href: '/admin/user/notify', icon: BellOutlined },
  { title: t('user.common.info'), href: '/admin/user/info', icon: TrophyOutlined },
  { title: t('user.common.log'), href: '/admin/user/log', icon: ProfileOutlined },
  { title: t('user.common.editor'), href: '/admin/user/editor', icon: CodeOutlined },
  { title: t('user.common.assistant'), href: '/admin/user/assistant', icon: RobotOutlined },
  { title: t('user.common.modifyPassword'), href: '/admin/user/password', icon: LockOutlined },
  { title: t('user.common.modifyUsername'), href: '/admin/user/username', icon: IdcardOutlined }
])
</script>

<style scoped>
.dc-profile-sidebar {
    display: flex;
    flex-direction: column;
    width: 264px;
    background: #fff;
    border: 1px solid rgba(0, 0, 0, 0.06);
    border-radius: 12px;
    padding: 16px 12px;
    min-height: calc(100vh - 220px);
}

.dc-profile-sidebar__head {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 4px 8px 14px;
}

.dc-profile-sidebar__badge {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 38px;
    height: 38px;
    border-radius: 10px;
    background: rgba(22, 119, 255, 0.1);
    color: #1677ff;
    flex-shrink: 0;
}

.dc-profile-sidebar__title {
    font-size: 15px;
    font-weight: 600;
    color: rgba(0, 0, 0, 0.88);
}

.dc-profile-sidebar__subtitle {
    font-size: 12px;
    color: rgba(0, 0, 0, 0.45);
    margin-top: 2px;
}

.dc-profile-sidebar__menu {
    flex: 1;
    border-inline-end: none !important;
    background: transparent;
}

.dc-profile-sidebar__link {
    display: flex;
    align-items: center;
    gap: 8px;
}

.dc-profile-sidebar__link-icon {
    font-size: 15px;
}

.dc-profile-sidebar__promo {
    position: relative;
    margin: 12px 4px 4px;
    padding: 16px 14px 14px;
    border-radius: 12px;
    background: linear-gradient(135deg, rgba(22, 119, 255, 0.08), rgba(82, 196, 26, 0.1));
    overflow: hidden;
}

.dc-profile-sidebar__promo-art {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 44px;
    height: 44px;
    border-radius: 12px;
    background: linear-gradient(135deg, #1677ff, #36cfc9);
    margin-bottom: 10px;
}

.dc-profile-sidebar__promo-title {
    font-size: 13px;
    font-weight: 600;
    color: rgba(0, 0, 0, 0.88);
}

.dc-profile-sidebar__promo-desc {
    font-size: 11px;
    color: rgba(0, 0, 0, 0.45);
    margin-top: 4px;
    padding-right: 24px;
    line-height: 1.5;
}

.dc-profile-sidebar__promo-arrow {
    position: absolute;
    right: 12px;
    bottom: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 22px;
    height: 22px;
    border-radius: 50%;
    background: rgba(22, 119, 255, 0.12);
    color: #1677ff;
}
</style>
