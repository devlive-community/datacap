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
      <Alert v-if="validator"
             type="error">
        {{ validatorMessage }}
      </Alert>
      <CircularLoading v-if="loading"
                       :show="loading">
      </CircularLoading>
      <Form v-else
            style="padding: 0 20px;"
            class="center-form">
        <FormItem style="margin-bottom: 5px;">
          <Row :gutter="10">
            <Col class="w150">{{ $t('dataset.columnName') }}</Col>
            <Col class="w150">{{ $t('dataset.columnAlias') }}</Col>
            <Col class="w100 center">{{ $t('dataset.columnType') }}</Col>
            <Col class="w100 center">{{ $t('dataset.columnMode') }}</Col>
            <Col class="w100">{{ $t('dataset.columnDefaultValue') }}</Col>
            <Col class="w100 center">{{ $t('dataset.columnIsNullable') }}</Col>
            <Col class="w100 center">{{ $t('dataset.columnIsOrderByKey') }}</Col>
            <Col class="w100 center">{{ $t('dataset.columnIsPartitionKey') }}</Col>
            <Col class="w100 center">{{ $t('dataset.columnIsPrimaryKey') }}</Col>
            <Col class="w100 center">{{ $t('dataset.columnIsSampling') }}</Col>
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
              <Col class="w100 center">
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
                <Switch v-model="item.primaryKey"/>
              </Col>
              <Col class="w100 center">
                <Switch v-model="item.samplingKey"
                        @on-change="validatorSampling">
                </Switch>
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
                :disabled="validator"
                @click="infoVisible = true">
          {{ code ? $t('dataset.modify') : $t('dataset.create') }}
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
           :title="code ? $t('dataset.modify') : $t('dataset.create')"
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
        <FormItem :label="$t('common.executor')">
          <RadioGroup v-model="formState.executor">
            <Radio v-for="item in executors"
                   :key="item"
                   :label="item">
              {{ item }}
            </Radio>
          </RadioGroup>
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
                  :label="$t('common.scheduler')">
          <RadioGroup v-model="formState.scheduler">
            <Radio v-for="item in schedulers"
                   :key="item"
                   :label="item">
              {{ item }}
            </Radio>
          </RadioGroup>
        </FormItem>
      </Form>
      <template #footer>
        <Button type="primary"
                :loading="saving"
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
import CircularLoading from "@/components/loading/CircularLoading.vue";
import PluginService from "@/services/admin/PluginService";

export default defineComponent({
  name: 'DatasetInfo',
  components: {CircularLoading, AgGridVue},
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
      saving: false,
      validator: false,
      validatorMessage: null,
      columnDefs: [],
      schedulers: [],
      executors: [],
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
        scheduler: 'Default',
        executor: 'Default'
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
        PluginService.getPlugins()
          .then(response => {
            if (response.status) {
              this.schedulers = response.data['scheduler']
              this.executors = response.data['executor']
            }
          })
        const code = this.$route.params.code
        if (code) {
          this.loading = true
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
            .finally(() => this.loading = false)
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
              primaryKey: false,
              samplingKey: false,
              mode: 'DIMENSION'
            }
            this.formState.columns.push(column)
          })
        }
      })
    },
    handlerCreate()
    {
      this.saving = true
      DatasetService.saveOrUpdate(this.formState)
        .then(response => {
          if (response.status) {
            this.$Message.success(`${this.$t('dataset.create')} [ ${this.formState.name} ] ${this.$t('common.success')}`)
            this.$router.push('/admin/dataset')
          }
        })
        .finally(() => this.saving = false)
    },
    validatorSampling()
    {
      const samplingColumns = this.formState.columns
        .filter((item: { samplingKey: boolean; }) => item.samplingKey)
      if (samplingColumns.length === 0) {
        this.validator = false
        this.validatorMessage = null
        return
      }

      const orderByColumns = this.formState.columns
        .filter((item: { orderByKey: boolean; }) => item.orderByKey)
      const isNameInOrderByColumns = samplingColumns.every((samplingItem: { name: string; }) => {
        return orderByColumns.some((orderByItem: { name: string; }) => orderByItem.name === samplingItem.name);
      })
      if (!isNameInOrderByColumns) {
        this.validator = true
        this.validatorMessage = this.$t('dataset.validatorSamplingTip')
      }
      else {
        this.validator = false
        this.validatorMessage = null
      }
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
