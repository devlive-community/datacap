<template>
  <div>
    <Header>
      <Menu mode="horizontal" theme="dark" active-name="dashboard">
        <div class="layout-logo">
          <div class="layout-logo logo-text">
            <Badge :text="version" :offset="[14, -12]">
              DataCap
            </Badge>
          </div>
        </div>
        <div class="layout-nav">
          <MenuItem name="dashboard" to="/">
            <Icon type="ios-navigate"></Icon>
            {{ $t('common.home') }}
          </MenuItem>
          <MenuItem name="console" to="/console/index">
            <Icon type="md-browsers"/>
            {{ $t('common.query') }}
          </MenuItem>
          <Submenu name="admin">
            <template #title>
              <Icon type="ios-hammer"/>
              {{ $t('common.admin') }}
            </template>
            <MenuGroup :title="$t('common.default')">
              <MenuItem name="admin_source" to="/admin/source">
                <Icon type="md-appstore"/>
                {{ $t('common.source') }}
              </MenuItem>
              <MenuItem name="admin_snippet" to="/admin/snippet">
                <Icon type="ios-barcode"/>
                {{ $t('common.snippet') }}
              </MenuItem>
            </MenuGroup>
            <MenuGroup :title="$t('common.history')">
              <MenuItem name="admin_history" to="/admin/history">
                <Icon type="ios-book"/>
                {{ $t('common.history') }}
              </MenuItem>
            </MenuGroup>
          </Submenu>
          <Submenu name="settings">
            <template #title>
              <Icon type="ios-cog"/>
              {{ $t('common.settings') }}
            </template>
            <MenuGroup :title="$t('common.default')">
              <MenuItem name="settings_functions" to="/admin/settings/function">
                <Icon type="ios-basket"/>
                {{ $t('common.function') }}
              </MenuItem>
            </MenuGroup>
            <MenuGroup :title="$t('common.template')">
              <MenuItem name="admin_template_sql" to="/admin/template/sql">
                <Icon type="md-browsers"/>
                {{ $t('common.sql') }}
              </MenuItem>
            </MenuGroup>
          </Submenu>
          <MenuItem name="about" to="/common/about">
            <Icon type="md-information-circle"/>
            {{ $t('common.about') }}
          </MenuItem>
        </div>
        <div :style="{lineHeight: '64px', float: 'right', 'margin-right': '10px'}">
          <MenuItem name="feedback" target="_blank" to="https://github.com/EdurtIO/incubator-datacap/issues/new/choose">
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
            <MenuGroup :title="$t('common.profile')">
              <MenuItem name="profile_index" to="/profile/index">
                <Icon type="md-contact"/>
                {{ $t('common.profile') }}
              </MenuItem>
            </MenuGroup>
            <MenuGroup :title="$t('common.advanced')">
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
import {Badge, MenuItem} from "view-ui-plus";
import config from '../../../../package.json';

export default defineComponent({
  name: "LayoutHeader",
  components: {Badge, MenuItem},
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
  methods: {
    handlerChangeLang(language: string)
    {
      this.$emit('changeLanguage', language);
    }
  }
});
</script>

<style scoped>
.layout-logo {
  width: 100px;
  height: 30px;
  background: #5b6270;
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
