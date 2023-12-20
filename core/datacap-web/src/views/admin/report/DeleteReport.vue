<template>
  <div>
    <Modal v-model="visible"
           :title="$t('common.delete') + ' [ ' + data.name + ' ]'"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <Alert type="warning"
             show-icon>
        {{ $t('report.deleteTip1') }}
      </Alert>
      <Alert type="error"
             show-icon>
        {{ $t('report.deleteTip2') }}
      </Alert>
      <p>{{ $t('report.deleteTip3').replace('REPLACE_NAME', data.name) }}</p>
      <Input v-model="inputValue"/>
      <template #footer>
        <Button type="error"
                :disabled="inputValue !== data.name"
                :loading="loading"
                @click="handlerDelete()">
          <FontAwesomeIcon icon="delete-left"/>
          {{ $t('common.delete') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import ReportService from "@/services/admin/ReportService";

export default defineComponent({
  name: "DeleteReport",
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
      loading: false,
      inputValue: null
    }
  },
  methods: {
    handlerDelete()
    {
      this.loading = true;
      ReportService.deleteById(this.data.id)
        .then((response) => {
          if (response.status) {
            this.$Message.success(`${this.$t('common.delete')} [ ${this.data.name} ] ${this.$t('common.success')}`);
            this.handlerCancel();
          }
        })
        .finally(() => this.loading = false);
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
