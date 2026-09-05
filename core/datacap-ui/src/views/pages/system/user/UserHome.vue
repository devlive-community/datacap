<template>
  <a-card>
    <template #title>
      <div class="ml-2 font-normal text-sm">{{ $t('user.common.list') }}</div>
    </template>

    <template #extra>
      <a-button size="small" shape="circle" @click="handlerChangeInfo(true, null)">
        <template #icon>
          <ShadcnIcon icon="Plus"/>
        </template>
      </a-button>
    </template>

    <a-spin :spinning="loading">
      <a-table size="small"
               :columns="headers"
               :data-source="data"
               :pagination="false"
               row-key="id">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'role'">
            <a-tag v-for="role in record.roles" :key="role.id">{{ role.name }}</a-tag>
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-tooltip :title="$t('user.common.assignRole')">
                <a-button size="small" shape="circle" @click="handlerChangeRole(true, record)">
                  <ShadcnIcon icon="SquareArrowUp" size="15"/>
                </a-button>
              </a-tooltip>

              <a-tooltip :title="$t('common.editData')">
                <a-button size="small" shape="circle" @click="handlerChangeInfo(true, record)">
                  <ShadcnIcon icon="Pencil" size="15"/>
                </a-button>
              </a-tooltip>
            </a-space>
          </template>
        </template>
      </a-table>

      <a-pagination v-model:current="pageIndex"
                    class="py-2"
                    :page-size="pageSize"
                    :total="dataCount"
                    show-size-changer
                    :page-size-options="['10', '20', '50']"
                    :show-total="(total: number) => `${ total }`"
                    @change="onPageChange"
                    @show-size-change="onSizeChange"/>
    </a-spin>
  </a-card>

  <UserRole v-if="dataRoleVisible"
            :is-visible="dataRoleVisible"
            :info="dataInfo"
            @close="handlerChangeRole(false, null)"/>

  <UserInfo v-if="dataInfoVisible"
            :is-visible="dataInfoVisible"
            :info="dataInfo"
            @close="handlerChangeInfo(false, null)"/>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { FilterModel } from '@/model/filter'
import UserService from '@/services/user'
import { useHeaders } from './UserUtils'
import { UserModel } from '@/model/user'
import UserInfo from '@/views/pages/system/user/UserInfo.vue'
import UserRole from '@/views/pages/system/user/components/UserRole.vue'

const filter: FilterModel = new FilterModel()
const { headers } = useHeaders()

const loading = ref(false)
const dataRoleVisible = ref(false)
const data = ref<any[]>([])
const pageIndex = ref(1)
const pageSize = ref(10)
const dataCount = ref(0)
const dataInfo = ref<UserModel | null>(null)
const dataInfoVisible = ref(false)

const handlerInitialize = () => {
  loading.value = true
  UserService.getAll(filter)
             .then((response) => {
               if (response.status) {
                 data.value = response.data.content
                 dataCount.value = response.data.total
                 pageSize.value = response.data.size
                 pageIndex.value = response.data.page
               }
             })
             .finally(() => (loading.value = false))
}

const fetchData = (value: number) => {
  filter.page = value
  filter.size = pageSize.value
  handlerInitialize()
}

const onPageChange = (value: number) => fetchData(value)

const onSizeChange = (_current: number, size: number) => {
  pageSize.value = size
  fetchData(pageIndex.value)
}

const handlerChangeRole = (isOpen: boolean, info: UserModel | null) => {
  dataRoleVisible.value = isOpen
  dataInfo.value = info
  if (!isOpen) {
    handlerInitialize()
  }
}

const handlerChangeInfo = (opened: boolean, info: UserModel | null) => {
  dataInfoVisible.value = opened
  dataInfo.value = info
  if (!opened) {
    handlerInitialize()
  }
}

onMounted(() => handlerInitialize())
</script>
