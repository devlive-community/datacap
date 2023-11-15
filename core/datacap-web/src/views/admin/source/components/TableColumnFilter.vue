<template>
  <div>
    <Modal v-model="visible"
           width="40%"
           :title="$t('source.manager.filter')"
           :closable="false"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <CircularLoading v-if="loading"
                       :show="loading"
                       style="margin-top: 150px;">
      </CircularLoading>
      <div v-else>
        <Form :model="formState"
              :label-width="80">
          <FormItem :label="$t('common.condition')"
                    :style="{marginBottom: '0px'}">
            <Row>
              <Col span="6">
                <Select v-model="formState.condition"
                        size="small">
                  <Option value="AND">AND</Option>
                  <Option value="OR">OR</Option>
                </Select>
              </Col>
            </Row>
          </FormItem>
          <Divider orientation="left"
                   size="small"
                   :style="{margin: '0'}">
            {{ $t('source.manager.filter') }}
          </Divider>
          <FormItem v-for="(item, index) in formState.filters"
                    :key="index"
                    :style="{marginBottom: '0px'}">
            <Row>
              <Col span="6"
                   offset="1">
                <Select v-model="item.index"
                        size="small"
                        @on-change="handlerFetchOperations($event, item)">
                  <Option v-for="(column, index) in columns"
                          :label="column"
                          :key="index"
                          :value="index">
                  </Option>
                </Select>
              </Col>
              <Col span="6"
                   offset="1">
                <Select v-model="item.operator"
                        size="small">
                  <Option v-for="operation in item.operations"
                          :label="operation"
                          :key="operation"
                          :value="Object.keys(OPERATOR)[Object.values(OPERATOR).indexOf(operation)]">
                  </Option>
                </Select>
              </Col>
              <Col span="6"
                   offset="1">
                <Input v-model="item.value"
                       size="small">
                </Input>
              </Col>
              <Col span="1"
                   offset="1">
                <Button size="small"
                        type="error"
                        shape="circle"
                        icon="md-remove"
                        @click="handlerRemoveFilter(index)">
                </Button>
              </Col>
            </Row>
          </FormItem>
          <FormItem :style="{marginBottom: '0px'}">
            <Row>
              <Col span="6"
                   offset="1">
                <Button type="dashed"
                        @click="handlerAddFilter"
                        size="small"
                        icon="md-add">
                  {{ $t('source.manager.addFilter') }}
                </Button>
              </Col>
            </Row>
          </FormItem>
        </Form>
      </div>
      <template #footer>
        <Button danger
                @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <Button type="primary"
                @click="handlerApplyFilter">
          {{ $t('common.apply') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent, watch} from "vue";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import {ColumnFilter} from "@/model/TableFilter";
import {cloneDeep} from "lodash";
import {Operator} from "@/enum/Operator";

export default defineComponent({
  name: "TableFilter",
  components: {CircularLoading},
  props: {
    isVisible: {
      type: Boolean
    },
    columns: {
      type: Array
    },
    types: {
      type: Array
    },
    configure: {
      type: Object
    }
  },
  created()
  {
    if (this.configure) {
      this.formState = cloneDeep(this.configure);
    }
    this.watchId();
  },
  data()
  {
    return {
      OPERATOR: Operator,
      loading: false,
      formState: {
        condition: 'AND',
        filters: []
      }
    }
  },
  methods: {
    handlerAddFilter()
    {
      const filter: ColumnFilter = new ColumnFilter();
      this.formState.filters.push(filter);
    },
    handlerRemoveFilter(index: number)
    {
      this.formState.filters.splice(index, 1);
    },
    handlerFetchOperations(value: number, filter: ColumnFilter)
    {
      const type = this.types[value];
      filter.column = this.columns[value];
      if (type === 'Long' || type === 'Double' || type === 'Integer') {
        filter.operations = [Operator.EQ, Operator.NEQ, Operator.GT, Operator.LT, Operator.GTE, Operator.LTE];
      }
      else {
        filter.operations = [Operator.EQ, Operator.NEQ, Operator.LIKE, Operator.NLIKE, Operator.NULL, Operator.NNULL];
      }
    },
    handlerApplyFilter()
    {
      this.$emit('apply', this.formState);
      this.handlerCancel();
    },
    handlerCancel()
    {
      this.visible = false;
    },
    watchId()
    {
      watch(
        () => this.id,
        () => {
          this.handlerInitialize();
        }
      );
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
