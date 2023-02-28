<template>
  <div>
    <Card style="width:100%">
      <template #title>
        <Space>
          <SourceSelect @changeValue="handlerChangeValue($event)"/>
        </Space>
      </template>
      <Result v-if="!applySource" type="warning">
        <template #desc>
          {{ $t('alert.pleaseSelectSource') }}
        </template>
      </Result>
      <div v-else style="min-height: 300px;">
        <Spin fix :show="loading">
        </Spin>
        <Result v-if="!isSupported && !loading" type="error">
          <template #desc>
            {{ $t('alert.currentSourceNotSupportOperator') }}
          </template>
          <template #extra>
            <div>{{ $t('alert.currentSourceFixedTooltip') }}:</div>
            <div>
              <Icon type="ios-close-circle-outline" color="#ed4014"/>
              {{ $t('alert.currentSourceFixedTooltipStep1') }}
              <a class="ivu-ml-16" target="_blank" href="https://github.com/EdurtIO/datacap/issues/new/choose">
                Go
                <Icon type="ios-arrow-forward"/>
              </a>
            </div>
            <div>
              <Icon type="ios-close-circle-outline" color="#ed4014"/>
              {{ $t('alert.currentSourceFixedTooltipStep2') }}
              <a class="ivu-ml-16">
                <router-link to="/admin/template/sql">
                  Go
                  <Icon type="ios-arrow-forward"/>
                </router-link>
              </a>
            </div>
          </template>
        </Result>
        <Table v-else :columns="headers" :data="columns">
          <template #action="{ row }">
            <Space>
              <Tooltip :content="$t('common.modify')" transfer>
                <Button shape="circle" type="primary" size="small" icon="md-eye"
                        @click="handlerShowContent(row.content)"/>
              </Tooltip>
            </Space>
          </template>
        </Table>
      </div>
    </Card>
    <SqlDetail v-if="visibleContent" :isVisible="visibleContent" :content="content"
               @close="handlerCloseContent($event)"/>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {useI18n} from 'vue-i18n';
import SourceSelect from "@/components/source/SourceSelect.vue";
import TemplateSqlService from "@/services/template/TemplateSqlService";
import {SqlBody} from "@/model/template/SqlBody";
import {createHeaders} from "@/views/pages/admin/monitor/processor/ProcessorGenerate";
import SqlDetail from "@/components/sql/SqlDetail.vue";

export default defineComponent({
  name: "ProcessorAdmin",
  components: {SourceSelect, SqlDetail},
  setup()
  {
    const i18n = useI18n();
    const headers = createHeaders(i18n);
    return {
      headers
    }
  },
  data()
  {
    return {
      columns: [],
      applySource: null,
      applySourceType: null,
      loading: false,
      isSupported: false,
      content: null,
      visibleContent: false
    }
  },
  methods: {
    handlerInitialize()
    {
      const sqlBody: SqlBody = {
        templateName: 'getAllRunningProcess',
        sourceId: this.applySource,
        configure: {}
      }
      TemplateSqlService.execute(sqlBody)
        .then((response) => {
          if (response.status) {
            this.isSupported = true;
            this.columns = response.data.columns;
          }
          else {
            this.isSupported = false;
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handlerChangeValue(value: string)
    {
      this.loading = true;
      const idAndType = value.split(':');
      this.applySource = idAndType[0];
      this.applySourceType = idAndType[1];
      this.handlerInitialize();
    },
    handlerShowContent(content: string)
    {
      this.visibleContent = true;
      this.content = content;
    },
    handlerCloseContent(value: boolean)
    {
      this.visibleContent = value;
      this.content = null;
    },
  }
});
</script>
