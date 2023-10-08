<template>
  <div>
    <CellGroup>
      <Row>
        <Col span="12">
          <Cell>
            <Text strong>
              {{ info.origin.database.name }}
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
              {{ info.origin.name }}
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
                {{ info.origin.inCreateTime }}
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
                {{ info.origin.inUpdateTime }}
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
                {{ info.origin.engine }}
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
                {{ info.origin.collation }}
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
                {{ info.origin.rows }}
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
                {{ info.origin.format }}
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
        <Input v-model="info.origin.comment"
               type="textarea"
               :rows="4"
               disabled>
        </Input>
      </Col>
    </Row>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import {DataStructureModel} from "@/model/DataStructure";

export default defineComponent({
  name: "TableInfo",
  props: {
    info: {
      type: DataStructureModel
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  data()
  {
    return {
      gridOptions: null
    }
  },
  methods: {
    handlerInitialize()
    {
      console.log(1)
    }
  }
});
</script>
