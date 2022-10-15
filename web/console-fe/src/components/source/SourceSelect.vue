<template>
  <div>
    <a-select v-model:value="applySource" size="small" style="width: 160px" @change="handlerChangeValue($event)">
      <template #suffixIcon>
        <meh-outlined class="ant-select-suffix"/>
      </template>
      <a-select-option v-for="column in columns" :value="column.id" v-bind:key="column.id">
        {{ column.name }}
      </a-select-option>
    </a-select>
  </div>
</template>

<script lang="ts">
import {SourceService} from "@/services/SourceService";
import {defineComponent} from "vue";

export default defineComponent({
  name: "SourceSelectComponent",
  components: {},
  data()
  {
    return {
      columns: [],
      loading: false,
      applySource: null
    }
  },
  created()
  {
    this.handlerInitialize(1, 100);
  },
  methods: {
    handlerInitialize(page: number, size: number)
    {
      this.loading = true;
      new SourceService()
        .getSources(page, size)
        .then((response) => {
          if (response.status) {
            this.columns = response.data.content;
          }
          this.loading = false;
        })
    },
    handlerChangeValue(value: string)
    {
      this.$emit('changeValue', value);
    }
  }
});
</script>
