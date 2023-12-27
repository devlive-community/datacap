<template>
  <div>
    <Modal v-model="visible"
           :title="$t('common.dashboard')"
           :mask-closable="false"
           :width="'98%'"
           @cancel="handlerCancel()">
      <CircularLoading v-if="loading"
                       :show="loading"
                       style="margin-top: 150px;">
      </CircularLoading>
      <div v-else>
        <DashboardEditor :elements="nodes"
                         :source-configure="sourceConfigure"
                         @onCommit="handlerCommit">
        </DashboardEditor>
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
      configure: null,
      nodes: [],
      sourceConfigure: null
    }
  },
  methods: {
    handlerInitialize()
    {
      if (this.id) {
        this.loading = true;
        DashboardService.getById(this.id)
          .then(response => {
            if (response.status) {
              const configure = JSON.parse(response.data.configure)
              configure.nodes?.forEach((node: any) => {
                this.nodes.push({id: node.id, type: node.type, label: node.label, position: node.position, data: node.data})
              })
              this.sourceConfigure = response.data
            }
            else {
              this.$Message.error(response.message)
            }
          })
          .finally(() => this.loading = false)
      }
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
