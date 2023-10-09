<template>
  <div>
    <Modal :title="$t('common.content')"
           v-model="visible"
           width="80%"
           :closable="false"
           :maskClosable="false">
      <div>
        <VMdPreview v-if="content"
                    :text="content">
        </VMdPreview>
      </div>
      <div class="datacap-drawer-footer">
        <Button type="error"
                @click="handlerCancel()">
          {{ $t('common.cancel') }}
        </Button>
      </div>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';
import VMdPreview from '@kangc/v-md-editor/lib/preview'
import '@kangc/v-md-editor/lib/style/preview.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '../markdown/vuepress.css';

import Prism from 'prismjs';
import 'prismjs/components/prism-json';
import 'prismjs/components/prism-java';
import 'prismjs/components/prism-c';
import 'prismjs/components/prism-css'
import 'prismjs/components/prism-typescript'
import 'prismjs/components/prism-javascript'
import 'prismjs/components/prism-http'
import 'prismjs/components/prism-powershell'
import 'prismjs/components/prism-antlr4'
import 'prismjs/components/prism-scala'
import 'prismjs/components/prism-sass'
import 'prismjs/components/prism-sql'
import 'prismjs/components/prism-json5'
import createLineNumberPlugin from '@kangc/v-md-editor/lib/plugins/line-number/index';
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index';
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css';

VMdPreview.use(vuepressTheme, {
  Prism
});

VMdPreview.use(createLineNumberPlugin())
VMdPreview.use(createCopyCodePlugin())

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
  components: {VMdPreview},
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
