<template>
  <div>
    <Exception type="500">
      <template #desc>
        Current user is not logged.
      </template>
      <template #actions>
        <Button type="primary" @click="handlerGoSignIn">{{ $t('common.login') }}</Button>
      </template>
    </Exception>
  </div>
</template>
<script>
import router from "@/router";

export default {
  methods: {
    handlerGoSignIn() {
      router.push('/auth/signin');
    }
  }
}
</script>
