<template>
  <div class="layout-footer-center">
    <GlobalFooter :links="links">
      <template #copyright>
        <p>Copyright © 2022 Devlive Community All Rights Reserved</p>
        <br/>
        <p>{{ $t('common.version') }}:
          <Text type="danger"
                strong>
            {{ version }}
          </Text>
        </p>
      </template>
    </GlobalFooter>
  </div>
</template>

<script lang="ts">
import {PackageCommon} from "@/common/Package";
import {useI18n} from "vue-i18n";

export default {
  name: "LayoutFooter",
  setup()
  {
    const i18n = useI18n()
    const version = PackageCommon.get('version')
    return {
      i18n,
      version
    }
  },
  data()
  {
    return {
      links: [
        {
          key: 'Documentation',
          title: this.i18n.t('common.documentation'),
          href: 'https://datacap.edurt.io/reference/get_started/install.html',
          blankTarget: true
        },
        {
          key: 'github',
          icon: 'logo-github',
          href: 'https://github.com/devlive-community/datacap',
          blankTarget: true
        },
        {
          key: 'release_note',
          title: this.i18n.t('common.releaseNote'),
          href: 'https://datacap.edurt.io/release-latest.html',
          blankTarget: true
        }
      ]
    }
  }
};
</script>
<style scoped>
.layout-footer-center {
  text-align: center;
}
</style>
