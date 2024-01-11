<template>
  <div>
    <CircularLoading v-if="loading"
                     :show="loading">
    </CircularLoading>
    <div v-else>
      <VisualTable v-if="configuration.type === Type.TABLE"
                   :configuration="localConfiguration"
                   :submitted="false">
      </VisualTable>
      <VisualLine v-else-if="configuration.type === Type.LINE"
                  :configuration="localConfiguration"
                  :submitted="false">
      </VisualLine>
      <VisualBar v-else-if="configuration.type === Type.BAR"
                 :configuration="localConfiguration"
                 :submitted="false">
      </VisualBar>
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

export default {
  name: 'VisualView',
  computed: {
    Type()
    {
      return Type
    }
  },
  components: {CircularLoading, VisualBar, VisualLine, VisualTable},
  props: {
    configuration: {
      type: Configuration
    },
    query: {
      type: Object
    },
    code: {
      type: String
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
