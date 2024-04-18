<template>
  <div class="pl-3 pr-3">
    <CircularLoading v-if="loading" :show="loading"/>
    <div v-else-if="dataInfo">
      <div class="grid w-full grid-cols-3 gap-6 pt-2">
        <div class="flex items-center">
          <Database :size="18" class="mr-2"/>
          {{ dataInfo.database?.name }}
        </div>
        <div class="flex items-center">
          <Table :size="18" class="mr-2"/>
          {{ dataInfo?.name }}
        </div>
        <div class="flex items-center">
          <Tooltip :content="$t('common.createTime')">
            <Clock :size="18" class="mr-2"/>
            {{ dataInfo?.inCreateTime === 'null' ? $t('source.common.notSpecified') : dataInfo.inCreateTime }}
          </Tooltip>
        </div>
        <div class="flex items-center">
          <Tooltip :content="$t('common.updateTime')">
            <Clock :size="18" class="mr-2"/>
            {{ dataInfo?.inUpdateTime === 'null' ? $t('source.common.notUpdated') : dataInfo.inUpdateTime }}
          </Tooltip>
        </div>
        <div class="flex items-center">
          <Tooltip :content="$t('source.common.engine')">
            <CalendarHeart :size="18" class="mr-2"/>
            {{ dataInfo.engine === 'null' ? $t('source.common.notSpecifiedEngine') : dataInfo.engine }}
          </Tooltip>
        </div>
        <div class="flex items-center">
          <Tooltip :content="$t('source.common.collation')">
            <ArrowUpDown :size="18" class="mr-2"/>
            {{ dataInfo.collation === 'null' ? $t('source.common.notSpecifiedCollation') : dataInfo.collation }}
          </Tooltip>
        </div>
      </div>
      <Divider class="mt-4 mb-4"/>
      <div class="grid w-full grid-cols-3 gap-6 pt-2">
        <div class="flex items-center">
          <Tooltip :content="$t('source.common.totalRows')">
            <TableCellsMerge :size="18" class="mr-2"/>
            {{ dataInfo.rows }}
          </Tooltip>
        </div>
        <div class="flex items-center">
          <Tooltip :content="$t('source.common.format')">
            <RemoveFormatting :size="18" class="mr-2"/>
            {{ dataInfo.format === 'null' ? $t('source.common.notSpecifiedFormat') : dataInfo.format }}
          </Tooltip>
        </div>
        <div class="flex items-center">
          <Tooltip :content="$t('source.common.avgRowLength')">
            <ArrowUp10 :size="18" class="mr-2"/>
            {{ dataInfo.avgRowLength === 'null' ? 0 : dataInfo.avgRowLength }}
          </Tooltip>
        </div>
        <div class="flex items-center">
          <Tooltip :content="$t('source.common.dataSize')">
            <ArrowUpDown :size="18" class="mr-2"/>
            {{ dataInfo.dataLength === 'null' ? 0 : dataInfo.dataLength }}
          </Tooltip>
        </div>
        <div class="flex items-center">
          <Tooltip :content="$t('source.common.indexSize')">
            <Search :size="18" class="mr-2"/>
            {{ dataInfo.indexLength === 'null' ? $t('source.common.notSpecifiedIndex') : dataInfo.indexLength }}
          </Tooltip>
        </div>
        <div class="flex items-center justify-between">
          <div class="flex items-center">
            <Tooltip :content="$t('source.common.autoIncrement')">
              <ArrowUpDown :size="18" class="mr-2"/>
              {{ dataInfo.autoIncrement === 'null' ? $t('source.common.notSpecifiedPrimaryKey') : dataInfo.autoIncrement }}
            </Tooltip>
          </div>
          <div>
            <Popover>
              <PopoverTrigger as-child>
                <Button size="icon" class="rounded-full w-6 h-6" :disabled="dataInfo.autoIncrement === 'null'">
                  <Tooltip :content="$t('source.common.resetAutoIncrement')">
                    <Cog :size="14"/>
                  </Tooltip>
                </Button>
              </PopoverTrigger>
              <PopoverContent class="w-80">
                <div class="grid gap-4">
                  <div class="space-y-2">
                    <h4 class="font-medium leading-none">{{ $t('source.common.resetAutoIncrement') }}</h4>
                  </div>
                  <div class="grid gap-2">
                    <div class="grid grid-cols-3 items-center gap-4">
                      <Label for="autoIncrement">{{ $t('source.common.resetTo') }}</Label>
                      <Input v-model="dataInfo.autoIncrement" id="autoIncrement" type="number" :default-value="dataInfo.autoIncrement"/>
                      <Button size="sm" :loading="submitting" :disabled="submitting" @click="handlerApply">
                        {{ $t('common.apply') }}
                      </Button>
                    </div>
                  </div>
                </div>
              </PopoverContent>
            </Popover>
          </div>
        </div>
      </div>
      <Divider class="mt-4 mb-4"/>
      <div class="space-y-2">
        <Label for="comment">{{ $t('source.common.comment') }}</Label>
        <Textarea id="comment" v-model="dataInfo.comment"/>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent, watch } from 'vue'
import TableService from '@/services/table'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { ArrowUp10, ArrowUpDown, CalendarHeart, Clock, Cog, Database, RemoveFormatting, Search, Table, TableCellsMerge } from 'lucide-vue-next'
import Tooltip from '@/views/ui/tooltip'
import { SqlType, TableFilter, TableModel } from '@/model/table'
import { Separator } from '@/components/ui/separator'
import Divider from '@/views/ui/divider'
import Button from '@/views/ui/button'
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import { ToastUtils } from '@/utils/toast'
import { Textarea } from '@/components/ui/textarea'

export default defineComponent({
  name: 'SourceTableInfo',
  components: {
    Textarea,
    Input,
    Label,
    Button,
    Divider,
    Separator,
    Tooltip,
    CircularLoading,
    Database, Table, Clock, CalendarHeart, ArrowUpDown, TableCellsMerge, RemoveFormatting, ArrowUp10, Search, Cog,
    Popover, PopoverContent, PopoverTrigger
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
      dataInfo: null as TableModel | null
    }
  },
  methods: {
    handlerInitialize()
    {
      const code = this.$route?.params.table as string
      if (code) {
        this.loading = true
        TableService.getByCode(code)
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
    handlerApply()
    {
      if (this.dataInfo) {
        this.submitting = true
        const configure: TableFilter = {
          type: SqlType.ALTER,
          value: this.dataInfo.autoIncrement
        }
        TableService.getData(this.dataInfo.code as string, configure)
                    .then(response => {
                      if (response.status) {
                        ToastUtils.success(this.$t('source.tip.resetAutoIncrementSuccess').replace('$VALUE', this.dataInfo?.autoIncrement as string))
                        this.handlerInitialize()
                      }
                      else {
                        ToastUtils.error(response.message)
                      }
                    })
                    .finally(() => this.submitting = false)
      }
    },
    watchChange()
    {
      watch(
          () => this.$route?.params.table,
          () => this.handlerInitialize()
      )
    }
  }
})
</script>
