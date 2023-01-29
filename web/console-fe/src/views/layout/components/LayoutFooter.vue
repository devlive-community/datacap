<template>
  <div class="layout-footer-center">
    <GlobalFooter :links="links" :copyright="copyright"/>
  </div>
</template>

<script lang="ts">
export default {
  name: "LayoutFooter",
  data()
  {
    return {
      links: [
        {
          key: 'Documentation',
          title: 'Documentation',
          href: 'https://datacap.incubator.edurt.io/reference/get_started/install.html',
          blankTarget: true
        },
        {
          key: 'github',
          icon: 'logo-github',
          href: 'https://github.com/EdurtIO/incubator-datacap',
          blankTarget: true
        },
        {
          key: 'release_note',
          title: 'Release Note',
          href: 'https://datacap.incubator.edurt.io/release-latest.html',
          blankTarget: true
        }
      ],
      copyright: 'Copyright © 2022 DataCap All Rights Reserved'
    }
  }
};
</script>
<style scoped>
.layout-footer-center {
  text-align: center;
}
</style>
