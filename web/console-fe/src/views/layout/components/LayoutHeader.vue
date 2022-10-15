<template>
  <a-menu theme="dark" mode="horizontal" :style="{ lineHeight: '64px' }">
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
    </a-sub-menu>
    <a-sub-menu key="language">
      <template #icon>
        <a-dropdown placement="bottom">
          <template #overlay>
            <a-menu>
              <a-menu-item key="en" @click="handlerChangeLang('en')">
                English
              </a-menu-item>
              <a-menu-item key="zhCN" @click="handlerChangeLang('zh_cn')">
                中文
              </a-menu-item>
            </a-menu>
          </template>
          <translation-outlined/>
        </a-dropdown>
      </template>
    </a-sub-menu>
  </a-menu>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {AimOutlined, HomeFilled, SettingOutlined} from '@ant-design/icons-vue';

export default defineComponent({
  name: "LayoutHeader",
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
