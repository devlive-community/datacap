<template>
  <div style="float: right; margin-right: 60px;">
    <a-affix :offsetBottom="20">
      <a-dropdown placement="top">
        <a-button type="primary" shape="circle">
          <template #icon>
            <appstore-outlined/>
          </template>
        </a-button>
        <template #overlay>
          <a-menu>
            <a-menu-item-group>
              <template #title>
                <a target="_blank" href="https://gitee.com/EdurtIO/incubator-datacap">
                  <img width="15" src='https://gitee.com/EdurtIO/incubator-datacap/widgets/widget_5.svg'>
                  Gitee
                </a>
              </template>
              <a-menu-item>
                <a target="_blank" href="https://gitee.com/EdurtIO/incubator-datacap/members">
                  <fork-outlined/>
                  Forks <img src='https://gitee.com/EdurtIO/incubator-datacap/badge/fork.svg?theme=white' alt='fork'/>
                </a>
              </a-menu-item>
              <a-menu-item>
                <a target="_blank" href="https://gitee.com/EdurtIO/incubator-datacap/stargazers">
                  <star-outlined/>
                  Stars <img src='https://gitee.com/EdurtIO/incubator-datacap/badge/star.svg?theme=white' alt='star'/>
                </a>
              </a-menu-item>
            </a-menu-item-group>
            <a-menu-item-group>
              <template #title>
                <a target="_blank" href="https://github.com/EdurtIO/incubator-datacap">
                  <github-outlined/>
                  GitHub
                </a>
              </template>
              <a-menu-item>
                <a target="_blank" href="https://github.com/EdurtIO/incubator-datacap/fork">
                  <fork-outlined/>
                  Forks <img alt="GitHub stars" src="https://img.shields.io/github/forks/EdurtIO/incubator-datacap?logo=github">
                </a>
              </a-menu-item>
              <a-menu-item>
                <a target="_blank" href="https://github.com/EdurtIO/incubator-datacap/stargazers">
                  <star-outlined/>
                  Stars <img alt="GitHub stars" src="https://img.shields.io/github/stars/EdurtIO/incubator-datacap?logo=github">
                </a>
              </a-menu-item>
            </a-menu-item-group>
          </a-menu>
        </template>
      </a-dropdown>
    </a-affix>
  </div>
</template>

<script lang="ts">
export default {
  name: "LayoutAffix",
  components: {},
  computed: {}
};
</script>
