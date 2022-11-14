<template>
  <a-card size="small" :bodyStyle="{ padding: '20px'}">
    <template #title>
      <a-alert message="Release Notes" type="info" show-icon/>
    </template>
    <a-timeline :reverse="true">
      <a-timeline-item v-for="releaseNote of ReleaseNotes.releaseNotes" v-bind:key="releaseNote.version">
        <template #dot>
          <check-circle-two-tone :spin="releaseNote.latest" two-tone-color="#52c41a"/>
        </template>
        <p>
          <a-tag>{{ releaseNote.version }}</a-tag>
          <clock-circle-two-tone/>&nbsp;{{ releaseNote.time }}
        </p>
        <vue-markdown-it :source="releaseNote.content"/>
      </a-timeline-item>
    </a-timeline>
  </a-card>
</template>
<script lang="ts">
import VueMarkdownIt from 'vue3-markdown-it';
import 'highlight.js/styles/monokai.css';
import ReleaseNotes from "@/views/common/release/ReleaseNotes";

export default {
  components: {
    VueMarkdownIt
  },
  setup()
  {
    return {
      ReleaseNotes
    }
  }
}
</script>
