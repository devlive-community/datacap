<template>
  <div>
    <Alert type="success" banner show-icon>Released Note</Alert>
    <Timeline pending class="datacap-release-note">
      <TimelineItem :color="releaseNote.latest ? 'blue' : 'green'"
                    v-for="releaseNote of ReleaseNotes.releaseNotes" v-bind:key="releaseNote.version">
        <template #dot>
          <Icon :type="releaseNote.latest ? 'md-trophy' : 'md-archive'" :size="20"></Icon>
        </template>
        <div style="margin-top: -5px;">
          <p class="time">
            <Space>
              <Tag :color="releaseNote.latest ? 'primary' : 'success'">{{ releaseNote.version }}</Tag>
              <Tag type="border" :color="releaseNote.latest ? 'primary' : 'success'">{{ releaseNote.time }}</Tag>
              <Time :time="releaseNote.time"/>
            </Space>
          </p>
          <p class="content">
            <v-md-preview :text="releaseNote.content"/>
          </p>
        </div>
      </TimelineItem>
    </Timeline>
  </div>
</template>
<script lang="ts">
import 'highlight.js/styles/monokai.css';
import ReleaseNotes from "@/views/common/release/ReleaseNotes";

export default {
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
