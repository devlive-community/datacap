<template>
  <div class="w-full">
    <GridLayout ref="refLayout" :layout="layouts" :responsive="true" :col-num="12" :row-height="30" :is-draggable="false" :is-resizable="false" :vertical-compact="true"
                :use-css-transforms="true">
      <GridItem v-for="item in layouts" :key="item.i" :x="item.x" :y="item.y" :w="item.w" :h="item.h" :i="item.i" :min-h="3" :is-resizable="false" :min-w="3">
        <Card :style="{width: item.width, height: item.height}" body-class="p-0">
          <template #title>{{ item.title ? item.title : $t('dataset.common.notSpecifiedTitle') }}</template>
          <template #extra>
            <Tooltip v-if="item.description" :content="item.description">
              <Info class="cursor-pointer"/>
            </Tooltip>
          </template>
          <VisualView :width="item.width.replace('px', '') - 3 + 'px'" :height="item.height.replace('px', '') - 55 + 'px'" :code="item.node.code"
                      :configuration="JSON.parse(item.node.configure)" :query="JSON.parse(item.node.query)"/>
        </Card>
      </GridItem>
    </GridLayout>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { GridItem, GridLayout } from 'vue3-grid-layout-next'
import Card from '@/views/ui/card'
import VisualView from '@/views/components/visual/VisualView.vue'
import Tooltip from '@/views/ui/tooltip'
import { Info } from 'lucide-vue-next'

export default defineComponent({
  name: 'DashboardView',
  components: {
    Tooltip,
    VisualView,
    Card,
    GridItem, GridLayout,
    Info
  },
  props: {
    layouts: {
      type: Array as () => any[],
      default: () => []
    }
  }
})
</script>
