<template>
  <a-card>
    <template #cover>
      <a-card :bodyStyle="{'text-align': 'center'}">
        <a-card-meta>
          <template #title>
            <a-avatar style="background-color: #87d068">{{ username }}</a-avatar>
          </template>
          <template #description>{{ username }}</template>
        </a-card-meta>
        <a-divider></a-divider>
        <a-menu style="width: 199px; margin-left: -24px;">
          <a-menu-item key="public">
            <template #icon>
              <profile-outlined/>
            </template>
            <router-link to="/profile/public">
              {{ $t('setting.profile') }}
            </router-link>
          </a-menu-item>
        </a-menu>
      </a-card>
    </template>
  </a-card>
</template>

<script lang="ts">
import Common from "@/common/Common";
import {AuthResponse} from "@/model/AuthResponse";

import {defineComponent} from "vue";

export default defineComponent({
  name: "ProfileLayoutSidebar",
  components: {},
  setup()
  {
    let username;
    const authUser = JSON.parse(localStorage.getItem(Common.token) || '{}') as AuthResponse;
    if (authUser) {
      username = authUser.username;
    }
    return {
      username
    }
  }
});
</script>
