<template>
  <div>
    <Timeline pending class="datacap-release-note">
      <TimelineItem v-for="releaseNote of ReleaseNotes.releaseNotes" v-bind:key="releaseNote.version">
        <p class="time">
          <Tag :color="releaseNote.latest ? 'primary' : 'default'">{{ releaseNote.version }}</Tag>
          <Tag color="default">{{ releaseNote.time }}</Tag>
        </p>
        <p class="content">
          <vue-markdown-it :source="releaseNote.content"/>
        </p>
      </TimelineItem>
    </Timeline>
  </div>
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
<style scoped>
.datacap-release-note {
  padding: 20px;
}

.time {
  font-size: 14px;
  font-weight: bold;
}

.content {
  padding-left: 5px;
}
</style>
