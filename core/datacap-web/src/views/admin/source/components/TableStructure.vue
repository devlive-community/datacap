<template>
  <div>
    <CircularLoading v-if="loading"
                     :show="loading"
                     style="margin-top: 150px;">
    </CircularLoading>
    <div v-else>
      <Table size="small"
             style="margin-top: -15px;"
             :columns="headers"
             :height="450"
             :data="data">
        <template #isNullable="{ row }">
          <Switch v-model="row.isNullable"
                  disabled
                  true-value="1"
                  false-value="0">
          </Switch>
        </template>
      </Table>
    </div>
  </div>
</template>
<script lang="ts">
import {defineComponent, watch} from "vue";
import ColumnService from "@/services/Column";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import {useI18n} from "vue-i18n";
import {createHeaders} from "./TableStructureFunction";

export default defineComponent({
  name: "TableStructure",
  components: {CircularLoading},
  props: {
    id: {
      type: Number,
      default: 0
    }
  },
  setup()
  {
    const i18n = useI18n();
    const headers = createHeaders(i18n);
    return {
      headers
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
      data: []
    }
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true;
      ColumnService.getAllByTable(this.id)
        .then(response => {
          if (response.status) {
            this.data = response.data
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
