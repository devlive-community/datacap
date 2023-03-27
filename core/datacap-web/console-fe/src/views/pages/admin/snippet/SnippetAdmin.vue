<template>
  <div>
    <Card style="width:100%" :title="$t('common.snippet')">
      <template #extra>
        <Tooltip>
          <template #content>{{ $t('common.create') }}</template>
          <Button type="primary" shape="circle" icon="md-add" size="small" @click="handlerCreateOrUpdate()"/>
        </Tooltip>
      </template>
      <Table :loading="loading" :columns="headers" :data="data.content">
        <template #username="{ row }">
          <Avatar style="color: #f56a00;background-color: #fde3cf">{{ row.user.username }}</Avatar>
        </template>
        <template #action="{ row }">
          <Space>
            <Tooltip content="SQL" transfer>
              <Button :disabled="currentUserId !== row.user.id" shape="circle" type="info" size="small" icon="md-eye"
                      @click="handlerShowContent(row.code)"/>
            </Tooltip>
            <Tooltip :content="$t('common.modify')" transfer>
              <Button :disabled="currentUserId !== row.user.id" shape="circle" type="primary" size="small" icon="md-create"
                      @click="handlerCreateOrUpdate(row.id)"/>
            </Tooltip>
            <Tooltip :content="$t('common.quote')" transfer>
              <Button shape="circle" type="dashed" size="small" icon="md-pin" @click="handlerGoConsoleIndex(row.id)"/>
            </Tooltip>
            <Poptip confirm title="Are you sure delete?" popper-class="poptip-box" transfer placement="right-end" @on-ok="handlerDeleteRecord(row.id)">
              <Tooltip :content="$t('common.delete')" transfer>
                <Button :disabled="currentUserId !== row.user.id" shape="circle" type="error" size="small" icon="md-trash"/>
              </Tooltip>
            </Poptip>
          </Space>
        </template>
      </Table>
      <p v-if="!loading" style="margin-top: 10px;">
        <Page v-model="pagination.current" :total="pagination.total" :page-size="pagination.pageSize" show-sizer show-elevator show-total
              @on-page-size-change="handlerSizeChange" @on-change="handlerIndexChange"/>
      </p>
    </Card>
    <SqlDetail v-if="visibleContent" :isVisible="visibleContent" :content="content"
               @close="handlerCloseContent($event)"/>
    <SnippetDetails v-if="visibleSnippetInfo" :isVisible="visibleSnippetInfo" :id="applyId"
                    @close="handlerCloseCreateNew()"/>
  </div>
</template>

<script lang="ts">
import SnippetDetails from "@/views/pages/admin/snippet/SnippetDetails.vue";
import {defineComponent} from "vue";
import {useI18n} from 'vue-i18n';
import Common from "@/common/Common";
import {createHeaders} from "@/views/pages/admin/snippet/SnippetGenerate";
import router from "@/router";
import {ResponsePage} from "@/model/ResponsePage";
import SqlDetail from "@/components/sql/SqlDetail.vue";
import SnippetService from "@/services/SnippetService";

export default defineComponent({
  name: "SnippetAdmin",
  components: {SqlDetail, SnippetDetails},
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
      applyId: 0,
      visibleSnippetInfo: false,
      content: '',
      visibleContent: false,
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
      SnippetService.getSnippets(page, size)
        .then((response) => {
          if (response.status) {
            this.data = response.data;
            this.pagination.total = response.data.total;
          }
          this.loading = false;
        })
    },
    handlerDeleteRecord(id: number)
    {
      SnippetService.delete(id)
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
    handlerShowContent(content: string)
    {
      this.visibleContent = true;
      this.content = content;
    },
    handlerCloseContent(value: boolean)
    {
      this.visibleContent = value;
      this.content = '';
    },
    handlerCreateOrUpdate(value?: number)
    {
      if (value) {
        this.applyId = value;
      }
      this.visibleSnippetInfo = true;
    },
    handlerCloseCreateNew()
    {
      this.visibleSnippetInfo = false;
      this.applyId = 0;
      this.handlerInitialize(this.pagination.current, this.pagination.pageSize);
    },
    handlerGoConsoleIndex(id: number)
    {
      router.push('/console/index?id=' + id + '&from=snippet');
    }
  }
});
</script>
