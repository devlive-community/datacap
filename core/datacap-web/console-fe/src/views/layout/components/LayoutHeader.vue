<template>
  <div>
    <Header>
      <Menu mode="horizontal" theme="dark" active-name="dashboard" :style="{'z-index':99}">
        <div class="layout-logo">
          <div class="layout-logo logo-text">
            <RouterLink to="/">
              <Avatar style="width: 45px; height: 45px;" src="/static/images/logo.png"></Avatar>
            </RouterLink>
          </div>
        </div>
        <div class="layout-nav">
          <div v-for="menu in availableMenus" v-bind:key="menu.id">
            <Submenu v-if="menu?.children" :name="menu.title">
              <template #title>
                <Icon v-if="menu?.icon" :type="menu.icon"/>
                {{ $t(menu.i18nKey) }}
              </template>
              <MenuItem v-for="children in menu.children" :name="children.title" :to="children.url">
                <Icon v-if="children?.icon" :type="children.icon"/>
                {{ $t(children.i18nKey) }}
              </MenuItem>
            </Submenu>
            <MenuItem v-else :name="menu.title" :to="menu.url">
              <Icon v-if="menu?.icon" :type="menu.icon"/>
              {{ $t(menu.i18nKey) }}
            </MenuItem>
          </div>
        </div>
        <div :style="{lineHeight: '64px', float: 'right', 'margin-right': '10px'}">
          <MenuItem name="feedback" target="_blank" to="https://github.com/EdurtIO/datacap/issues/new/choose">
            <Tooltip placement="top">
              <Icon type="md-help-circle"/>
              <template #content>
                {{ $t('common.feedback') }}
              </template>
            </Tooltip>
          </MenuItem>
          <Submenu name="language">
            <template #title>
              <Icon type="md-infinite"/>
              {{ $t('common.language') }}&nbsp;
            </template>
            <MenuGroup :title="$t('common.default')">
              <MenuItem name="language_en" @click="handlerChangeLang('en')">
                <span>🇺🇸</span>&nbsp;&nbsp;{{ $t('common.english') }}
              </MenuItem>
              <MenuItem name="language_zh" @click="handlerChangeLang('zh_cn')">
                <span>🇨🇳</span>&nbsp;&nbsp;{{ $t('common.chinese') }}
              </MenuItem>
            </MenuGroup>
          </Submenu>
          <Submenu name="profile">
            <template #title>
              <Avatar size="large" style="background-color: #87d068">
                {{ username }}
              </Avatar>&nbsp;
            </template>
            <MenuGroup v-if="username" :title="$t('common.profile')">
              <MenuItem name="profile_index" to="/profile/index">
                <Icon type="md-contact"/>
                {{ $t('common.profile') }}
              </MenuItem>
            </MenuGroup>
            <MenuGroup v-if="username" :title="$t('common.advanced')">
              <MenuItem name="advanced_logout" @click="handlerLogout()">
                <Icon type="md-log-out"/>
                {{ $t('common.logout') }}
              </MenuItem>
            </MenuGroup>
          </Submenu>
        </div>
      </Menu>
    </Header>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import Common from "@/common/Common";
import {AuthResponse} from "@/model/AuthResponse";
import router from "@/router";
import config from '../../../../package.json';
import UserService from "@/services/UserService";

export default defineComponent({
  name: "LayoutHeader",
  setup()
  {
    let username;
    const authUser = JSON.parse(localStorage.getItem(Common.token) || '{}') as AuthResponse;
    if (authUser) {
      username = authUser.username;
    }

    const handlerLogout = () => {
      localStorage.removeItem(Common.token);
      router.push('/auth/signin')
    }
    const language = 'zh_cn';
    const version = config.version;
    return {
      username,
      language,
      version,
      handlerLogout
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  data()
  {
    return {
      availableMenus: []
    }
  },
  methods: {
    handlerInitialize()
    {
      UserService.getMenus()
        .then(response => {
          if (response.status) {
            this.availableMenus = response.data
          }
          else {
            this.$Message.error(response.message)
          }
        })
    },
    handlerChangeLang(language: string)
    {
      this.$emit('changeLanguage', language);
    }
  }
});
</script>

<style scoped>
.layout-logo {
  width: 60px;
  height: 35px;
  border-radius: 3px;
  float: left;
  position: relative;
  top: 15px;
  left: 20px;
  margin-right: 40px;
}

.layout-logo .logo-text {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  margin: auto;
  width: 80px;
  color: #F5F5F5;
  background-color: transparent;
  font-size: 20px;
  font-weight: 600;
  height: 63px;
}

.layout-nav {
  margin: 0 auto;
}

.badge-count {
  top: 1px;
}

.ivu-tag {
  height: 17px;
  line-height: 17px;
  margin: 0;
  padding: 0px 5px;
  font-size: 10px;
}
</style>
