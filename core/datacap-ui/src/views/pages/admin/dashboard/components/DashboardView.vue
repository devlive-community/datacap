<template>
  <div class="w-full">
    <GridLayout ref="refLayout"
                :layout="layouts"
                :responsive="true"
                :col-num="12"
                :row-height="30"
                :is-draggable="false"
                :is-resizable="false"
                :vertical-compact="true"
                :use-css-transforms="true">
      <GridItem v-for="item in layouts"
                :key="item.i"
                :x="item.x"
                :y="item.y"
                :w="item.w"
                :h="item.h"
                :i="item.i"
                :min-h="3"
                :is-resizable="false"
                :min-w="3">
        <Card :style="{width: item.width, height: item.height}">
          <CardHeader class="border-b p-4">
            <CardTitle>{{ item.title ? item.title : $t('dataset.common.notSpecifiedTitle') }}</CardTitle>
          </CardHeader>
          <CardContent class="p-0">
            <EchartsPreview v-if="item.type === 'QUERY'"
                            :width="item.width"
                            :key="item.id"
                            :id="item.node.id"
                            :height="item.height"
                            :configure="JSON.parse(item.node.configure)">
            </EchartsPreview>
            <VisualView v-else-if="item.type === 'DATASET'"
                        :width="item.width.replace('px', '') - 3 + 'px'"
                        :height="item.height.replace('px', '') - 55 + 'px'"
                        :code="item.node.code"
                        :configuration="JSON.parse(item.node.configure)"
                        :query="JSON.parse(item.node.query)">
            </VisualView>
          </CardContent>
        </Card>
      </GridItem>
    </GridLayout>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { GridItem, GridLayout } from 'vue3-grid-layout-next'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import VisualView from '@/views/components/visual/VisualView.vue'

export default defineComponent({
  name: 'DashboardView',
  components: {
    VisualView,
    CardContent, CardHeader, CardTitle, Card,
    GridItem, GridLayout
  },
  props: {
    layouts: {
      type: Array as () => any[],
      default: () => []
    }
  }
})
</script>
