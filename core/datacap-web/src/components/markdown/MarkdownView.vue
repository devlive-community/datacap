<template>
  <div>
    <div>
      <VMdPreview v-if="content"
                  :text="content">
      </VMdPreview>
    </div>
  </div>
</template>
<script>
import {defineComponent} from "vue";
import VMdPreview from '@kangc/v-md-editor/lib/preview'
import '@kangc/v-md-editor/lib/style/preview.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';

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
  name: "MarkdownView",
  props: {
    content: {
      type: Boolean,
      default: () => false
    }
  },
  components: {VMdPreview}
})
</script>
<style scoped>
</style>
