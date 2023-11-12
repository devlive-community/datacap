<template>
  <div>
    <CircularLoading v-if="loading"
                     :show="loading"
                     style="margin-top: 150px;">
    </CircularLoading>
    <div v-else>
      <CellGroup>
        <Row>
          <Col span="12">
            <Cell>
              <Text strong>
                {{ info.database.name }}
              </Text>
              <template #icon>
                <FontAwesomeIcon icon="database"
                                 size="lg"
                                 style="margin-right: 10px;">
                </FontAwesomeIcon>
              </template>
            </Cell>
          </Col>
          <Col span="12">
            <Cell>
              <Text strong>
                {{ info.name }}
              </Text>
              <template #icon>
                <FontAwesomeIcon icon="table"
                                 size="lg"
                                 style="margin-right: 10px;">
                </FontAwesomeIcon>
              </template>
            </Cell>
          </Col>
          <Col span="12">
            <Cell>
              <Tooltip :content="$t('common.createTime')"
                       transfer>
                <Text strong>
                  {{ info.inCreateTime === 'null' ? $t('common.notSpecified') : info.inCreateTime }}
                </Text>
              </Tooltip>
              <template #icon>
                <FontAwesomeIcon icon="clock"
                                 size="lg"
                                 style="margin-right: 10px;">
                </FontAwesomeIcon>
              </template>
            </Cell>
          </Col>
          <Col span="12">
            <Cell>
              <Tooltip :content="$t('common.updateTime')"
                       transfer>
                <Text strong>
                  {{ info.inUpdateTime === 'null' ? $t('common.notUpdated') : info.inUpdateTime }}
                </Text>
              </Tooltip>
              <template #icon>
                <FontAwesomeIcon icon="clock"
                                 size="lg"
                                 style="margin-right: 10px;">
                </FontAwesomeIcon>
              </template>
            </Cell>
          </Col>
          <Col span="12">
            <Cell>
              <Tooltip :content="$t('common.engine')"
                       transfer>
                <Text strong>
                  {{ info.engine === 'null' ? $t('common.notSpecifiedEngine') : info.engine }}
                </Text>
              </Tooltip>
              <template #icon>
                <FontAwesomeIcon icon="font"
                                 size="lg"
                                 style="margin-right: 10px;">
                </FontAwesomeIcon>
              </template>
            </Cell>
          </Col>
          <Col span="12">
            <Cell>
              <Tooltip :content="$t('common.collation')"
                       transfer>
                <Text strong>
                  {{ info.collation === 'null' ? $t('common.notSpecifiedCollation') : info.collation }}
                </Text>
              </Tooltip>
              <template #icon>
                <FontAwesomeIcon icon="fa-arrow-up-a-z"
                                 size="lg"
                                 style="margin-right: 10px;">
                </FontAwesomeIcon>
              </template>
            </Cell>
          </Col>
        </Row>
      </CellGroup>
      <Divider orientation="left">
        {{ $t('common.data') + $t('common.info') }}
      </Divider>
      <CellGroup>
        <Row>
          <Col span="12">
            <Cell>
              <Tooltip :content="$t('common.totalRows')"
                       transfer>
                <Text strong>
                  {{ info.rows }}
                </Text>
              </Tooltip>
              <template #icon>
                <FontAwesomeIcon icon="arrow-up-1-9"
                                 size="lg"
                                 style="margin-right: 10px;">
                </FontAwesomeIcon>
              </template>
            </Cell>
          </Col>
          <Col span="12">
            <Cell>
              <Tooltip :content="$t('common.format')"
                       transfer>
                <Text strong>
                  {{ info.format === 'null' ? $t('common.notSpecifiedFormat') : info.format }}
                </Text>
              </Tooltip>
              <template #icon>
                <FontAwesomeIcon icon="text-height"
                                 size="lg"
                                 style="margin-right: 10px;">
                </FontAwesomeIcon>
              </template>
            </Cell>
          </Col>
          <Col span="12">
            <Cell>
              <Tooltip :content="$t('common.avgRowLength')"
                       transfer>
                <Text strong>
                  {{ info.avgRowLength === 'null' ? 0 : info.avgRowLength }}
                </Text>
              </Tooltip>
              <template #icon>
                <FontAwesomeIcon icon="arrow-down-1-9"
                                 size="lg"
                                 style="margin-right: 10px;">
                </FontAwesomeIcon>
              </template>
            </Cell>
          </Col>
          <Col span="12">
            <Cell>
              <Tooltip :content="$t('common.dataSize')"
                       transfer>
                <Text strong>
                  {{ info.dataLength === 'null' ? 0 : info.dataLength }}
                </Text>
              </Tooltip>
              <template #icon>
                <FontAwesomeIcon icon="arrow-down-9-1"
                                 size="lg"
                                 style="margin-right: 10px;">
                </FontAwesomeIcon>
              </template>
            </Cell>
          </Col>
          <Col span="12">
            <Cell>
              <Tooltip :content="$t('common.indexSize')"
                       transfer>
                <Text strong>
                  {{ info.indexLength === 'null' ? $t('common.notSpecifiedIndex') : info.indexLength }}
                </Text>
              </Tooltip>
              <template #icon>
                <FontAwesomeIcon icon="magnifying-glass"
                                 size="lg"
                                 style="margin-right: 10px;">
                </FontAwesomeIcon>
              </template>
            </Cell>
          </Col>
          <Col span="12">
            <Cell>
              <Tooltip :content="$t('common.autoIncrement')"
                       transfer>
                <Text strong>
                  {{ info.autoIncrement === 'null' ? $t('common.notSpecifiedPrimaryKey') : info.autoIncrement }}
                </Text>
              </Tooltip>
              <template #icon>
                <FontAwesomeIcon icon="flag"
                                 size="lg"
                                 style="margin-right: 10px;">
                </FontAwesomeIcon>
              </template>
              <template #extra>
                <Poptip v-if="info.autoIncrement !== 'null'"
                        placement="right"
                        transfer>
                  <Tooltip :content="$t('source.manager.resetAutoIncrement')"
                           placement="bottom-end"
                           transfer>
                    <FontAwesomeIcon icon="gear"
                                     size="lg">
                    </FontAwesomeIcon>
                  </Tooltip>
                  <template #content>
                    {{ $t('source.manager.resetTo') }}
                    <InputNumber v-model="autoIncrement"
                                 size="small"
                                 min="0">
                    </InputNumber>
                    <Button size="small"
                            :loading="loadingAutoIncrement"
                            :style="{marginLeft: '5px'}"
                            @click="handlerApply">
                      {{ $t('common.apply') }}
                    </Button>
                  </template>
                </Poptip>
              </template>
            </Cell>
          </Col>
        </Row>
      </CellGroup>
      <Divider orientation="left"/>
      <Form :label-width="80">
        <FormItem :label="$t('common.comment')">
          <Input v-model="info.comment"
                 type="textarea"
                 :rows="4">
          </Input>
        </FormItem>
      </Form>
    </div>
  </div>
</template>
<script lang="ts">
import {defineComponent, watch} from "vue";
import TableService from "@/services/Table";
import CircularLoading from "@/components/loading/CircularLoading.vue";
import {cloneDeep} from "lodash";
import {SqlType, TableFilter} from "@/model/TableFilter";

export default defineComponent({
  name: "TableInfo",
  components: {CircularLoading},
  props: {
    id: {
      type: Number,
      default: 0
    }
  },
  created()
  {
    this.handlerInitialize();
    this.watchId();
  },
  data()
  {
    return {
      loading: false,
      loadingAutoIncrement: false,
      info: null,
      autoIncrement: 0
    }
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true;
      TableService.getById(this.id)
        .then(response => {
          if (response.status) {
            this.info = response.data;
            this.autoIncrement = cloneDeep(this.info.autoIncrement);
          }
        })
        .finally(() => this.loading = false)
    },
    handlerApply()
    {
      this.loadingAutoIncrement = true;
      const configure = new TableFilter();
      configure.type = SqlType.ALTER;
      configure.value = this.autoIncrement;
      TableService.getData(this.id, configure)
        .then(response => {
          if (response.status) {
            this.$Message.success(response.message);
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => this.loadingAutoIncrement = false)
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
  }
});
</script>
