<template>
  <div>
    <Select v-model="applySource" style="width: 160px" :loading="loading" @on-change="handlerChangeValue">
      <Option v-for="column in columns"
              :value="column.id + ':' + column.type"
              v-bind:key="column.id"
              :disabled="!column.available">
        <Tooltip transfer :content="column.type">
          <Avatar :src="'/static/images/plugin/' + column.type.split(' ')[0] + '.png'" size="small"/>
          {{ column.name }}
        </Tooltip>
      </Option>
    </Select>
  </div>
</template>

<script lang="ts">
import {SourceService} from "@/services/SourceService";
import {defineComponent} from "vue";

export default defineComponent({
  name: "SourceSelect",
  data() {
    return {
      columns: [],
      loading: false,
      applySource: null
    }
  },
  created() {
    this.handlerInitialize(1, 100);
  },
  methods: {
    handlerInitialize(page: number, size: number) {
      this.loading = true;
      new SourceService()
        .getSources(page, size)
        .then((response) => {
          if (response.status) {
            this.columns = response.data.content;
          }
        })
        .finally(() => {
          this.loading = false;
        })
    },
    handlerChangeValue(value: string) {
      this.$emit('changeValue', value);
    }
  }
});
</script>
