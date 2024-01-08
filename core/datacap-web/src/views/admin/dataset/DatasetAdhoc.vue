<template>
  <div>
    <Layout style="min-height: 600px;">
      <Sider style="max-height: 500px; background-color: white;">
        <Content style="height: 50%; background-color: white;">
          <Divider orientation="left">
            {{ $t('dataset.columnModeMetric') }}
          </Divider>
          <Card :bordered="false"
                dis-hover
                padding="10"
                style="height: 100%; overflow: auto;">
            <Draggable item-key="id"
                       :clone="handlerClone"
                       :group="{ name: 'metrics', pull: 'clone', put: false }"
                       :list="originalMetrics">
              <template #item="{ element }">
                <Tag size="medium">
                  {{ element.name }}
                </Tag>
              </template>
            </Draggable>
          </Card>
        </Content>
        <Content style="height: 50%; background-color: white; margin-top: 50px;">
          <Divider orientation="left">
            {{ $t('dataset.columnModeMetric') }}
          </Divider>
          <Card :bordered="false"
                dis-hover
                padding="10"
                style="height: 100%; overflow: auto;">
            <Draggable item-key="id"
                       :clone="handlerClone"
                       :group="{ name: 'dimensions', pull: 'clone', put: false }"
                       :list="originalDimensions">
              <template #item="{ element }">
                <Tag size="medium">
                  {{ element.name }}
                </Tag>
              </template>
            </Draggable>
          </Card>
        </Content>
      </Sider>
      <Layout>
        <Content style="background-color: white;">
          <List :border="false"
                style="margin-left: 20px;">
            <ListItem>
              {{ $t('dataset.columnModeMetric') }}: &nbsp;
              <Draggable group="metrics"
                         item-key="id"
                         :list="metrics">
                <template #item="{ element }">
                  <Tag size="medium"
                       closable>
                    {{ element.name }}
                  </Tag>
                </template>
              </Draggable>
            </ListItem>
            <ListItem>
              {{ $t('dataset.columnModeDimension') }}: &nbsp;
              <Draggable group="dimensions"
                         item-key="id"
                         :list="dimensions">
                <template #item="{ element }">
                  <Tag size="medium"
                       closable>
                    {{ element.name }}
                  </Tag>
                </template>
              </Draggable>
            </ListItem>
            <ListItem>
              &nbsp;
            </ListItem>
          </List>
        </Content>
        <Layout>
          <Content style="background-color: white;"></Content>
          <Sider style="background-color: white;"></Sider>
        </Layout>
      </Layout>
    </Layout>
  </div>
</template>

<script lang="ts">
import Draggable from 'vuedraggable'
import DatasetService from "@/services/admin/DatasetService";

export default {
  name: 'DatasetAdhoc',
  components: {Draggable},
  data()
  {
    return {
      originalMetrics: [],
      originalDimensions: [],
      metrics: [],
      dimensions: []
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
        const code = this.$route.params.code
        DatasetService.getColumnsByCode(code)
          .then(response => {
            if (response.status) {
              this.originalMetrics = response.data.filter((item: { mode: string; }) => item.mode === 'METRIC')
              this.originalDimensions = response.data.filter((item: { mode: string; }) => item.mode === 'DIMENSION')
            }
            else {
              this.$Message.error(response.message)
            }
          })
      }, 0)
    },
    handlerClone(value: any)
    {
      return value
    }
  }
};
</script>
