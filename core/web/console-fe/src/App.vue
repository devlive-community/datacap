<template>
  <div id="app">
    <router-view></router-view>
    <div class="pdfContent" id="watermark-parent">
    </div>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import watermark from 'watermark-dom';
import Common from "@/common/Common";
import {AuthResponse} from "@/model/AuthResponse";

export default defineComponent({
  mounted()
  {
    setTimeout(() => {
      const authUser = JSON.parse(localStorage.getItem(Common.token) || '{}') as AuthResponse;
      if (authUser) {
        watermark['watermark'].load({
          watermark_txt: authUser.username,
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
    }, 100);
  }
});
</script>
<style lang="scss"></style>
