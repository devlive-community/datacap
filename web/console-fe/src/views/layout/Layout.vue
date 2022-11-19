<template>
  <div>
    <a-layout-header>
      <LayoutHeader @changeLanguage="setLangCondition($event)"/>
    </a-layout-header>
    <a-layout class="layout">
      <a-layout-content style="padding: 10px; background-color: #ffffff;">
        <LayoutContent/>
      </a-layout-content>
    </a-layout>
    <LayoutAffix></LayoutAffix>
    <LayoutFooter/>
  </div>
</template>

<script lang="ts">
import LayoutContent from "@/views/layout/components/LayoutContent.vue";
import LayoutFooter from "@/views/layout/components/LayoutFooter.vue";
import LayoutHeader from "@/views/layout/components/LayoutHeader.vue";
import {useI18n} from 'vue-i18n';
import LayoutAffix from "@/views/layout/components/LayoutAffix.vue";

export default {
  name: "LayoutContainer",
  components: {LayoutAffix, LayoutHeader, LayoutContent, LayoutFooter},
  computed: {},
  setup()
  {
    const {locale} = useI18n();
    const setLangCondition = (v: string) => {
      locale.value = v;
    }
    return {
      setLangCondition
    }
  },
  methods: {}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
</style>
