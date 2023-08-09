<template>
  <div>
    <Card style="width:100%"
          :title="$t('common.source')"
          dis-hover>
      <template #extra>
        <Tooltip>
          <template #content>{{ $t('common.create') }}</template>
          <Button type="primary" shape="circle" icon="md-add" size="small" @click="handlerCreateOrUpdate()"/>
        </Tooltip>
      </template>
      <Table :loading="loading" :columns="headers" :data="data.content">
        <template #name="{ row }">
          <Ellipsis :text="row.name" :length="8" tooltip transfer/>
        </template>
        <template #type="{ row }">
          <Tooltip transfer :content="row.type">
            <Avatar :src="'/static/images/plugin/' + row.type.split(' ')[0] + '.png'" size="small"/>
          </Tooltip>
        </template>
        <template #host="{ row }">
          <Ellipsis :text="row.host" :length="8" tooltip transfer/>
        </template>
        <template #version="{ row }">
          <Tag v-if="row.version"
               color="primary">
            <Ellipsis :length="10"
                      :text="row.version"
                      tooltip
                      transfer>
            </Ellipsis>
          </Tag>
        </template>
        <template #available="{ row }">
          <Button :type="row.available ? 'success' : 'error'"
                  shape="circle"
                  size="small"
                  style="padding: 0; height: auto;">
            <Tooltip v-if="!row.available"
                     :content="row.message"
                     max-width="auto"
                     transfer>
              <Icon type="md-close-circle"
                    size="25">
              </Icon>
            </Tooltip>
            <Icon v-else
                  type="md-checkmark-circle"
                  size="25">
            </Icon>
          </Button>
        </template>
        <template #public="{ row }">
          <Switch v-model="row.public"
                  :disabled="currentUserId !== row.user.id"
                  @on-change="handlerShared(row.public, row.id)">
          </Switch>
        </template>
        <template #action="{ row }">
          <Space>
            <Tooltip :content="$t('common.modify')" transfer>
              <Button :disabled="currentUserId !== row.user.id" shape="circle" type="primary" size="small" icon="md-create"
                      @click="handlerCreateOrUpdate(row.id)"/>
            </Tooltip>
            <Tooltip :content="$t('common.delete')" transfer>
              <Poptip confirm title="Are you sure delete?" popper-class="poptip-box" transfer @on-ok="handlerDeleteRecord(row.id)">
                <Button :disabled="currentUserId !== row.user.id" shape="circle" type="error" size="small" icon="md-trash"/>
              </Poptip>
            </Tooltip>
            <Tooltip :content="$t('common.admin')" transfer>
              <Button :disabled="currentUserId !== row.user.id" shape="circle" type="info" size="small" icon="md-construct"
                      :to="'/admin/source/' + row.id + '/manager'"/>
            </Tooltip>
          </Space>
        </template>
      </Table>
      <p v-if="!loading" style="margin-top: 10px;">
        <Page v-model="pagination.current" :total="pagination.total" :page-size="pagination.pageSize" show-sizer show-elevator show-total
              @on-page-size-change="handlerSizeChange" @on-change="handlerIndexChange"/>
      </p>
    </Card>
    <SourceDetail v-if="visibleSourceInfo" :isVisible="visibleSourceInfo" :id="applyId" @close="handlerCloseCreateNew($event)"/>
  </div>
</template>

<script lang="ts">
import {SourceService} from "@/services/SourceService";
import {createHeaders} from "@/views/admin/source/SourceGenerate";
import {defineComponent} from "vue";
import {useI18n} from 'vue-i18n';
import Common from "@/common/Common";
import {SharedSource} from "@/model/SharedSource";
import {ResponsePage} from "@/model/ResponsePage";
import {Space, Tooltip} from "view-ui-plus";
import SourceDetail from "@/views/admin/source/SourceDetail.vue";

export default defineComponent({
  name: "SourceAdmin",
  components: {SourceDetail, Space, Tooltip},
  setup()
  {
    const i18n = useI18n();
    const headers = createHeaders(i18n);
    const currentUserId = Common.getCurrentUserId();
    return {
      headers,
      currentUserId
    }
  },
  data()
  {
    return {
      data: ResponsePage,
      loading: false,
      visibleSourceInfo: false,
      applyId: 0,
      pagination: {
        total: 0,
        current: 1,
        pageSize: 10
      }
    }
  },
  created()
  {
    this.handlerInitialize(this.pagination.current, this.pagination.pageSize)
  },
  methods: {
    handlerInitialize(page: number, size: number)
    {
      this.loading = true;
      new SourceService()
        .getSources(page, size)
        .then((response) => {
          if (response.status) {
            this.data = response.data;
            this.pagination.total = response.data.total;
          }
          this.loading = false;
        })
    },
    handlerCreateOrUpdate(value?: number)
    {
      if (value) {
        this.applyId = value;
      }
      this.visibleSourceInfo = true;
    },
    handlerCloseCreateNew(value: boolean)
    {
      this.visibleSourceInfo = value;
      this.applyId = 0;
      this.handlerInitialize(this.pagination.current, this.pagination.pageSize);
    },
    handlerDeleteRecord(id: number)
    {
      new SourceService()
        .delete(id)
        .then((response) => {
          if (response.status) {
            this.$Message.success("Delete successful");
            this.handlerInitialize(this.pagination.current, this.pagination.pageSize);
          }
        });
    },
    handlerSizeChange(size: number)
    {
      this.pagination.pageSize = size;
      this.handlerTableChange(this.pagination);
    },
    handlerIndexChange(index: number)
    {
      this.pagination.current = index;
      this.handlerTableChange(this.pagination);
    },
    handlerTableChange(pagination: any)
    {
      this.pagination.current = pagination.current;
      this.pagination.pageSize = pagination.pageSize;
      this.handlerInitialize(pagination.current, pagination.pageSize)
    },
    handlerShared(shared: boolean, sourceId: number)
    {
      const instance: SharedSource = {
        public: shared,
        sourceId: sourceId,
        userId: this.currentUserId
      };
      new SourceService().shared(instance)
        .then((response) => {
          if (response.status) {
            this.handlerInitialize(this.pagination.current, this.pagination.pageSize);
          }
        });
    }
  }
});
</script>
<style scoped>
.poptip-box .ivu-poptip-body-content {
  overflow: visible;
}
</style>
