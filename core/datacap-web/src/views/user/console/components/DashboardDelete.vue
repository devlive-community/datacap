<template>
  <div>
    <Modal v-model="visible"
           :title="$t('common.delete') + ' [ ' + data.name + ' ]'"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <Alert type="warning"
             show-icon>
        {{ $t('dashboard.deleteTip1') }}
      </Alert>
      <Alert type="error"
             show-icon>
        {{ $t('dashboard.deleteTip2') }}
      </Alert>
      <p>{{ $t('dashboard.deleteTip3').replace('REPLACE_NAME', data.name) }}</p>
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
import DashboardService from "@/services/DashboardService";

export default defineComponent({
  name: "DashboardDelete",
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
      DashboardService.deleteById(this.data.id)
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
