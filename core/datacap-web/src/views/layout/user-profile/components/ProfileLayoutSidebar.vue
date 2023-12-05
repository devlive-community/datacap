<template>
  <div style="padding: 0 0 15px 15px;">
    <Card dis-hover>
      <template #title>
        <div class="datacap-profile-header">
          <router-link to="/profile/index">
            <Avatar v-if="authUser.avatar"
                    size="large"
                    :src="authUser.avatar">
            </Avatar>&nbsp;
            <Avatar v-else
                    size="large"
                    style="background-color: #87d068">
              {{ username }}
            </Avatar>&nbsp;
          </router-link>
          <p style="margin-top: 5px;">{{ username }}</p>
        </div>
      </template>
      <Menu style="width: 184px; margin-left: -16px;">
        <MenuItem name="setting" to="/profile/public">
          <Icon type="ios-cog"/>
          {{ $t('setting.profile') }}
        </MenuItem>
        <MenuItem name="setting_log" to="/profile/log">
          <Icon type="md-egg"/>
          {{ $t('setting.log') }}
        </MenuItem>
        <MenuItem name="security" to="/profile/account">
          <Icon type="ios-contact"/>
          {{ $t('setting.accountSetting') }}
        </MenuItem>
      </Menu>
    </Card>
  </div>
</template>

<script lang="ts">
import Common from "@/common/Common";
import {AuthResponse} from "@/model/AuthResponse";
import {defineComponent} from "vue";

export default defineComponent({
  name: "ProfileLayoutSidebar",
  setup()
  {
    let username;
    const authUser = JSON.parse(localStorage.getItem(Common.token) || '{}') as AuthResponse;
    if (authUser) {
      username = authUser.username;
    }
    return {
      username,
      authUser
    }
  }
});
</script>
<style scoped>
.datacap-profile-header {
  text-align: center;
}
</style>
