<template>
  <div>
    <Spin fix
          :show="show">
      <Icon type="ios-loading"
            size=18
            class="spin-icon-load">
      </Icon>
      <div>Loading</div>
    </Spin>
  </div>
</template>
<script>
import {defineComponent} from "vue";

export default defineComponent({
  name: "CircularLoading",
  props: {
    show: {
      type: Boolean,
      default: () => false
    }
  }
})
</script>
<style scoped>
.spin-icon-load {
  animation: ani-spin 1s linear infinite;
}

@keyframes ani-spin {
  from {
    transform: rotate(0deg);
  }
  50% {
    transform: rotate(180deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
