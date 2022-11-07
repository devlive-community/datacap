<template>
  <a-card size="small" :bodyStyle="{ padding: '20px'}">
    <template #title>
      <a-alert message="Release Notes" type="info" show-icon/>
    </template>
    <a-timeline :reverse="true">
      <a-timeline-item>
        <template #dot>
          <check-circle-two-tone spin two-tone-color="#52c41a"/>
        </template>
        <p>
          <a-tag>1.0.0</a-tag>
          <clock-circle-two-tone/>&nbsp;2022-10-15
        </p>
        <vue-markdown-it :source="releaseNote20221015"/>
      </a-timeline-item>
    </a-timeline>
  </a-card>
</template>
<script lang="ts">
import VueMarkdownIt from 'vue3-markdown-it';
import 'highlight.js/styles/monokai.css';
import releaseNotes from "@/views/common/release/Release20221015";

export default {
  components: {
    VueMarkdownIt
  },
  setup()
  {
    const releaseNote20221015 = releaseNotes.releaseNote20221015
    return {
      releaseNote20221015
    }
  }
}
</script>
