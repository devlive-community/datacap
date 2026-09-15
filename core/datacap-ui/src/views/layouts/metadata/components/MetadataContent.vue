<template>
  <div v-if="!originalDatabase">
    <a-alert type="warning" :message="$t('source.tip.notSelectedNode')"/>
  </div>

  <a-tabs v-else v-model:activeKey="selectTab" @change="onChange">
    <a-tab-pane key="info">
      <template #tab>
        <div class="flex items-center space-x-1">
          <ShadcnIcon icon="Info"/>
          <span>{{ $t('source.common.info') }}</span>
        </div>
      </template>

      <RouterView/>
    </a-tab-pane>

    <a-tab-pane key="structure" :disabled="!originalTable">
      <template #tab>
        <div class="flex items-center space-x-2">
          <ShadcnIcon icon="LayoutPanelTop"/>
          <span>{{ $t('source.common.structure') }}</span>
        </div>
      </template>

      <RouterView/>
    </a-tab-pane>

    <a-tab-pane key="data" :disabled="!originalTable">
      <template #tab>
        <div class="flex items-center space-x-2">
          <ShadcnIcon icon="Table"/>
          <span>{{ $t('source.common.tableData') }}</span>
        </div>
      </template>

      <RouterView/>
    </a-tab-pane>

    <a-tab-pane key="statement" :disabled="!originalTable">
      <template #tab>
        <div class="flex items-center space-x-2">
          <ShadcnIcon icon="SatelliteDish"/>
          <span>{{ $t('source.common.statement') }}</span>
        </div>
      </template>

      <RouterView/>
    </a-tab-pane>

    <a-tab-pane key="erDiagram" :disabled="!originalTable">
      <template #tab>
        <div class="flex items-center space-x-2">
          <ShadcnIcon icon="Wind"/>
          <span>{{ $t('source.common.erDiagram') }}</span>
        </div>
      </template>

      <RouterView/>
    </a-tab-pane>
  </a-tabs>
</template>

<script lang="ts" setup>
import { onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

defineOptions({ name: 'MetadataContent' })

const route = useRoute()
const router = useRouter()

const selectTab = ref<string | null>(null)
const originalSource = ref<string | null>(null)
const originalDatabase = ref<string | null>(null)
const originalTable = ref<string | undefined>(undefined)

const handleInitialize = () => {
  originalSource.value = String(route.params?.source || '')
  originalDatabase.value = String(route.params?.database || '')
  originalTable.value = route.params?.table ? String(route.params.table) : undefined
  selectTab.value = String(route.meta.type || '')
}

const onChange = () => router.push(`/admin/source/${ originalSource.value }/d/${ originalDatabase.value }/t/${ selectTab.value }/${ originalTable.value }`)

watch(
    () => route.params.table,
    () => handleInitialize()
)

onMounted(() => handleInitialize())
</script>
