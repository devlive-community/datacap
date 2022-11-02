<template>
  <div id="container"/>
</template>
<script lang="ts">
import {defineComponent, PropType} from "vue";
import {Chart, IGroup, registerShape} from "@antv/g2";
import {RangePoint, ShapeInfo} from "@antv/g2/lib/interface";
import {ShapeCfg} from "@antv/g-base/src/types";
import {Data} from "@antv/s2";
import {Calendar} from "@/charts/calendar/Calendar";

export default defineComponent({
  name: "CalendarChart",
  props: {
    data: {
      type: Array as PropType<Array<Calendar>>,
      default: () => []
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      setTimeout(() => {
        registerShape('polygon', 'boundary-polygon', {
          draw(cfg: ShapeInfo, container: IGroup)
          {
            const group = container.addGroup();
            const attrs = {
              stroke: '#fff',
              lineWidth: 1,
              fill: cfg.color,
            } as unknown as ShapeCfg;
            const points = cfg.points as RangePoint[];
            const path = [
              ['M', points[0].x, points[0].y],
              ['L', points[1].x, points[1].y],
              ['L', points[2].x, points[2].y],
              ['L', points[3].x, points[3].y],
              ['Z']
            ];
            attrs.path = this.parsePath(path);
            group.addShape('path', {
              attrs
            });
            const dat = cfg.data as Data;
            if (dat.lastWeek) {
              const linePath = [
                ['M', points[2].x, points[2].y],
                ['L', points[3].x, points[3].y]
              ];
              // Add the right border for the last week's polygon
              group.addShape('path', {
                attrs: {
                  path: this.parsePath(linePath),
                  lineWidth: 4,
                  stroke: '#404040'
                }
              });
              if (dat.lastDay) {
                group.addShape('path', {
                  attrs: {
                    path: this.parsePath([
                      ['M', points[1].x, points[1].y],
                      ['L', points[2].x, points[2].y]
                    ]),
                    lineWidth: 2,
                    stroke: '#404040'
                  }
                });
              }
            }
            return group;
          }
        });
        const chart = new Chart({
          container: 'container',
          autoFit: true,
          height: 150
        });
        chart.data(this.data);
        chart.scale({
          day: {
            type: 'cat',
            values: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
          }
        });
        chart.axis('week', {
          position: 'top',
          tickLine: null,
          line: null,
          label: {
            offset: 12,
            style: {
              fontSize: 12,
              fill: '#666',
              textBaseline: 'top'
            }
          }
        });
        chart.axis('day', {});
        chart.legend(false);
        chart.tooltip({
          title: 'date',
          showMarkers: true,
        });
        chart.coordinate().reflect('y');
        chart.polygon().position('week*day*date')
          .color('count', '#BAE7FF-#1890FF-#0050B3')
          .shape('boundary-polygon');
        chart.interaction('element-active');
        chart.render();
      }, 0)
    }
  }
});
</script>
