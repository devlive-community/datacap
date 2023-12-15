<template>
  <div>
    <Modal v-model="visible"
           :title="$t('source.manager.modifyColumn') + ' [ ' + data.title + ' ]'"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <Form :model="formState"
            :label-width="120">
        <FormItem :label="$t('source.manager.tableName')">
          <Input v-model="data.table"
                 disabled>
          </Input>
        </FormItem>
        <Divider orientation="left">{{ $t('source.manager.column') }}</Divider>
        <Collapse accordion>
          <Panel v-for="(item, index) in formState.columns"
                 :key="index">
            {{ item.name }}
            <template #content>
              <Row>
                <Col span="12">
                  <FormItem :label="$t('source.manager.columnName')"
                            :label-width="80">
                    <Input v-model="item.name"/>
                  </FormItem>
                </Col>
                <Col span="12">
                  <FormItem :label="$t('source.manager.columnType')"
                            :label-width="80">
                    <Input v-model="item.type"/>
                  </FormItem>
                </Col>
                <Col span="12">
                  <FormItem :label="$t('source.manager.columnLength')"
                            :label-width="80">
                    <InputNumber v-model="item.length"/>
                  </FormItem>
                </Col>
                <Col span="12">
                  <FormItem :label="$t('source.manager.columnDefaultValue')"
                            :label-width="80">
                    <Input v-model="item.defaultValue"/>
                  </FormItem>
                </Col>
                <Col span="12">
                  <FormItem :label="$t('source.manager.columnPrimaryKey')"
                            :label-width="80">
                    <Switch v-model="item.primaryKey"/>
                  </FormItem>
                </Col>
                <Col span="12">
                  <FormItem :label="$t('source.manager.columnAutoIncrement')"
                            :label-width="80">
                    <Switch v-model="item.autoIncrement"/>
                  </FormItem>
                </Col>
                <Col span="12">
                  <FormItem :label="$t('source.manager.columnIsNullable')">
                    <Switch v-model="item.isNullable"/>
                  </FormItem>
                </Col>
                <Col span="24">
                  <FormItem :label="$t('source.manager.columnComment')"
                            :label-width="80">
                    <Input v-model="item.comment"
                           type="textarea">
                    </Input>
                  </FormItem>
                </Col>
              </Row>
            </template>
          </Panel>
        </Collapse>
      </Form>
      <template #footer>
        <Button type="primary"
                :loading="loading"
                @click="handlerSave()">
          <FontAwesomeIcon v-if="!loading"
                           icon="save">
          </FontAwesomeIcon>
          {{ $t('source.manager.modifyColumn') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import {DataStructureModel} from "@/model/DataStructure";
import TableService from "@/services/Table";
import ColumnService from "@/services/Column";
import {SqlType} from "@/model/TableFilter";

export default defineComponent({
  name: "ColumnModify",
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
        type: SqlType.MODIFY,
        columns: []
      }
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      ColumnService.getById(this.data.applyId)
        .then(response => {
          if (response.status) {
            const data = response.data;
            const column = {
              name: data.name,
              type: data.dataType,
              length: data.maximumLength,
              comment: data.comment,
              defaultValue: data.defaultValue,
              primaryKey: data.isKey === 'PRI',
              autoIncrement: data.extra === 'auto_increment',
              isNullable: data.isNullable
            }
            this.formState.columns.push(column);
          }
        })
    },
    handlerSave()
    {
      this.loading = true;
      TableService.manageColumn(this.data.tableId, this.formState)
        .then(response => {
          if (response.data) {
            if (response.data.isSuccessful) {
              this.$Message.success(this.$t('common.success'));
              this.handlerCancel();
            }
            else {
              this.$Message.error(response.data?.message);
            }
          }
        })
        .finally(() => this.loading = false)
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
