<template>
  <div>
    <CircularLoading v-if="loading"
                     :show="loading">
    </CircularLoading>
    <div v-else>
      <VisualTable v-if="configuration.type === Type.TABLE"
                   :configuration="localConfiguration"
                   :submitted="false"
                   :width="width"
                   :height="height">
      </VisualTable>
      <VisualLine v-else-if="configuration.type === Type.LINE"
                  :configuration="localConfiguration"
                  :submitted="false"
                  :width="width"
                  :height="height">
      </VisualLine>
      <VisualBar v-else-if="configuration.type === Type.BAR"
                 :configuration="localConfiguration"
                 :submitted="false"
                 :width="width"
                 :height="height">
      </VisualBar>
      <VisualArea v-else-if="configuration.type === Type.AREA"
                  :configuration="localConfiguration"
                  :submitted="false"
                  :width="width"
                  :height="height">
      </VisualArea>
      <VisualPie v-else-if="configuration.type === Type.PIE"
                 :configuration="localConfiguration"
                 :submitted="false"
                 :width="width"
                 :height="height">
      </VisualPie>
      <VisualHistogram v-else-if="configuration.type === Type.HISTOGRAM"
                       :configuration="localConfiguration"
                       :submitted="false"
                       :width="width"
                       :height="height">
      </VisualHistogram>
      <VisualWordCloud v-else-if="configuration.type === Type.WORDCLOUD"
                       :configuration="localConfiguration"
                       :submitted="false"
                       :width="width"
                       :height="height">
      </VisualWordCloud>
    </div>
  </div>
</template>
<script lang="ts">
import {Configuration} from "@/components/visual/Configuration";
import VisualTable from "@/components/visual/components/VisualTable.vue";
import VisualLine from "@/components/visual/components/VisualLine.vue";
import {Type} from "@/components/visual/Type";
import VisualBar from "@/components/visual/components/VisualBar.vue";
import DatasetService from "@/services/admin/DatasetService";
import {cloneDeep} from "lodash";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import VisualArea from "@/components/visual/components/VisualArea.vue";
import VisualPie from "@/components/visual/components/VisualPie.vue";
import VisualHistogram from "@/components/visual/components/VisualHistogram.vue";
import VisualWordCloud from "@/components/visual/components/VisualWordCloud.vue";

export default {
  name: 'VisualView',
  computed: {
    Type()
    {
      return Type
    }
  },
  components: {VisualWordCloud, VisualHistogram, VisualPie, VisualArea, CircularLoading, VisualBar, VisualLine, VisualTable},
  props: {
    configuration: {
      type: Configuration
    },
    query: {
      type: Object
    },
    code: {
      type: String
    },
    width: {
      type: String,
      default: () => '100%'
    },
    height: {
      type: String,
      default: () => '400px'
    }
  },
  data()
  {
    return {
      loading: false,
      localConfiguration: null as Configuration
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.localConfiguration = cloneDeep(this.configuration)
      setTimeout(() => {
        this.loading = true
        DatasetService.adhoc(this.code, this.query)
          .then(response => {
            if (response.status) {
              if (response.data.isSuccessful) {
                this.localConfiguration.headers = response.data.headers
                this.localConfiguration.columns = response.data.columns
                this.localConfiguration.message = null
              }
              else {
                this.localConfiguration.headers = []
                this.localConfiguration.columns = []
                this.localConfiguration.message = response.data.message
              }
            }
            else {
              this.$Message.error(response.message)
            }
          })
          .finally(() => this.loading = false)
      })
    }
  }
};
</script>
