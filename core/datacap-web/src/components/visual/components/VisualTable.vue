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
import {cloneDeep} from "lodash";

export default {
  name: 'VisualTable',
  props: {
    configuration: {
      type: Configuration
    },
    submitted: {
      type: Boolean,
      default: true
    }
  },
  created()
  {
    setTimeout(() => {
      try {
        const columns = createdTableHeader(this.configuration.headers)
        const options = {
          records: this.configuration.columns,
          columns,
          hover: {
            highlightMode: 'row' as const
          },
          tooltip: {
            isShowOverflowTextTooltip: true,
          }
        }
        const tableInstance = new VTable.ListTable(document.getElementById('content'), options)
        // Add cell tooltip
        tableInstance.on('mouseenter_cell', (args) => {
          const {col, row} = args;
          const rect = tableInstance.getVisibleCellRangeRelativeRect({col, row});
          tableInstance.showTooltip(col, row, {
            content: `${tableInstance.getHeaderField(col, row)} : ${tableInstance.getCellValue(col, row)}`,
            referencePosition: {rect, placement: VTable.TYPES.Placement.right},
            className: 'defineTooltip',
            style: {bgColor: 'black', color: 'white', arrowMark: true}
          })
        })
        if (this.submitted) {
          const cloneOptions = cloneDeep(this.configuration)
          cloneOptions.headers = []
          cloneOptions.columns = []
          this.$emit('commitOptions', cloneOptions)
        }
      }
      catch (e) {
        console.warn(e)
      }
    })
  }
}
</script>
