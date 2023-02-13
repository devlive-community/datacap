<template>
  <div class="layout">
    <Layout>
      <LayoutHeader @changeLanguage="setLangCondition($event)"/>
      <LayoutContent style="background-color: #FFFFFF; padding: 12px; min-height: 500px"/>
      <LayoutFooter/>
      <LayoutAffix/>
    </Layout>
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
  components: {LayoutHeader, LayoutFooter, LayoutContent, LayoutAffix},
  setup()
  {
    const {locale} = useI18n();
    const setLangCondition = (v: string) => {
      locale.value = v;
    }
    return {
      setLangCondition
    }
  }
}
</script>

<style scoped>
.layout {
  background: #f5f7f9;
  position: relative;
  overflow: hidden;
}

.layout-topMiddleBottom .ivu-menu-light {
  background: none;
  color: #fff
}
</style>
