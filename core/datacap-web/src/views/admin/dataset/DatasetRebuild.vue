<template>
  <div>
    <Modal v-model="visible"
           :title="$t('dataset.rebuild') + ' [ ' + data.name + ' ]'"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <Alert type="warning"
             show-icon>
        {{ $t('dataset.rebuildProgress') }}
      </Alert>
      <!--      <Alert type="error"-->
      <!--             show-icon>-->
      <!--        {{ $t('report.deleteTip2') }}-->
      <!--      </Alert>-->
      <!--      <p>{{ $t('report.deleteTip3').replace('REPLACE_NAME', data.name) }}</p>-->
      <!--      <Input v-model="inputValue"/>-->
      <template #footer>
        <Button type="primary"
                :loading="loading"
                @click="handlerRebuild">
          {{ $t('dataset.rebuild') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue';
import DatasetService from "@/services/admin/DatasetService";

export default defineComponent({
  name: 'DatasetRebuild',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    data: {
      type: Object
    }
  },
  data()
  {
    return {
      loading: false
    }
  },
  methods: {
    handlerRebuild()
    {
      this.loading = true;
      DatasetService.rebuild(this.data.id)
        .then((response) => {
          if (response.status) {
            this.$Message.success(`${this.$t('dataset.rebuild')} [ ${this.data.name} ] ${this.$t('common.success')}`)
            this.handlerCancel()
          }
        })
        .finally(() => {
          this.loading = false
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
</style>
