<template>
  <a-card :title="$t('dashboard.common.list')">
    <template #extra>
      <a-tooltip :title="$t('dashboard.common.create')">
        <router-link to="/admin/dashboard/info" target="_blank">
          <a-button type="primary" shape="circle" size="small">
            <template #icon>
              <ShadcnIcon icon="Plus" :size="15"/>
            </template>
          </a-button>
        </router-link>
      </a-tooltip>
    </template>

    <div class="mb-3 min-h-screen p-2">
      <a-spin :spinning="loading">
        <a-row :gutter="16">
          <a-col v-for="item in data" :key="item.code" :span="6">
            <a-card class="mb-4">
              <template #title>
                <router-link :to="`/admin/dashboard/preview/${item.code}`" target="_blank">{{ item.name }}</router-link>
              </template>

              <template #extra>
                <a-space class="items-center">
                  <div v-if="item.description" class="cursor-pointer mt-1">
                    <a-tooltip :title="item.description">
                      <ShadcnIcon icon="Info" :size="20"/>
                    </a-tooltip>
                  </div>

                  <a-dropdown trigger="click" placement="bottomRight">
                    <a-button shape="circle" size="small">
                      <template #icon>
                        <ShadcnIcon icon="Cog" :size="18"/>
                      </template>
                    </a-button>

                    <template #overlay>
                      <a-menu>
                        <a-menu-item>
                          <router-link :to="`/admin/dashboard/info/${item.code}`" target="_blank" class="flex items-center space-x-2">
                            <ShadcnIcon icon="Pencil" :size="15"/>
                            <span>{{ $t('dashboard.common.modify') }}</span>
                          </router-link>
                        </a-menu-item>

                        <a-menu-item @click="visibleDelete(true, item)">
                          <div class="flex items-center space-x-2">
                            <ShadcnIcon icon="Trash" :size="15"/>
                            <span>{{ $t('dashboard.common.delete') }}</span>
                          </div>
                        </a-menu-item>
                      </a-menu>
                    </template>
                  </a-dropdown>
                </a-space>
              </template>

              <div class="p-3">
                <div class="aspect-video w-full relative overflow-hidden rounded-md flex items-center justify-center">
                  <img class="w-32 h-32 object-cover"
                       :src="`${item.avatar?.path ? item.avatar.path : '/static/images/dashboard.png'}`"
                       :alt="item.name"/>
                </div>

                <div class="text-xs text-muted-foreground text-right mt-2">{{ item.createTime }}</div>
              </div>
            </a-card>
          </a-col>

          <a-col :span="24">
            <div v-if="!loading && data.length === 0" class="text-center">
              {{ $t('common.noData') }}
            </div>
          </a-col>
        </a-row>

        <div v-if="data.length > 0" class="py-2 flex justify-end">
          <a-pagination :current="pageIndex"
                        :page-size="pageSize"
                        :total="dataCount"
                        show-size-changer
                        :page-size-options="['12', '24', '36']"
                        @change="onPageChange"
                        @show-size-change="onSizeChange"/>
        </div>
      </a-spin>
    </div>
  </a-card>

  <DashboardDelete v-if="deleteVisible"
                   :is-visible="deleteVisible"
                   :data="dataInfo"
                   @close="visibleDelete(false, null)"/>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import DashboardService from '@/services/dashboard'
import { FilterModel } from '@/model/filter'
import { DashboardModel } from '@/model/dashboard'
import DashboardDelete from '@/views/pages/admin/dashboard/DashboardDelete.vue'

defineOptions({ name: 'DashboardHome' })

const filter: FilterModel = new FilterModel()
filter.size = 12

const loading = ref(false)
const deleteVisible = ref(false)
const data = ref<DashboardModel[]>([])
const pageIndex = ref(1)
const pageSize = ref(12)
const dataCount = ref(0)
const dataInfo = ref<DashboardModel | null>(null)

const handleInitialize = () => {
  loading.value = true
  DashboardService.getAll(filter)
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
  handleInitialize()
}

const onPageChange = (value: number) => fetchData(value)

const onSizeChange = (_current: number, size: number) => {
  pageSize.value = size
  fetchData(pageIndex.value)
}

const visibleDelete = (opened: boolean, info: DashboardModel | null) => {
  deleteVisible.value = opened
  dataInfo.value = info
  if (!opened) {
    handleInitialize()
  }
}

handleInitialize()
</script>
