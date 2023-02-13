<template>
  <div>
    <Exception type="403">
      <template #actions>
        <CountDown :target="new Date().getTime() + 10000" @on-end="handlerGoSignIn" v-font="30"/>
        <br/>
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
      localStorage.removeItem(Common.token);
      router.push('/auth/signin');
    }
  }
}
</script>
