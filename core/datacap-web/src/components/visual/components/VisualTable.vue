<template>
  <div>
    <div id="content"
         style="width: 100%; height:400px;">
    </div>
  </div>
</template>
<script lang="ts">
import {Configuration} from "@/components/visual/Configuration";
import * as VTable from '@visactor/vtable'
import {createdTableHeader} from "../Utils";

export default {
  name: 'VisualTable',
  props: {
    configuration: {
      type: Configuration
    }
  },
  created()
  {
    setTimeout(() => {
      const columns = createdTableHeader(this.configuration.headers)
      const options = {
        records: this.configuration.columns,
        columns
      }
      window['tableInstance'] = new VTable.ListTable(document.getElementById('content'), options)
    })
  }
}
;
</script>
