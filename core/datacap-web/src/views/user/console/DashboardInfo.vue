<template>
  <div>
    <Modal v-model="visible"
           :title="$t('common.dashboard')"
           :mask-closable="false"
           :width="'90%'"
           @cancel="handlerCancel()">
      <CircularLoading v-if="loading"
                       :show="loading"
                       style="margin-top: 150px;">
      </CircularLoading>
      <div v-else>
        <DashboardEditor @onCommit="handlerCommit"/>
      </div>
      <template #footer>
        <Button type="primary"
                :loading="saving"
                @click="handlerSave">
          <FontAwesomeIcon v-if="!saving"
                           icon="save">
          </FontAwesomeIcon>
          {{ $t('common.save') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import DashboardEditor from "@/components/editor/dashboard/DashboardEditor.vue";
import DashboardService from "@/services/DashboardService";

export default defineComponent({
  name: "DashboardInfo",
  components: {DashboardEditor, CircularLoading},
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    id: {
      type: Number
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  data()
  {
    return {
      loading: false,
      saving: false,
      configure: null
    }
  },
  methods: {
    handlerInitialize()
    {
      // this.loading = true;
      // const configure: TableFilter = new TableFilter();
      // configure.type = SqlType.DROP;
      // configure.preview = preview;
      // configure.columnId = this.data.applyId;
      // TableService.manageColumn(this.data.tableId, configure)
      //   .then(response => {
      //     if (response.data) {
      //       if (response.data?.isSuccessful) {
      //         if (preview) {
      //           this.statement = response.data.content;
      //         }
      //         else {
      //           this.$Message.success(this.$t('source.manager.dropColumn') + ' [ ' + this.data.title + ' ] ' + this.$t('common.success'));
      //           this.handlerCancel();
      //         }
      //       }
      //       else {
      //         this.$Message.error(response.data?.message);
      //       }
      //     }
      //   })
      //   .finally(() => this.loading = false)
      console
    },
    handlerSave()
    {
      this.saving = true
      DashboardService.saveOrUpdate(this.configure)
        .then(response => {
          if (response.status) {
            this.$Message.success(this.$t('common.save') + ' [ ' + this.configure.name + ' ] ' + this.$t('common.success'))
            this.handlerCancel()
          }
          else {
            this.$Message.error(response.message)
          }
        })
        .finally(() => this.saving = false)
    },
    handlerCommit(configure: any)
    {
      this.configure = configure
    },
    handlerCancel()
    {
      this.visible = false;
    }
  },
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible;
      },
      set(value: boolean)
      {
        this.$emit('close', value);
      }
    }
  }
});
</script>
