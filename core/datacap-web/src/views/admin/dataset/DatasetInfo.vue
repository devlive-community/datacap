<template>
  <div>
    <div v-if="data">
      <Divider orientation="left">
        {{ $t('dataset.dataPreview') }}
      </Divider>
      <AgGridVue :style="{height: '300px'}"
                 class="ag-theme-datacap"
                 :pagination="true"
                 :columnDefs="columnDefs"
                 :rowData="data.columns"
                 :gridOptions="gridOptions">
      </AgGridVue>
      <Divider orientation="left">
        {{ $t('dataset.dataColumn') }}
      </Divider>
      <Form style="padding: 0 20px">
        <FormItem style="margin-bottom: 5px;">
          <Row :gutter="10">
            <Col span="4">{{ $t('dataset.columnName') }}</Col>
            <Col span="4">{{ $t('dataset.columnType') }}</Col>
            <Col span="4">{{ $t('dataset.columnDefaultValue') }}</Col>
            <Col span="4">{{ $t('dataset.columnIsNullable') }}</Col>
            <Col span="4">{{ $t('dataset.columnLength') }}</Col>
            <Col span="4">{{ $t('dataset.columnComment') }}</Col>
          </Row>
        </FormItem>
        <template v-for="(item, index) in formState.columns" :key="index">
          <FormItem>
            <Row :gutter="10">
              <Col span="4">
                <Input v-model="item.name"
                       type="text">
                </Input>
              </Col>
              <Col span="4">
                <Select v-model="item.type">
                  <Option value="STRING">{{ $t('dataset.columnTypeString') }}</Option>
                  <Option value="NUMBER">{{ $t('dataset.columnTypeNumber') }}</Option>
                  <Option value="BOOLEAN">{{ $t('dataset.columnTypeBoolean') }}</Option>
                </Select>
              </Col>
              <Col span="4">
                <Input v-model="item.defaultValue"
                       type="text">
                </Input>
              </Col>
              <Col span="4">
                <Switch v-model="item.isNullable"/>
              </Col>
              <Col span="4">
                <InputNumber v-model="item.length"/>
              </Col>
              <Col span="4">
                <Input v-model="item.comment"
                       type="textarea">
                </Input>
              </Col>
            </Row>
          </FormItem>
        </template>
      </Form>
      <Button type="primary"
              style="margin-bottom: 15px; margin-right: 10px; float: right;"
              @click="infoVisible = true">
        {{ $t('dataset.create') }}
      </Button>
    </div>
    <Result v-else
            style="margin-top: 100px;"
            type="warning">
      <template #desc>
        {{ i18n.t('dataset.onlyPreviewCreate') }}
      </template>
      <template #actions>
        <Button type="primary"
                to="/console/index">
          {{ i18n.t('dataset.returnQuery') }}
        </Button>
      </template>
    </Result>
    <Modal v-model="infoVisible"
           :title="$t('dataset.create')"
           :mask-closable="false">
      <Form :label-width="80">
        <FormItem :label="$t('common.name')">
          <Input v-model="formState.name"/>
        </FormItem>
        <FormItem :label="$t('common.description')">
          <Input v-model="formState.description"
                 type="textarea">
          </Input>
        </FormItem>
        <FormItem :label="$t('dataset.syncMode')">
          <RadioGroup v-model="formState.syncMode">
            <Radio label="MANUAL">{{ $t('dataset.syncModeManual') }}</Radio>
            <Radio label="OUT_SYNC">{{ $t('dataset.syncModeOutSync') }}</Radio>
          </RadioGroup>
        </FormItem>
      </Form>
      <template #footer>
        <Button type="primary"
                :loading="loading"
                @click="handlerCreate">
          {{ $t('dataset.create') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue';
import {mapState} from "vuex";
import {AgGridVue} from "ag-grid-vue3";
import {useI18n} from "vue-i18n";
import TableGridOptions from "@/components/table/TableGridOptions";
import {TableColumnDef} from "@/components/table/TableColumnDef";
import DatasetService from "@/services/admin/DatasetService";

export default defineComponent({
  name: 'DatasetInfo',
  components: {AgGridVue},
  computed: {
    ...mapState(['data'])
  },
  setup()
  {
    const i18n = useI18n();
    const gridOptions = TableGridOptions.createDefaultOptions(i18n);
    return {
      i18n,
      gridOptions
    }
  },
  data()
  {
    return {
      loading: false,
      columnDefs: [],
      infoVisible: false,
      formState: {
        name: null,
        description: null,
        query: null,
        syncMode: 'MANUAL',
        columns: [],
        source: {id: null}
      }
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.data?.headers.forEach((header: any, index: number) => {
        const columnDef: TableColumnDef = {headerName: header, field: header}
        this.columnDefs.push(columnDef)
        const column = {
          id: null,
          name: header.replace('(', '_').replace(')', ''),
          type: 'STRING',
          comment: header,
          defaultValue: null,
          position: index,
          isNullable: false,
          length: 0,
          original: header
        }
        this.formState.columns.push(column)
      })
    },
    handlerCreate()
    {
      this.loading = true
      this.formState.query = this.data?.query;
      this.formState.source.id = this.data?.sourceId;
      DatasetService.saveOrUpdate(this.formState)
        .then(response => {
          if (response.status) {
            this.$Message.success(`${this.$t('dataset.create')} [ ${this.formState.name} ] ${this.$t('common.success')}`)
            this.$router.push('/admin/dataset')
          }
        })
        .finally(() => this.loading = false)
    }
  }
});
</script>

<style scoped>
</style>
