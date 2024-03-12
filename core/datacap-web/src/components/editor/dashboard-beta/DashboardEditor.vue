<template>
  <div class="layout">
    <Row class="layout">
      <Col flex="200px"
           style="border-right: #f3f5f7 1px solid;">
        <Collapse v-model="activeCsKey"
                  style="background-color: white; padding: 1px;">
          <Panel name="report">
            {{ $t('common.report') }}
            <template #content>
              <div v-for="item in reportItems"
                   draggable="true"
                   unselectable="on"
                   style="border: 1px solid #f3f5f7; margin-top: 5px;"
                   :key="item.id"
                   @drag="handlerDrag(item, $event)"
                   @dragend="handlerDragend(item)">
                <Card dis-hover
                      padding="0"
                      :style="{width: item.width, height: item.height}">
                  <template #title>
                    {{ item.name ? item.name : $t('dataset.notSpecifiedTitle') }}
                  </template>
                  <EchartsPreview v-if="item.type === 'QUERY'"
                                  :width="'180px'"
                                  :height="'100px'"
                                  :configure="JSON.parse(item.configure)">
                  </EchartsPreview>
                  <VisualView v-else-if="item.type === 'DATASET'"
                              :width="'180px'"
                              :height="'120px'"
                              :code="item.dataset.code"
                              :configuration="JSON.parse(item.configure)"
                              :query="JSON.parse(item.query)">
                  </VisualView>
                </Card>
              </div>
            </template>
          </Panel>
        </Collapse>
      </Col>
      <Col flex="auto">
        <Card dis-hover
              padding="0">
          <template #title>
            <div style="text-align: right;">
              <Button type="primary"
                      icon="md-document"
                      size="small"
                      @click="handlerSaveVisible(true)">
                {{ $t('common.save') }}
              </Button>
            </div>
          </template>
          <div id="content">
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
                <Card dis-hover
                      padding="0"
                      :style="{width: item.width, height: item.height}">
                  <template #title>
                    {{ item.title ? item.title : $t('dataset.notSpecifiedTitle') }}
                  </template>
                  <template #extra>
                    <Button size="small"
                            type="error"
                            shape="circle"
                            icon="md-trash"
                            style="z-index: 1000; position: absolute; right: 0; top: -5px;"
                            @click="handlerRemove(item.i)">
                    </Button>
                  </template>
                  <EchartsPreview v-if="item.type === 'QUERY'"
                                  :width="item.width"
                                  :key="item.id"
                                  :id="item.node.id"
                                  :height="item.height"
                                  :configure="JSON.parse(item.node.configure)">
                  </EchartsPreview>
                  <VisualView v-else-if="item.type === 'DATASET'"
                              :width="item.width.replace('px', '') - 2 + 'px'"
                              :height="item.height.replace('px', '') - 45 + 'px'"
                              :code="item.node.code"
                              :configuration="JSON.parse(item.node.configure)"
                              :query="JSON.parse(item.node.query)">
                  </VisualView>
                </Card>
              </GridItem>
            </GridLayout>
          </div>
        </Card>
      </Col>
    </Row>
    <Modal v-model="configureVisible"
           :title="$t('common.configure')">
      <Form>
        <FormItem :label="$t('common.name')"
                  :label-width="80">
          <Input v-model="formState.name"/>
        </FormItem>
      </Form>
      <template #footer>
        <Button type="primary"
                :loading="saving"
                @click="handlerSave">
          {{ $t('common.save') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>

<script lang="ts">
import './style.css'
import {defineComponent, nextTick} from "vue"
import {GridItem, GridLayout} from "vue3-grid-layout-next";
import EchartsPreview from "@/components/editor/echarts/EchartsPreview.vue";
import {Filter} from "@/model/Filter";
import ReportService from "@/services/admin/ReportService";
import DashboardService from "@/services/DashboardService";
import VisualView from "@/components/visual/VisualView.vue";

let mouseXY = {x: 0, y: 0}
let DragPos = {x: 0, y: 0, w: 1, h: 1, i: ""}

function addDragOverEvent(e: DragEvent)
{
  mouseXY.x = e.clientX
  mouseXY.y = e.clientY
}

export default defineComponent({
  name: 'DashboardEditorBeta',
  components: {VisualView, GridItem, GridLayout, EchartsPreview},
  props: {
    objInfo: {
      type: Object
    }
  },
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
      layouts: [],
      saving: false,
      configureVisible: false,
      formState: {
        name: null,
        version: 'V2',
        reports: [],
        configure: null
      }
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
      setTimeout(() => {
        if (this.objInfo) {
          this.formState = this.objInfo
          this.layouts = JSON.parse(this.formState.configure)
        }
      }, 300)
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
          title: node.name,
          node: {
            id: node.id,
            configure: node.configure
          }
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
            type: node.type,
            title: node.name,
            node: {
              id: node.id,
              configure: node.configure,
              code: node.dataset?.code,
              query: node?.query
            }
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
    handlerRemove(i: string | number)
    {
      this.layouts = this.layouts.filter((obj: { i: string; }) => obj.i !== i)
    },
    handlerSaveVisible(opened: boolean)
    {
      this.configureVisible = opened
    },
    handlerSave()
    {
      this.formState.configure = JSON.stringify(this.layouts)
      this.layouts.forEach((item: { node: { id: any; }; }) => this.formState.reports.push({id: item.node.id}))
      this.saving = true
      DashboardService.saveOrUpdate(this.formState)
        .then(response => {
          if (response.status) {
            this.$Message.success(this.$t('common.save') + ' [ ' + this.formState.name + ' ] ' + this.$t('common.success'))
            if (response.data?.id) {
              this.$router.push(`/preview/dashboard/${response.data.id}`)
            }
            else {
              this.$router.push('/console/dashboard')
            }
          }
          else {
            this.$Message.error(response.message)
          }
        })
        .finally(() => this.saving = false)
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
:deep(.ivu-collapse) {
  border: 0px solid #dcdee2;
}
</style>
