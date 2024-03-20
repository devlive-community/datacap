<template>
  <div>
    <CircularLoading v-if="loading"
                     :show="loading">
    </CircularLoading>
    <div v-else>
      <VisualTable v-if="localConfiguration && configuration?.type === Type.TABLE"
                   :configuration="localConfiguration"
                   :submitted="false"
                   :width="width"
                   :height="height">
      </VisualTable>
      <VisualLine v-else-if="localConfiguration && configuration?.type === Type.LINE"
                  :configuration="localConfiguration"
                  :submitted="false"
                  :width="width"
                  :height="height">
      </VisualLine>
      <VisualBar v-else-if="localConfiguration && configuration?.type === Type.BAR"
                 :configuration="localConfiguration"
                 :submitted="false"
                 :width="width"
                 :height="height">
      </VisualBar>
      <VisualArea v-else-if="localConfiguration && configuration?.type === Type.AREA"
                  :configuration="localConfiguration"
                  :submitted="false"
                  :width="width"
                  :height="height">
      </VisualArea>
      <VisualPie v-else-if="localConfiguration && configuration?.type === Type.PIE"
                 :configuration="localConfiguration"
                 :submitted="false"
                 :width="width"
                 :height="height">
      </VisualPie>
      <VisualHistogram v-else-if="localConfiguration && configuration?.type === Type.HISTOGRAM"
                       :configuration="localConfiguration"
                       :submitted="false"
                       :width="width"
                       :height="height">
      </VisualHistogram>
      <VisualWordCloud v-else-if="localConfiguration && configuration?.type === Type.WORDCLOUD"
                       :configuration="localConfiguration"
                       :submitted="false"
                       :width="width"
                       :height="height">
      </VisualWordCloud>
    </div>
  </div>
</template>
<script lang="ts">
import { cloneDeep } from 'lodash'
import { Type } from '@/views/components/visual/Type'
import VisualWordCloud from '@/views/components/visual/components/VisualWordCloud.vue'
import VisualHistogram from '@/views/components/visual/components/VisualHistogram.vue'
import VisualPie from '@/views/components/visual/components/VisualPie.vue'
import VisualArea from '@/views/components/visual/components/VisualArea.vue'
import { Configuration } from './Configuration'
import VisualBar from '@/views/components/visual/components/VisualBar.vue'
import VisualLine from '@/views/components/visual/components/VisualLine.vue'
import VisualTable from '@/views/components/visual/components/VisualTable.vue'
import { ToastUtils } from '@/utils/toast'
import DatasetService from '@/services/dataset'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'VisualView',
  computed: {
    Type()
    {
      return Type
    }
  },
  components: {
    CircularLoading,
    VisualWordCloud, VisualHistogram, VisualPie, VisualArea, VisualBar, VisualLine, VisualTable
  },
  props: {
    configuration: {
      type: Object as () => Configuration | null
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
      localConfiguration: null as Configuration | null
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.localConfiguration = cloneDeep(this.configuration) as Configuration
      setTimeout(() => {
        this.loading = true
        DatasetService.adhoc(this.code!, this.query)
            .then(response => {
              if (response.status) {
                if (this.localConfiguration) {
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
              }
              else {
                ToastUtils.error(response.message)
              }
            })
            .finally(() => this.loading = false)
      })
    }
  }
})
</script>
