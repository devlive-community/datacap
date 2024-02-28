<template>
  <div>
    <Modal v-model="visible"
           :title="$t('common.clearData') + ' [ ' + data.name + ' ]'"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <Alert type="error">
        {{ $t('dataset.clearDataTip') }}
      </Alert>
      <Row :gutter="10">
        <Col span="12">
          <Card dis-hover>
            <NumberInfo :title="$t('common.totalRows')"
                        :total="data.totalRows">
            </NumberInfo>
          </Card>
        </Col>
        <Col span="12">
          <Card dis-hover>
            <NumberInfo :title="$t('common.totalSize')"
                        :total="data.totalSize">
            </NumberInfo>
          </Card>
        </Col>
      </Row>
      <template #footer>
        <Button type="error"
                @click="handlerSubmit">
          <FontAwesomeIcon icon="trash"/>
          {{ $t('common.clearData') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue';
import DatasetService from "@/services/admin/DatasetService";

export default defineComponent({
  name: 'DatasetClearData',
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
      DatasetService.clearData(this.data.code)
        .then((response: { status: boolean; }) => {
          if (response.status) {
            this.$Message.success(`${this.$t('common.clearData')} [ ${this.data.name} ] ${this.$t('common.success')}`);
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
