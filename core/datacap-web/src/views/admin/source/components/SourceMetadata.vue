<template>
  <div>
    <Modal v-model="visible"
           :title="$t('common.syncMetadata') + ' [ ' + data.name + ' ]'"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <Alert type="warning"
             show-icon>
        {{ $t('source.manager.sourceSyncMetadataTip1') }}
      </Alert>
      <Alert type="error"
             show-icon>
        {{ $t('source.manager.sourceSyncMetadataTip2') }}
      </Alert>
      <p>{{ $t('source.manager.sourceSyncMetadataTip3').replace('REPLACE_NAME', data.name) }}</p>
      <Input v-model="inputValue"/>
      <template #footer>
        <Button type="primary"
                :disabled="inputValue !== data.name"
                :loading="loading"
                @click="handlerSubmit()">
          <FontAwesomeIcon icon="rotate"/>
          {{ $t('common.syncMetadata') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import SourceV2Service from "@/services/SourceV2Service";

export class DataItem
{
  id: number;
  name: string;
}

export default defineComponent({
  name: "SourceMetadata",
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    data: {
      type: DataItem
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
    handlerSubmit()
    {
      this.loading = true;
      SourceV2Service
        .syncMetadata(this.data.id)
        .then((response) => {
          if (response.status) {
            this.$Message.success(`${this.$t('source.manager.sourceSyncMetadataTip4').replace('REPLACE_NAME', this.data.name)}`);
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
