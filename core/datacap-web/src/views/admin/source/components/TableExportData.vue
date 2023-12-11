<template>
  <div>
    <Modal v-model="visible"
           :title="$t('source.manager.exportTable') + ' [ ' + data.title + ' ]'"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <Form :model="formState"
            :label-width="140">
        <FormItem :label="$t('source.manager.exportTableDataFormat')">
          <RadioGroup v-model="formState.format">
            <Radio label="CSV">CSV</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem :label="$t('source.manager.exportTableDataCount')">
          <InputNumber v-model="formState.count"
                       min="0">
          </InputNumber>
        </FormItem>
        <FormItem v-if="remotePath"
                  :label="$t('source.manager.downloadPath')">
          <Input v-model="remotePath">
            <template #append>
              <Button type="primary"
                      @click="handlerDownload">
                {{ $t('source.manager.download') }}
              </Button>
            </template>
          </Input>
        </FormItem>
      </Form>
      <template #footer>
        <Button type="primary"
                :loading="loading"
                @click="handlerExport()">
          <FontAwesomeIcon v-if="!loading"
                           icon="file-export">
          </FontAwesomeIcon>
          {{ $t('source.manager.exportTableData') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import {DataStructureModel} from "@/model/DataStructure";
import TableService from "@/services/Table";
import {FormItem} from "view-ui-plus";

export default defineComponent({
  name: "TableExportData",
  components: {FormItem},
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    data: {
      type: DataStructureModel
    }
  },
  data()
  {
    return {
      loading: false,
      formState: {
        count: 0,
        format: 'CSV'
      },
      remotePath: null
    }
  },
  methods: {
    handlerExport()
    {
      this.loading = true;
      this.remotePath = null;
      TableService.exportData(this.data.applyId, this.formState)
        .then(response => {
          if (response.status) {
            this.remotePath = response.data;
          }
        })
        .finally(() => this.loading = false)
    },
    handlerDownload()
    {
      window.open(this.remotePath, '_target');
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
