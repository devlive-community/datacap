<template>
  <div>
    <Modal :title="$t('common.content')" width="80%" :closable="false" v-model="visible" :maskClosable="false" :z-index="9">
      <div style="height: 350px; max-height: 350px;">
        <VMarkdownView v-if="content" :mode="'light'" :content="content"></VMarkdownView>
      </div>
      <div class="datacap-drawer-footer">
        <Button type="error" @click="handlerCancel()">{{ $t('common.cancel') }}</Button>
      </div>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import {VMarkdownView} from 'vue3-markdown'
import 'vue3-markdown/dist/style.css'

export default defineComponent({
  name: 'MarkdownPreview',
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
  components: {VMarkdownView},
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
        return this.isVisible
      },
      set(value: boolean)
      {
        this.$emit('close', value)
      }
    }
  },
});
</script>
<style scoped>
.datacap-drawer-footer {
  width: 100%;
  position: absolute;
  bottom: 0;
  left: 0;
  border-top: 0px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  background: #fff;
}
</style>
