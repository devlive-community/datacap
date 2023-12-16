<template>
  <div>
    <Modal v-model="visible"
           :title="$t('source.manager.sourceDelete') + ' [ ' + data.name + ' ]'"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <Alert type="warning"
             show-icon>
        {{ $t('source.manager.sourceDeleteTip1') }}
      </Alert>
      <Alert type="error"
             show-icon>
        {{ $t('source.manager.sourceDeleteTip2') }}
      </Alert>
      <p>{{ $t('source.manager.sourceDeleteTip3').replace('REPLACE_NAME', data.name) }}</p>
      <Input v-model="inputValue"/>
      <template #footer>
        <Button type="error"
                :disabled="inputValue !== data.name"
                :loading="loading"
                @click="handlerDelete()">
          <FontAwesomeIcon icon="delete-left"/>
          {{ $t('source.manager.sourceDelete') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import {SourceService} from "@/services/SourceService";

export default defineComponent({
  name: "SourceDelete",
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
      new SourceService()
        .delete(this.data.id)
        .then((response) => {
          if (response.status) {
            this.$Message.success(`${this.$t('source.manager.sourceDelete')} [ ${this.data.name} ] ${this.$t('common.success')}`);
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
