<template>
  <div>
    <Modal v-model="visible" :title="$t('common.menu')" :closable="false" :mask-closable="false" @cancel="handlerCancel()">
      <Tree show-checkbox
            :data="fullMenus"
            @on-check-change="handlerOnCheckedNode">
      </Tree>
      <Spin :show="loading" fix>
      </Spin>
      <template #footer>
        <Button key="cancel" type="error" :disabled="saving" size="small" @click="handlerCancel()">
          {{ $t('common.cancel') }}
        </Button>
        <Button type="primary" size="small" :loading="saving" @click="handlerSave()">
          {{ $t('common.save') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import RoleService from "@/services/admin/RoleService";

export default defineComponent({
  name: 'MenuTreeComponent',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    roleId: {
      type: Number,
      default: () => 0
    }
  },
  data()
  {
    return {
      loading: false,
      saving: false,
      fullMenus: [],
      selectNodes: []
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true;
      RoleService.getAllMenuById(this.roleId)
        .then((response) => {
          if (response.status) {
            this.fullMenus = response.data;
          }
        })
        .finally(() => {
          this.loading = false
        })
    },
    handlerOnCheckedNode(nodes: any)
    {
      this.selectNodes = nodes;
    },
    handlerSave()
    {
      this.saving = true;
      RoleService.saveMenu(this.roleId, this.selectNodes)
        .then((response) => {
          if (response.status) {
            this.saving = false;
            this.handlerCancel();
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => {
          this.saving = false
        })
    },
    handlerCancel()
    {
      this.$emit('close', false)
    }
  },
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible
      },
      set(value: boolean)
      {
        this.$emit('close', value)
      }
    }
  },
})
</script>
