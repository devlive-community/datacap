<template>
  <div id="app">
    <router-view/>
    <div class="pdfContent"
         id="watermark-parent">
    </div>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import watermark from 'watermark-dom';
import {TokenCommon} from "@/common/TokenCommon";
import router from "@/router";

export default defineComponent({
  // Fixed ResizeObserver loop completed with undelivered notifications
  setup()
  {
    const debounce = (callback: (...args: unknown[]) => void, delay: number) => {
      let tid: unknown;
      return function (...args: unknown[]) {
        const ctx = self;
        tid && clearTimeout(tid as number);
        tid = setTimeout(() => {
          callback.apply(ctx, args);
        }, delay);
      };
    };

    const _ = (window as any).ResizeObserver;
    (window as any).ResizeObserver = class ResizeObserver
      extends _
    {
      constructor(callback: (...args: unknown[]) => void)
      {
        callback = debounce(callback, 20);
        super(callback);
      }
    };
  },
  mounted()
  {
    setTimeout(() => {
      const authUser = TokenCommon.getAuthUser()
      if (authUser) {
        const applyWatermark = authUser.username ? authUser.username : 'DataCap';
        watermark['watermark'].load({
          watermark_txt: applyWatermark,
          watermark_x: 20,
          watermark_y: 20,
          watermark_rows: 0,
          watermark_cols: 0,
          watermark_x_space: 100,
          watermark_y_space: 50,
          watermark_color: 'black',
          watermark_fontsize: '18px',
          watermark_alpha: 0.05,
          watermark_angle: 15,
          watermark_parent_width: 0,
          watermark_parent_height: 0
        });
      }
      else {
        router.push('/common/token_invalid')
      }
    }, 100);
  }
});
</script>
<style lang="scss"></style>
