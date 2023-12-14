<template>
  <div>
    <Modal v-model="visible"
           :title="$t('source.manager.new') + ' [ ' + $t('source.manager.newTable') + ' ]'"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <Form :model="formState"
            :label-width="120">
        <FormItem :label="$t('source.manager.tableName')">
          <Input v-model="formState.name"/>
        </FormItem>
        <FormItem :label="$t('source.manager.tableComment')">
          <Input v-model="formState.comment"
                 type="textarea">
          </Input>
        </FormItem>
        <FormItem :label="$t('source.manager.tableEngine')">
          <Input v-model="formState.engine"/>
        </FormItem>
        <Divider orientation="left">{{ $t('source.manager.column') }}</Divider>
        <Collapse accordion>
          <Panel v-for="(item, index) in formState.columns"
                 :key="index">
            {{ item.name }}
            <Button type="text"
                    size="small"
                    @click="handlerRemove(index)">
              <FontAwesomeIcon icon="circle-minus"/>
            </Button>
            <Button type="text"
                    size="small"
                    @click="handlerAdd">
              <FontAwesomeIcon icon="circle-plus"/>
            </Button>
            <template #content>
              <FormItem :label="$t('source.manager.columnName')">
                <Input v-model="item.name"/>
              </FormItem>
              <FormItem :label="$t('source.manager.columnType')">
                <Input v-model="item.type"/>
              </FormItem>
              <FormItem :label="$t('source.manager.columnLength')">
                <InputNumber v-model="item.length"/>
              </FormItem>
              <FormItem :label="$t('source.manager.columnComment')">
                <Input v-model="item.comment"
                       type="textarea">
                </Input>
              </FormItem>
              <FormItem :label="$t('source.manager.columnDefaultValue')">
                <Input v-model="item.defaultValue"/>
              </FormItem>
              <FormItem :label="$t('source.manager.columnPrimaryKey')">
                <Switch v-model="item.primaryKey"/>
              </FormItem>
              <FormItem :label="$t('source.manager.columnAutoIncrement')">
                <Input v-model="item.autoIncrement"/>
              </FormItem>
              <FormItem :label="$t('source.manager.columnIsNullable')">
                <Input v-model="item.isNullable"/>
              </FormItem>
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
          {{ $t('common.save') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import {DataStructureModel} from "@/model/DataStructure";

export default defineComponent({
  name: "TableCreate",
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
        name: null,
        comment: null,
        engine: null,
        columns: [{name: 'column_name', type: null, length: 0, comment: null, defaultValue: null, primaryKey: null, autoIncrement: null, isNullable: null}]
      }
    }
  },
  methods: {
    handlerSave()
    {
      this.loading = true;
    },
    handlerAdd()
    {
      const newColumn = {name: 'column_name', type: null, length: 0, comment: null, defaultValue: null, primaryKey: null, autoIncrement: null, isNullable: null}
      this.formState.columns.push(newColumn);
    },
    handlerRemove(index: number)
    {
      this.formState.columns.splice(index, 1);
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
