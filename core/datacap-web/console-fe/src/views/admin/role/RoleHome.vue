<template>
  <div>
    <Card style="width:100%" :title="$t('common.authority')">
      <template #extra>
        <Tooltip>
          <template #content>{{ $t('common.create') }}</template>
          <Button type="primary" shape="circle" icon="md-add" size="small" @click="handlerVisibleDetail(null, true)"/>
        </Tooltip>
      </template>
      <Table :loading="loading" :columns="headers" :data="finalData?.content">
        <template #action="{ row }">
          <Space>
            <Tooltip :content="$t('common.modify')" transfer>
              <Button shape="circle" type="primary" size="small" icon="md-create" @click="handlerVisibleDetail(row.id, true)"/>
            </Tooltip>
            <Tooltip :content="$t('common.menu')" transfer>
              <Button shape="circle" type="info" size="small" icon="md-menu" @click="handlerVisibleMenuTree(row.id)"/>
            </Tooltip>
          </Space>
        </template>
      </Table>
      <p v-if="!loading" style="margin-top: 10px;">
        <Page v-model="pagination.current" :total="pagination.total" :page-size="pagination.size" show-sizer show-elevator show-total
              @on-page-size-change="handlerSizeChange" @on-change="handlerIndexChange"/>
      </p>
    </Card>
    <RoleDetails v-if="visibleDetail" :isVisible="visibleDetail" :id="applyId" @close="handlerVisibleDetail(null, $event)"/>
    <menu-tree-component v-if="visibleMenuTree"
                         :isVisible="visibleMenuTree"
                         :role-id="applyId"
                         @close="handlerVisibleMenuTree(null)">
    </menu-tree-component>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import {useI18n} from 'vue-i18n';
import {createHeaders} from "@/views/admin/role/RoleGenerate";
import RoleService from "@/services/admin/RoleService";
import {Filter} from "@/model/Filter";
import {ResponsePage} from "@/model/ResponsePage";
import RoleDetails from "@/views/admin/role/RoleDetails.vue";
import {Pagination, PaginationBuilder} from "@/model/Pagination";
import MenuTreeComponent from "@/views/admin/menu/components/MenuTree.vue";

const filter: Filter = new Filter();
const pagination: Pagination = PaginationBuilder.newInstance();
export default defineComponent({
  name: 'AdminRoleHome',
  components: {MenuTreeComponent, RoleDetails},
  setup()
  {
    const i18n = useI18n();
    const headers = createHeaders(i18n);
    return {
      headers,
      filter,
      pagination
    }
  },
  data()
  {
    return {
      loading: false,
      visibleDetail: false,
      applyId: null,
      finalData: null as ResponsePage,
      visibleMenuTree: false
    }
  },
  created()
  {
    this.handlerInitialize(this.filter);
  },
  methods: {
    handlerInitialize(filter: Filter)
    {
      this.loading = true;
      RoleService.getAll(filter)
        .then((response) => {
          if (response.status) {
            this.finalData = response.data;
            this.pagination.total = response.data.total;
          }
        })
        .finally(() => {
          this.loading = false
        })
    },
    handlerVisibleDetail(value: number, isOpened: boolean)
    {
      if (isOpened) {
        this.applyId = value;
        this.visibleDetail = true;
      }
      else {
        this.applyId = null;
        this.visibleDetail = false;
      }
    },
    handlerSizeChange(size: number)
    {
      this.pagination.size = size;
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
      this.pagination.size = pagination.size;
      this.filter.page = pagination.current;
      this.filter.size = pagination.size;
      this.handlerInitialize(this.filter);
    },
    handlerVisibleMenuTree(value: number)
    {
      this.applyId = value;
      this.visibleMenuTree = !this.visibleMenuTree;
    }
  }
})
</script>
