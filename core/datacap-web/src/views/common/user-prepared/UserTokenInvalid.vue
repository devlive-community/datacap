<template>
  <div>
    <Exception type="500">
      <template #desc>
        Current token is invalid.
      </template>
      <template #actions>
        <Button type="primary" @click="handlerGoSignIn">{{ $t('common.login') }}</Button>
      </template>
    </Exception>
  </div>
</template>
<script>
import router from "@/router";
import Common from "@/common/Common";

export default {
  methods: {
    handlerGoSignIn() {
      localStorage.removeItem(Common.token)
      router.push('/auth/signin');
    }
  }
}
</script>
