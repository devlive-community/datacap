<template>
  <div>
    <div v-if="data || id">
      <Divider orientation="left">
        {{ $t('dataset.dataPreview') }}
      </Divider>
      <AgGridVue v-if="data?.columns"
                 :style="{height: '300px'}"
                 class="ag-theme-datacap"
                 :pagination="true"
                 :columnDefs="columnDefs"
                 :rowData="data.columns"
                 :gridOptions="gridOptions">
      </AgGridVue>
      <Result v-else
              type="warning">
        <template #desc>
          {{ i18n.t('dataset.modifyNotSupportDataPreview') }}
        </template>
      </Result>
      <Divider orientation="left">
        {{ $t('dataset.dataColumn') }}
      </Divider>
      <Form style="padding: 0 20px">
        <FormItem style="margin-bottom: 5px;">
          <Row :gutter="10">
            <Col span="3">{{ $t('dataset.columnName') }}</Col>
            <Col span="2">{{ $t('dataset.columnType') }}</Col>
            <Col span="2">{{ $t('dataset.columnMode') }}</Col>
            <Col span="3">{{ $t('dataset.columnDefaultValue') }}</Col>
            <Col span="3">{{ $t('dataset.columnIsNullable') }}</Col>
            <Col span="3">{{ $t('dataset.columnIsOrderByKey') }}</Col>
            <Col span="3">{{ $t('dataset.columnLength') }}</Col>
            <Col span="5">{{ $t('dataset.columnComment') }}</Col>
          </Row>
        </FormItem>
        <template v-for="(item, index) in formState.columns" :key="index">
          <FormItem>
            <Row :gutter="10">
              <Col span="3">
                <Input v-model="item.name"
                       type="text">
                </Input>
              </Col>
              <Col span="2">
                <Select v-model="item.type">
                  <Option value="STRING">{{ $t('dataset.columnTypeString') }}</Option>
                  <Option value="NUMBER">{{ $t('dataset.columnTypeNumber') }}</Option>
                  <Option value="BOOLEAN">{{ $t('dataset.columnTypeBoolean') }}</Option>
                </Select>
              </Col>
              <Col span="2">
                <Switch v-model="item.mode"
                        size="large"
                        true-value="METRIC"
                        false-value="DIMENSION">
                  <template #open>
                    {{ $t('dataset.columnModeMetric') }}
                  </template>
                  <template #close>
                    {{ $t('dataset.columnModeDimension') }}
                  </template>
                </Switch>
              </Col>
              <Col span="3">
                <Input v-model="item.defaultValue"
                       type="text">
                </Input>
              </Col>
              <Col span="3">
                <Switch v-model="item.nullable"/>
              </Col>
              <Col span="3">
                <Switch v-model="item.orderByKey"/>
              </Col>
              <Col span="3">
                <InputNumber v-model="item.length"/>
              </Col>
              <Col span="5">
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
        {{ id ? $t('dataset.modify') : $t('dataset.create') }}
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
           :title="id ? $t('dataset.modify') : $t('dataset.create')"
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
            <Radio label="TIMING">{{ $t('dataset.syncModeTiming') }}</Radio>
            <Radio label="OUT_SYNC">{{ $t('dataset.syncModeOutSync') }}</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem v-if="formState.syncMode === 'TIMING'"
                  :label="$t('dataset.columnExpression')">
          <Input v-model="formState.expression"
                 placeholder="0 0 * * * ?">
          </Input>
        </FormItem>
      </Form>
      <template #footer>
        <Button type="primary"
                :loading="loading"
                @click="handlerCreate">
          {{ id ? $t('dataset.modify') : $t('dataset.create') }}
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
import {HttpCommon} from "@/common/HttpCommon";

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
      id: null,
      loading: false,
      columnDefs: [],
      infoVisible: false,
      formState: {
        id: null,
        name: null,
        description: null,
        query: null,
        syncMode: 'MANUAL',
        columns: [],
        source: {id: null},
        expression: null
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
      setTimeout(() => {
        const id = this.$route.query.id
        if (id) {
          this.id = id

          const axios = new HttpCommon().getAxios();
          axios.all([DatasetService.getById(this.id), DatasetService.getColumns(this.id)])
            .then(axios.spread((info, column) => {
              if (info.status) {
                this.formState = info.data
                this.formState.source.id = info.data.source.id
              }
              if (column.status) {
                this.formState.columns = column.data
              }
            }))
        }
        else {
          this.formState.source.id = this.data?.sourceId
          this.formState.query = this.data?.query
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
              nullable: false,
              length: 0,
              original: header,
              orderByKey: false,
              mode: 'DIMENSION'
            }
            this.formState.columns.push(column)
          })
        }
      })
    },
    handlerCreate()
    {
      this.loading = true
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
