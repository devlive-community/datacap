<template>
  <div class="layout">
    <Layout style="height: 600px;">
      <Sider class="layout-sider"
             hide-trigger>
        <Collapse v-model="activeCsKey"
                  style="background-color: white; padding: 1px;">
          <Panel name="report">
            {{ $t('common.report') }}
            <template #content>
              <div v-for="item in reportItems"
                   draggable="true"
                   unselectable="on"
                   style="border: 1px solid #f3f5f7;"
                   :key="item.id"
                   @drag="handlerDrag(item, $event)"
                   @dragend="handlerDragend(item)">
                <EchartsPreview :width="'180px'"
                                :height="'100px'"
                                :configure="JSON.parse(item.configure)">
                </EchartsPreview>
              </div>
            </template>
          </Panel>
        </Collapse>
      </Sider>
      <Layout>
        <Content class="layout-content"
                 id="content">
          <GridLayout ref="refLayout"
                      :layout="layouts"
                      :responsive="true"
                      :col-num="12"
                      :row-height="30"
                      :is-draggable="true"
                      :is-resizable="true"
                      :vertical-compact="true"
                      :use-css-transforms="true">
            <GridItem v-for="item in layouts"
                      :ref="el => set$Children(el)"
                      :key="item.i"
                      :x="item.x"
                      :y="item.y"
                      :w="item.w"
                      :h="item.h"
                      :i="item.i"
                      :min-h="3"
                      :min-w="3"
                      @resized="handlerResize">
              <EchartsPreview :width="item.width"
                              :key="item.id"
                              :id="item.node.id"
                              :height="item.height"
                              :configure="JSON.parse(item.node.configure)">
              </EchartsPreview>
            </GridItem>
          </GridLayout>
        </Content>
      </Layout>
    </Layout>
  </div>
</template>

<script lang="ts">
import './style.css'
import {defineComponent, nextTick} from "vue"
import {GridItem, GridLayout} from "vue3-grid-layout-next";
import EchartsPreview from "@/components/editor/echarts/EchartsPreview.vue";
import {Filter} from "@/model/Filter";
import ReportService from "@/services/admin/ReportService";

let mouseXY = {x: 0, y: 0}
let DragPos = {x: 0, y: 0, w: 1, h: 1, i: ""}

function addDragOverEvent(e: DragEvent)
{
  mouseXY.x = e.clientX
  mouseXY.y = e.clientY
}

export default defineComponent({
  name: 'DashboardEditorBeta',
  components: {GridItem, GridLayout, EchartsPreview},
  mounted()
  {
    document.addEventListener("dragover", addDragOverEvent)
  },
  data()
  {
    return {
      activeCsKey: 'report',
      reportItems: [],
      mapCache: new Map(),
      columnNumber: 12,
      layouts: []
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      const filter: Filter = new Filter()
      filter.size = 1000
      ReportService.getAll(filter)
        .then(response => {
          if (response.status) {
            this.reportItems = response.data.content
          }
        })
    },
    handlerDrag(node: any, e: DragEvent)
    {
      e.stopPropagation()
      e.preventDefault()
      const t = document.getElementById("content") as HTMLElement
      let parentRect = t.getBoundingClientRect()
      let mouseInGrid = false
      if (mouseXY.x > parentRect.left && mouseXY.x < parentRect.right && mouseXY.y > parentRect.top && mouseXY.y < parentRect.bottom) {
        mouseInGrid = true
      }
      if (mouseInGrid === true && this.layouts.findIndex((item: { i: string; }) => item.i === "drop") === -1) {
        this.layouts.push({
          x: (this.layouts.length * 2) % this.columnNumber,
          y: this.layouts.length + this.columnNumber,
          w: 3,
          h: 4,
          i: "drop",
          width: "350px",
          height: "150px",
          node: node
        })
      }
      let index = this.layouts.findIndex((item: { i: string; }) => item.i === "drop")
      if (index !== -1) {
        try {
          this.$refs.refLayout.defaultGridItem.$el.style.display = "none"
        }
        catch (e) {
          console.log(e)
        }
        let el = this.mapCache.get("drop")
        if (!el) {
          return
        }
        el.dragging = {top: mouseXY.y - parentRect.top, left: mouseXY.x - parentRect.left}
        let pos = el.calcXY(mouseXY.y - parentRect.top, mouseXY.x - parentRect.left)
        if (mouseInGrid === true) {
          this.$refs.refLayout.dragEvent("dragstart", "drop", pos.x, pos.y, 3, 4)
          DragPos.i = String(index)
          DragPos.x = this.layouts[index].x
          DragPos.y = this.layouts[index].y
        }
        if (mouseInGrid === false) {
          this.$refs.refLayout.dragEvent("dragend", "drop", pos.x, pos.y, 3, 4)
          this.layouts = this.layouts.filter((obj: { i: string; }) => obj.i !== "drop")
        }
      }
    },
    handlerDragend(node: any)
    {
      const t = document.getElementById("content") as HTMLElement
      let parentRect = t.getBoundingClientRect()
      let mouseInGrid = false
      if (mouseXY.x > parentRect.left && mouseXY.x < parentRect.right && mouseXY.y > parentRect.top && mouseXY.y < parentRect.bottom) {
        mouseInGrid = true
      }
      if (mouseInGrid === true) {
        this.$refs.refLayout.dragEvent("dragend", "drop", DragPos.x, DragPos.y, 3, 4)
        this.layouts = this.layouts.filter((obj: { i: string; }) => obj.i !== "drop")
        nextTick(() => {
          this.layouts.push({
            x: DragPos.x,
            y: DragPos.y,
            w: 3,
            h: 4,
            width: "350px",
            height: "150px",
            i: DragPos.i,
            node: node
          })
          this.$refs.refLayout.dragEvent("dragend", DragPos.i, DragPos.x, DragPos.y, 3, 4)
          this.mapCache.delete("drop")
        })
      }
    },
    handlerResize(i: string | number, w: number, h: number, x: number, y: number)
    {
      const node = this.layouts[i]
      node.width = `${y}px`
      node.height = `${x}px`
    },
    set$Children(vm: any)
    {
      if (vm && vm.i) {
        this.mapCache.set(vm.i, vm)
      }
    }
  }
})
</script>

<style scoped>
.layout {
  background-color: #eee;
}
</style>
