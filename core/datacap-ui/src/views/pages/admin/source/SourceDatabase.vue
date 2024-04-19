<template>
  <div class="pl-3 pr-3">
    <CircularLoading v-if="loading" :show="loading"/>
    <div v-else-if="dataInfo">
      <div class="grid w-full grid-cols-3 gap-6 pt-2">
        <div class="flex items-center space-x-2">
          <Database :size="18"/>
          <span>{{ dataInfo?.name }}</span>
        </div>
        <div class="flex items-center space-x-2">
          <Tooltip :content="$t('common.createTime')">
            <Clock :size="18"/>
            <span>{{ dataInfo?.createTime === 'null' ? $t('source.common.notSpecified') : dataInfo.createTime }}</span>
          </Tooltip>
        </div>
        <div class="flex items-center space-x-2">
          <Tooltip :content="$t('common.updateTime')">
            <Clock :size="18"/>
            <span>{{ dataInfo?.updateTime === 'null' ? $t('source.common.notUpdated') : dataInfo.updateTime }}</span>
          </Tooltip>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, watch } from 'vue'
import DatabaseService from '@/services/database.ts'
import { ToastUtils } from '@/utils/toast.ts'
import { DatabaseModel } from '@/model/database.ts'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { Clock, Database } from 'lucide-vue-next'
import Tooltip from '@/views/ui/tooltip'

export default defineComponent({
  name: 'SourceDatabase',
  components: {
    Database, Clock,
    CircularLoading,
    Tooltip
  },
  created()
  {
    this.handlerInitialize()
    this.watchChange()
  },
  data()
  {
    return {
      loading: false,
      submitting: false,
      dataInfo: null as DatabaseModel | null
    }
  },
  methods: {
    handlerInitialize()
    {
      const code = this.$route?.params.database as string
      if (code) {
        this.loading = true
        DatabaseService.getByCode(code)
                       .then(response => {
                         if (response.status) {
                           this.dataInfo = response.data
                         }
                         else {
                           ToastUtils.error(response.message)
                         }
                       })
                       .finally(() => this.loading = false)
      }
    },
    watchChange()
    {
      watch(
          () => this.$route?.params.database,
          () => this.handlerInitialize()
      )
    }
  }
})
</script>
