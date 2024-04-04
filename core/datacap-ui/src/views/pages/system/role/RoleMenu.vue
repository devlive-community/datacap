<template>
  <Dialog :open="isVisible" persistent @update:open="handlerCancel">
    <DialogContent class="p-0">
      <DialogHeader class="border-b p-3">
        <DialogTitle>{{ title }}</DialogTitle>
        <DialogDescription></DialogDescription>
      </DialogHeader>
      <CircularLoading v-if="loading" :show="loading"/>
      <CardContent v-else class="grid gap-4 mt-5 max-h-80 overflow-auto">
        <Tree show-checkbox :data="fullMenus" @on-check-change="handlerOnCheckedNode"/>
      </CardContent>
      <DialogFooter class="border-t pt-3 pb-3 pr-3">
        <Button :disabled="saving" @click="handlerSave">
          <Loader2 v-if="saving" class="w-4 h-4 mr-2 animate-spin"/>
          {{ title }}
        </Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Loader2 } from 'lucide-vue-next'
import { CardContent } from '@/components/ui/card'
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog'
import { RoleModel } from '@/model/role'
import { Button } from '@/components/ui/button'
import { Tree } from 'view-ui-plus'
import '@/views/components/tree/style.css'
import RoleService from '@/services/role'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import { ToastUtils } from '@/utils/toast'
import UserService from '@/services/user'
import CommonUtils from '@/utils/common'

export default defineComponent({
  name: 'RoleMenu',
  components: {
    CircularLoading,
    Button,
    DialogFooter, DialogDescription, DialogTitle, DialogHeader, DialogContent, DialogTrigger, Dialog,
    CardContent,
    Loader2,
    Tree
  },
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    info: {
      type: Object as () => RoleModel | null,
      default: null
    }
  },
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible
      },
      set(value: boolean)
      {
        this.$emit('close', value)
      }
    }
  },
  data()
  {
    return {
      loading: false,
      saving: false,
      title: null as string | null,
      fullMenus: [],
      selectNodes: [] as any[]
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      if (this.info) {
        this.title = this.$t('role.common.assignMenu').replace('$NAME', this.info.name as string)
        RoleService.getAllMenuById(this.info?.id as number)
            .then((response) => {
              if (response.status) {
                this.fullMenus = response.data
                const selectedItems = this.fullMenus.filter((item: { selected: boolean }) => item.selected)
                this.handlerOnCheckedNode(selectedItems)
              }
            })
            .finally(() => this.loading = false)
      }
    },
    handlerOnCheckedNode(nodes: any)
    {
      this.selectNodes = nodes
    },
    handlerSave()
    {
      this.saving = true
      RoleService.saveMenu(this.info?.id as number, this.selectNodes)
          .then((response) => {
            if (response.status) {
              UserService.getMenus()
                  .then(response => {
                    if (response.status) {
                      localStorage.setItem(CommonUtils.menu, JSON.stringify(response.data))
                    }
                  })
              this.handlerCancel()
            }
            else {
              ToastUtils.error(response.message)
            }
          })
          .finally(() => this.saving = false)
    },
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>