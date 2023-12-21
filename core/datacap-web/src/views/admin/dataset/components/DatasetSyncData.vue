<template>
  <div>
    <Modal v-model="visible"
           :title="$t('dataset.syncData') + ' [ ' + data.name + ' ]'"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <Alert type="warning">
        {{ $t('dataset.syncDataTip') }}
      </Alert>
      <template #footer>
        <Button type="primary"
                @click="handlerSubmit">
          <FontAwesomeIcon icon="rotate"/>
          {{ $t('dataset.syncData') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue';
import DatasetService from "@/services/admin/DatasetService";

export default defineComponent({
  name: 'DatasetSyncData',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    data: {
      type: Object
    }
  },
  methods: {
    handlerSubmit()
    {
      DatasetService.syncData(this.data.id)
        .then(response => {
          if (response.status) {
            this.$Message.success(`${this.$t('dataset.syncData')} [ ${this.data.name} ] ${this.$t('common.success')}`);
            this.handlerCancel();
          }
        })
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

<style scoped>
.time {
  font-size: 14px;
}

.content {
  margin-left: 30px;
  font-weight: bold;
}
</style>
