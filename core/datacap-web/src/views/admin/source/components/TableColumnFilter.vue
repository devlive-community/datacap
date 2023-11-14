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
          <template v-for="(item, index) in formState.filters">
            <FormItem :key="index"
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
                  <Select v-model="item.operation"
                          size="small">
                    <Option v-for="operation in item.operations"
                            :label="operation"
                            :key="operation"
                            :value="operation">
                    </Option>
                  </Select>
                </Col>
                <Col span="6"
                     offset="1">
                  <Input v-model="item.filter"
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
          </template>
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
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent, watch} from "vue";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import {ColumnFilter} from "@/model/TableFilter";

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
    }
  },
  created()
  {
    this.watchId();
  },
  data()
  {
    return {
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
    handlerFetchOperations(value, filter: ColumnFilter)
    {
      const type = this.types[value];
      filter.column = this.columns[value];
      if (type === 'Long' || type === 'Double' || type === 'Integer') {
        filter.operations = ['=', '!=', '>', '<', '>=', '<='];
      }
      else {
        filter.operations = ['=', '!=', 'LIKE', 'NOT LIKE', 'IS NULL', 'IS NOT NULL'];
      }
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
