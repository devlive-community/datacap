<template>
  <div :style="{ clear: 'both' }">
    <a-menu theme="dark" mode="horizontal" :style="{lineHeight: '64px', float: 'left', width: '60%'}">
      <a-menu-item>DataCap(incubator)</a-menu-item>
      <a-menu-item key="dashboard">
        <router-link to="/">
          <home-filled/>
          {{ $t('common.home') }}
        </router-link>
      </a-menu-item>
      <a-menu-item key="console">
        <router-link to="/console/index">
          <console-sql-outlined/>
          {{ $t('common.query') }}
        </router-link>
      </a-menu-item>
      <a-sub-menu key="admin">
        <template #icon>
          <setting-outlined/>
        </template>
        <template #title>{{ $t('common.admin') }}</template>
        <a-menu-item key="admin_source">
          <router-link to="/admin/source">
            <aim-outlined/>
            {{ $t('common.source') }}
          </router-link>
        </a-menu-item>
        <a-menu-item key="admin_history">
          <router-link to="/admin/history">
            <history-outlined/>
            {{ $t('common.history') }}
          </router-link>
        </a-menu-item>
        <a-menu-item key="admin_snippet">
          <router-link to="/admin/snippet">
            <code-outlined/>
            {{ $t('common.snippet') }}
          </router-link>
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
    <a-menu theme="dark" mode="horizontal" :style="{lineHeight: '64px', float: 'right'}">
      <a-sub-menu key="language">
        <template #icon>
          <a-dropdown placement="bottom">
            <template #overlay>
              <a-menu style="margin-top: 5px;">
                <a-menu-item key="en" @click="handlerChangeLang('en')">
                  {{ $t('common.english') }}
                </a-menu-item>
                <a-menu-item key="zhCN" @click="handlerChangeLang('zh_cn')">
                  {{ $t('common.chinese') }}
                </a-menu-item>
              </a-menu>
            </template>
            <translation-outlined/>
          </a-dropdown>
        </template>
      </a-sub-menu>
      <a-sub-menu>
        <template #icon>
          <a-dropdown placement="bottomRight">
            <template #overlay>
              <a-menu style="margin-top: 5px;">
                <a-menu-item key="profile" >
                  <router-link to="/profile/index">
                    <user-outlined/>
                    {{ $t('common.profile') }}
                  </router-link>
                </a-menu-item>
                <a-menu-divider></a-menu-divider>
                <a-menu-item @click="handlerLogout">
                  <logout-outlined />
                  {{ $t('common.logout') }}
                </a-menu-item>
              </a-menu>
            </template>
            <a-avatar style="background-color: #87d068">{{ username }}</a-avatar>
          </a-dropdown>
        </template>
      </a-sub-menu>
    </a-menu>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {AimOutlined, HomeFilled, SettingOutlined} from '@ant-design/icons-vue';
import Common from "@/common/Common";
import {AuthResponse} from "@/model/AuthResponse";
import router from "@/router";

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
    return {
      username,
      handlerLogout
    }
  },
  components: {HomeFilled, SettingOutlined, AimOutlined},
  computed: {},
  methods: {
    handlerChangeLang(language: string)
    {
      this.$emit('changeLanguage', language);
    }
  }
});
</script>

<style rel="stylesheet/scss" lang="scss" scoped></style>
