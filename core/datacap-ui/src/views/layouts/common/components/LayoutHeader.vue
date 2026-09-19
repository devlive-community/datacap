<template>
  <a-alert banner show-icon closable class="dc-header-banner">
    <template #message>
      <router-link to="/admin/chat" target="_blank">
        Support ChatGPT
      </router-link>
    </template>
  </a-alert>

  <!-- 白底铺满整行：a-layout 默认灰底会在宽屏下从两侧透出来 -->
  <div class="dc-header__bar">
    <div class="container">
      <div class="flex items-center">
        <router-link to="/" class="flex items-center gap-2 shrink-0">
          <a-avatar src="/static/images/logo.png" alt="DataCap Logo" :size="34"/>
          <span class="dc-header__wordmark">DataCap</span>
        </router-link>

        <div class="ml-6 flex-1">
          <a-menu mode="horizontal" class="dc-header__menu" :selectedKeys="selectedKeys">
            <template v-for="item in activeMenus" :key="item.id">
              <a-sub-menu v-if="item.children" :key="item.id">
                <template #title>
                  <div class="flex items-center space-x-2">
                    <component v-if="item.icon" :is="menuIcons[item.icon]" :style="{ fontSize: '18px' }"/>
                    <span>{{ item.i18nKey ? $t(item.i18nKey) : 'Unknown' }}</span>
                  </div>
                </template>

                <a-menu-item v-for="children in item.children" :key="children.url">
                  <router-link :to="children.url" class="flex items-center space-x-2">
                    <component v-if="children.icon" :is="menuIcons[children.icon]" :style="{ fontSize: '18px' }"/>
                    <span>{{ children.i18nKey ? $t(children.i18nKey) : 'Unknown' }}</span>
                  </router-link>
                </a-menu-item>
              </a-sub-menu>

              <a-menu-item v-else :key="item.url">
                <router-link :to="item.url" class="flex items-center space-x-2">
                  <component v-if="item.icon" :is="menuIcons[item.icon]" :style="{ fontSize: '18px' }"/>
                  <span>{{ item.i18nKey ? $t(item.i18nKey) : 'Unknown' }}</span>
                </router-link>
              </a-menu-item>
            </template>
          </a-menu>
        </div>

        <a-space :size="16">
          <!-- Language Switcher -->
          <a-tooltip :title="$t('common.feedback')">
            <a href="https://github.com/devlive-community/datacap" target="_blank" rel="noopener noreferrer"
               class="dc-header__help">
              <QuestionCircleOutlined :style="{ fontSize: '17px' }"/>
              <span>{{ $t('common.help') }}</span>
            </a>
          </a-tooltip>
          <LanguageSwitcher @changeLanguage="onChangeLanguage"/>

          <div v-if="userInfo">
            <a-popover trigger="click" placement="bottomRight">
              <template #content>
                <div class="dc-notify" style="width: 340px; max-height: 300px; overflow-y: auto;" @scroll="onNotifyScroll">
                  <div v-if="messages.length === 0" class="p-4 text-center text-sm text-gray-400">
                    {{ $t('common.noData') }}
                  </div>

                  <div v-for="(item, index) in messages"
                       :key="index"
                       class="dc-notify__item"
                       @click="handleNotificationClick(item)">
                    <div class="mt-1 text-sm text-gray-600">
                      <div v-if="item.entityExists" class="flex space-x-1">
                        <span>{{ $t(`common.${ item.entityType?.toLowerCase() || '' }`) }}</span>

                        <template v-if="item.entityType === 'DATASET'">
                          <router-link :to="`/admin/dataset/info/${item.entityCode}`" target="_blank" class="hover:text-blue-400 flex items-center">
                            [ {{ item.entityName }} ]
                          </router-link>
                        </template>

                        <template v-else>
                          <router-link class="hover:text-blue-400" :to="'/' + item.entityType + '/' + item.entityCode">[ {{ item.entityName }} ]</router-link>
                        </template>

                        <span>{{ $t(`common.${ item.type?.toLowerCase() || '' }`) }}</span>
                      </div>
                      <div v-else>
                        {{ $t(`common.${ item.entityType?.toLowerCase() || '' }`) }} [ {{ item.entityName }} ] {{ $t(`common.${ item.type?.toLowerCase() || '' }`) }}
                      </div>
                    </div>

                    <div class="text-xs text-gray-400 mt-1">{{ item.createTime }}</div>
                  </div>
                </div>
              </template>

              <a-badge :count="userInfo?.unreadCount || 0">
                <BellOutlined class="hover:text-blue-400 cursor-pointer" :style="{ fontSize: '20px' }"/>
              </a-badge>
            </a-popover>
          </div>

          <!-- User Info -->
          <a-space v-if="!isLoggedIn">
            <router-link to="/auth/signin">
              <a-button type="primary">{{ $t('user.common.signin') }}</a-button>
            </router-link>
            <router-link to="/auth/signup">
              <a-button>{{ $t('user.common.signup') }}</a-button>
            </router-link>
          </a-space>
          <div v-else>
            <a-dropdown placement="bottomRight">
              <div class="flex items-center gap-2 cursor-pointer">
                <a-avatar :size="32"
                          :src="userInfo?.avatarConfigure?.path"
                          :alt="userInfo?.username">
                </a-avatar>
                <span class="dc-header__username">{{ userInfo?.username }}</span>
                <DownOutlined class="dc-header__chevron"/>
              </div>

              <template #overlay>
                <a-menu>
                  <a-menu-item disabled>
                    <div class="flex flex-col space-y-1">
                      <p class="text-sm font-medium leading-none text-center">{{ userInfo?.username }}</p>
                    </div>
                  </a-menu-item>

                  <a-menu-divider/>

                  <a-menu-item>
                    <router-link to="/admin/user" class="flex items-center space-x-2">
                      <SettingOutlined :style="{ fontSize: '16px' }"/>
                      <span>{{ $t('user.common.setting') }}</span>
                    </router-link>
                  </a-menu-item>

                  <a-menu-item @click="logout">
                    <div class="flex items-center space-x-2">
                      <LogoutOutlined :style="{ fontSize: '16px' }"/>
                      <span>{{ $t('user.common.signout') }}</span>
                    </div>
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
        </a-space>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { useUserStore } from '@/stores/user'
import { storeToRefs } from 'pinia'
import { TokenUtils } from '@/utils/token'
import router from '@/router'
import { createDefaultRouter } from '@/router/default'
import LanguageSwitcher from '@/views/layouts/common/components/components/LanguageSwitcher.vue'
import NotificationService from '@/services/notification'
import { FilterModel } from '@/model/filter.ts'
import { BellOutlined, BgColorsOutlined, DownOutlined, CodeOutlined, DashboardOutlined, HistoryOutlined, HomeOutlined, LogoutOutlined, ProjectOutlined, QuestionCircleOutlined, RightOutlined, SettingOutlined, ToolOutlined, UnorderedListOutlined } from '@ant-design/icons-vue'

defineOptions({ name: 'LayoutHeader' })

/**
 * 顶级菜单高亮：按路径前缀匹配（子页面如 /admin/user/profile 也命中所属菜单）
 */
const selectedKeys = computed(() => {
    const urls: string[] = []
    ;(activeMenus.value || []).forEach((item: any) => {
        if (item.children?.length) {
            item.children.forEach((child: any) => urls.push(child.url))
        }
        else if (item.url) {
            urls.push(item.url)
        }
    })
    const matched = urls.filter(url => route.path.startsWith(url))
                        .sort((a, b) => b.length - a.length)[0]
    return matched ? [matched] : []
})

// 后端菜单返回的是图标名字符串，这里映射到 antd 图标组件
const menuIcons: Record<string, any> = {
    Contrast: BgColorsOutlined,
    Gauge: DashboardOutlined,
    Hammer: ToolOutlined,
    History: HistoryOutlined,
    Home: HomeOutlined,
    List: UnorderedListOutlined,
    SquareChevronRight: RightOutlined,
    SquareDashedBottomCode: CodeOutlined,
    SquareKanban: ProjectOutlined
}

const route = useRoute()
const userStore = useUserStore()
const filter: FilterModel = new FilterModel()
const messages = ref<any[]>([])
const pageIndex = ref<number>(1)
const hasMoreData = ref(true)
const loading = ref(false)

const { userInfo, isLoggedIn, menu: activeMenus } = storeToRefs(userStore)

const emit = defineEmits<{
  changeLanguage: [language: string]
}>()

const fetchMessages = async (value: number = 1, append = false) => {
  filter.page = value
  filter.orders = [{ column: 'createTime', order: 'desc' }]
  loading.value = true
  try {
    const response = await NotificationService.getAll(filter)
    if (response.status && response.data) {
      const items = response.data.content.map((item: any) => {
        item.read = item.isRead
        return item
      })
      messages.value = append ? [...messages.value, ...items] : items
      pageIndex.value = response.data.page
      hasMoreData.value = response.data.page < response.data.totalPage
    }
    else {
      message.error(response.message)
    }
  }
  finally {
    loading.value = false
  }
}

const onNotifyScroll = (event: any) => {
  const target = event.target
  if (loading.value || !hasMoreData.value) {
    return
  }
  if (target.scrollTop + target.offsetHeight >= target.scrollHeight - 10) {
    fetchMessages(pageIndex.value + 1, true)
  }
}

onMounted(async () => {
  if (TokenUtils.getAuthUser()) {
    await userStore.fetchUserInfo()
    await fetchMessages()
  }
})

const logout = () => {
  userStore.logout()
  createDefaultRouter(router)
  router.push('/auth/signin')
}

const onChangeLanguage = (language: string) => {
  emit('changeLanguage', language)
}

const handleNotificationClick = (msg: any) => {
  const { id, code } = msg
  const payload = { id, code, isRead: true }
  NotificationService.saveOrUpdate(payload)
                     .then(response => {
                       if (response.status && response.data) {
                         fetchMessages()
                         userStore.fetchUserInfo()
                       }
                       else {
                         message.error(response.message)
                       }
                     })
}
</script>

<style scoped>
.dc-notify__item {
    padding: 8px 12px;
    border-radius: 6px;
    cursor: pointer;
}

.dc-notify__item:hover {
    background-color: rgba(0, 0, 0, 0.04);
}

.dc-header__bar {
    background: var(--dc-header);
    backdrop-filter: blur(8px);
}

.dc-header__wordmark {
    font-size: 18px;
    font-weight: 700;
    color: var(--dc-text-title);
    letter-spacing: 0.2px;
}

.dc-header__menu.ant-menu-horizontal {
    display: flex;
    align-items: center;
    flex-wrap: nowrap;
    min-width: 0;
    border-bottom: none;
    background: transparent;
    line-height: normal;
}

.dc-header__menu :deep(.ant-menu-item),
.dc-header__menu :deep(.ant-menu-submenu) {
    top: 0;
    height: 38px;
    line-height: 38px;
    margin-inline: 3px;
    padding-inline: 16px;
    border-radius: 999px;
    border-bottom: none !important;
    transition: background-color 0.2s;
}

.dc-header__bar :deep(.ant-menu-horizontal) {
    border-bottom: none;
}

.dc-header__menu :deep(.ant-menu-item::after),
.dc-header__menu :deep(.ant-menu-submenu::after) {
    display: none !important;
}

.dc-header__menu :deep(.ant-menu-item:hover),
.dc-header__menu :deep(.ant-menu-submenu:hover),
.dc-header__menu :deep(.ant-menu-submenu-active) {
    background: rgba(0, 0, 0, 0.04) !important;
}

.dc-header__menu :deep(.ant-menu-item-selected),
.dc-header__menu :deep(.ant-menu-submenu-selected) {
    background: var(--dc-primary-light) !important;
    color: var(--dc-primary-700) !important;
}

.dc-header__help {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 14px;
    color: var(--dc-text-secondary);
    transition: color 0.2s;
}

.dc-header__help:hover {
    color: var(--dc-primary);
}

.dc-header__username {
    font-size: 14px;
    color: var(--dc-text-primary);
}

.dc-header__chevron {
    font-size: 11px;
    color: var(--dc-text-secondary);
}
</style>
