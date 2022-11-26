<template>
  <Modal v-model="visible" title="Sql" width="70%" :closable="false" :mask-closable="false" @cancel="handlerCancel()">
    <Input v-model="localContent" show-word-limit type="textarea" :rows="8"/>
  </Modal>
</template>
<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  name: "SqlDetail",
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    content: {
      type: String,
      default: () => ''
    }
  },
  data()
  {
    return {
      localContent: this.content
    }
  }
  ,
  methods: {
    handlerCancel()
    {
      this.visible = false;
    }
  },
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible;
      },
      set(value: boolean)
      {
        this.$emit('close', value);
      }
    }
  }
});
</script>
