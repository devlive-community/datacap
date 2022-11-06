<template>
  <div id="containerForRadar"/>
</template>
<script lang="ts">
import {defineComponent, PropType} from "vue";
import {Chart} from "@antv/g2";
import DataSet from '@antv/data-set';
import {Radar} from "@/charts/radar/Radar";

const {DataView} = DataSet;
export default defineComponent({
  name: "RadarChart",
  props: {
    data: {
      type: Array as PropType<Array<Radar>>,
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
        const dv = new DataView().source(this.data);
        dv.transform({
          type: 'fold',
          fields: ['percentage'],
          value: 'percentage',
        });
        const chart = new Chart({
          container: 'containerForRadar',
          autoFit: true,
          height: 200,
        });
        chart.data(dv.rows);
        chart.scale('percentage', {
          min: 0,
          max: 100,
        });
        chart.coordinate('polar', {
          radius: 0.9,
        });
        chart.axis('label', {
          line: null,
          tickLine: null,
        });
        chart.axis('score', {
          line: null,
          tickLine: null,
          grid: {
            line: {
              type: 'circle',
            },
          },
        });
        chart.tooltip({
          shared: true,
          showCrosshairs: true,
          crosshairs: {
            type: 'xy',
            line: {
              style: {
                stroke: '#565656',
                lineDash: [4],
              },
            },
            follow: true
          }
        });
        chart.line().position('label*percentage').color('#5BD9A6');
        chart.point().position('label*percentage').color('#5BD9A6').shape('circle');
        chart.render();
      }, 0)
    }
  }
});
</script>
