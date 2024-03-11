<template>
  <div class="layout">
    <Row class="layout">
      <Col flex="auto">
        <div id="content">
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
              <Card dis-hover
                    padding="0"
                    :style="{width: item.width, height: item.height}">
                <template #title>
                  {{ item.title ? item.title : $t('dataset.notSpecifiedTitle') }}
                </template>
                <EchartsPreview v-if="item.type === 'QUERY'"
                                :width="item.width"
                                :key="item.id"
                                :id="item.node.id"
                                :height="item.height"
                                :configure="JSON.parse(item.node.configure)">
                </EchartsPreview>
                <VisualView v-else-if="item.type === 'DATASET'"
                            :width="item.width.replace('px', '') - 3 + 'px'"
                            :height="item.height.replace('px', '') - 45 + 'px'"
                            :code="item.node.code"
                            :configuration="JSON.parse(item.node.configure)"
                            :query="JSON.parse(item.node.query)">
                </VisualView>
              </Card>
            </GridItem>
          </GridLayout>
        </div>
      </Col>
    </Row>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue"
import {GridItem, GridLayout} from "vue3-grid-layout-next";
import EchartsPreview from "@/components/editor/echarts/EchartsPreview.vue";
import VisualView from "@/components/visual/VisualView.vue";

export default defineComponent({
  name: 'DashboardViewBeta',
  components: {VisualView, GridItem, GridLayout, EchartsPreview},
  props: {
    layouts: {
      type: Array,
      default: () => []
    }
  },
  data()
  {
    return {
      loading: false
    }
  }
})
</script>
