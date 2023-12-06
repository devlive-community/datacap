<template>
  <div>
    <CircularLoading v-if="loading"
                     :show="loading"
                     style="margin-top: 150px;">
    </CircularLoading>
    <div v-else>
      <ErDiagram :options="options">
      </ErDiagram>
    </div>
  </div>
</template>
<script lang="ts">
import {defineComponent, watch} from "vue";
import ColumnService from "@/services/Column";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import ErDiagram from "@/components/er-diagram/ErDiagram.vue";
import {ErDiagramOptions} from "@/components/er-diagram/ErDiagramOptions";

export default defineComponent({
  name: "TableErDiagram",
  components: {ErDiagram, CircularLoading},
  props: {
    id: {
      type: Number,
      default: 0
    },
    title: {
      type: String
    }
  },
  created()
  {
    this.handlerInitialize();
    this.watchId();
  },
  data()
  {
    return {
      loading: false,
      options: null as ErDiagramOptions
    }
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true;
      ColumnService.getAllByTable(this.id)
        .then(response => {
          if (response.status) {
            this.options = new ErDiagramOptions();
            this.options.table = {id: this.id, name: this.title}
            this.options.columns = response.data
          }
        })
        .finally(() => this.loading = false)
    },
    watchId()
    {
      watch(
        () => this.id,
        () => {
          this.handlerInitialize();
        }
      );
    }
  }
});
</script>
