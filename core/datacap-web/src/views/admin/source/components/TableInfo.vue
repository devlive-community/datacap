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
                  {{ info.inCreateTime }}
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
                  {{ info.inUpdateTime }}
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
                  {{ info.engine }}
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
                  {{ info.collation }}
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
                <FontAwesomeIcon icon="sun"
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
                  {{ info.format }}
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
        </Row>
      </CellGroup>
      <Divider orientation="left"/>
      <Row style="margin-left: 10px;">
        <Col span="1">
          {{ $t('common.comment') }}
        </Col>
        <Col span="23">
          <Input v-model="info.comment"
                 type="textarea"
                 :rows="4"
                 disabled>
          </Input>
        </Col>
      </Row>
    </div>
  </div>
</template>
<script lang="ts">
import {defineComponent, watch} from "vue";
import TableService from "@/services/Table";
import CircularLoading from "@/components/loading/CircularLoading.vue";

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
      info: null
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
          }
        })
        .finally(() => this.loading = false)
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
