<template>
  <div>
    <div v-if="data || code">
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
      <Form style="padding: 0 20px;"
            class="center-form">
        <FormItem style="margin-bottom: 5px;">
          <Row :gutter="10">
            <Col class="w150">{{ $t('dataset.columnName') }}</Col>
            <Col class="w150">{{ $t('dataset.columnAlias') }}</Col>
            <Col class="w100 center">{{ $t('dataset.columnType') }}</Col>
            <Col class="w100 center">{{ $t('dataset.columnMode') }}</Col>
            <Col class="w150">{{ $t('dataset.columnDefaultValue') }}</Col>
            <Col class="w100 center">{{ $t('dataset.columnIsNullable') }}</Col>
            <Col class="w100 center">{{ $t('dataset.columnIsOrderByKey') }}</Col>
            <Col class="w100 center">{{ $t('dataset.columnIsPartitionKey') }}</Col>
            <Col class="w100 center">{{ $t('dataset.columnLength') }}</Col>
            <Col class="w200">{{ $t('dataset.columnComment') }}</Col>
          </Row>
        </FormItem>
        <template v-for="(item, index) in formState.columns"
                  :key="index">
          <FormItem>
            <Row :gutter="10">
              <Col class="w150">
                <Input v-model="item.name"
                       type="text">
                </Input>
              </Col>
              <Col class="w150">
                <Input v-model="item.aliasName"
                       type="text">
                </Input>
              </Col>
              <Col class="w100">
                <Select v-model="item.type">
                  <Option value="STRING">{{ $t('dataset.columnTypeString') }}</Option>
                  <Option value="NUMBER">{{ $t('dataset.columnTypeNumber') }}</Option>
                  <Option value="BOOLEAN">{{ $t('dataset.columnTypeBoolean') }}</Option>
                </Select>
              </Col>
              <Col class="w100 center">
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
              <Col class="w150">
                <Input v-model="item.defaultValue"
                       type="text">
                </Input>
              </Col>
              <Col class="w100 center">
                <Switch v-model="item.nullable"/>
              </Col>
              <Col class="w100 center">
                <Switch v-model="item.orderByKey"/>
              </Col>
              <Col class="w100 center">
                <Switch v-model="item.partitionKey"/>
              </Col>
              <Col class="w100 center">
                <InputNumber v-model="item.length"
                             min="0"
                             max="65536">
                </InputNumber>
              </Col>
              <Col class="w200">
                <Input v-model="item.comment"
                       type="textarea">
                </Input>
              </Col>
            </Row>
          </FormItem>
        </template>
        <Button type="primary"
                style="margin-bottom: 15px; margin-right: 10px; float: right;"
                @click="infoVisible = true">
          {{ id ? $t('dataset.modify') : $t('dataset.create') }}
        </Button>
      </Form>
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
        <FormItem v-if="formState.syncMode === 'TIMING'"
                  :label="$t('dataset.actuator')">
          <RadioGroup v-model="formState.actuator">
            <Radio v-for="item in actuators"
                   :key="item"
                   :label="item">{{ item }}
            </Radio>
          </RadioGroup>
        </FormItem>
      </Form>
      <template #footer>
        <Button type="primary"
                :loading="loading"
                @click="handlerCreate">
          {{ code ? $t('dataset.modify') : $t('dataset.create') }}
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
      code: null,
      loading: false,
      columnDefs: [],
      actuators: [],
      infoVisible: false,
      formState: {
        id: null,
        name: null,
        description: null,
        query: null,
        syncMode: 'MANUAL',
        columns: [],
        source: {id: null},
        expression: null,
        actuator: null
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
      DatasetService.getActuators()
        .then(response => {
          if (response.status) {
            this.actuators = response.data
          }
        })

      setTimeout(() => {
        const code = this.$route.params.code
        if (code) {
          this.code = code
          const axios = new HttpCommon().getAxios();
          axios.all([DatasetService.getByCode(this.code), DatasetService.getColumnsByCode(this.code)])
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
              name: `column_${index + 1}`,
              aliasName: header.replace('(', '_').replace(')', ''),
              type: 'STRING',
              comment: header,
              defaultValue: null,
              position: index,
              nullable: false,
              length: 0,
              original: header,
              orderByKey: false,
              partitionKey: false,
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
.center-form {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.w100 {
  width: 100px;
}

.w150 {
  width: 150px;
}

.w200 {
  width: 200px;
}

.center {
  text-align: center;
}
</style>
