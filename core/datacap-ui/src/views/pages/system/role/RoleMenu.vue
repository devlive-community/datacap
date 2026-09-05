<template>
  <a-modal v-model:open="visible" :title="title" :footer="null">
    <div class="relative">
      <a-spin :spinning="loading">
        <a-tree v-model:checkedKeys="menus"
                checkable
                :tree-data="data"
                :field-names="{ title: 'label', key: 'value', children: 'children' }"/>

        <div class="flex justify-end">
          <a-button type="primary" :loading="saving" :disabled="saving" @click="onSubmit()">
            {{ title }}
          </a-button>
        </div>
      </a-spin>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { RoleModel } from '@/model/role'
import UserService from '@/services/user'
import RoleService from '@/services/role'
import CommonUtils from '@/utils/common'

defineOptions({ name: 'RoleMenu' })

const props = withDefaults(defineProps<{ isVisible?: boolean; info?: RoleModel | null }>(), {
  isVisible: false,
  info: null
})
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const { t } = useI18n()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const loading = ref(false)
const saving = ref(false)
const title = ref<string | null>(null)
const data = ref<any[]>([])
const menus = ref<any[]>([])

const handleData = (source: any[]): any[] => {
  return source.map(item => {
    const node: { value: any; label: any; children: any[] } = {
      value: item.id,
      label: item.title,
      children: []
    }
    if (item.children) {
      node.children = handleData(item.children)
    }
    return node
  })
}

const extractSelectedItems = (source: any): any[] => {
  let selectedItems = [] as any[]
  source.forEach((item: { selected: boolean; children: any[] }) => {
    if (item.selected) {
      selectedItems.push(item)
    }
    if (item.children) {
      selectedItems = selectedItems.concat(extractSelectedItems(item.children))
    }
  })
  return selectedItems
}

const handlerInitialize = () => {
  loading.value = true
  if (props.info) {
    title.value = t('role.common.assignMenu').replace('$NAME', props.info.name as string)
    RoleService.getAllMenuById(props.info?.id as number)
               .then((response) => {
                 if (response.status) {
                   const source = response.data
                   data.value = handleData(source)
                   menus.value = extractSelectedItems(source).map(item => item.id)
                 }
               })
               .finally(() => (loading.value = false))
  }
}

const onCancel = () => {
  visible.value = false
}

const onSubmit = () => {
  saving.value = true
  RoleService.saveMenu(props.info?.id as number, menus.value)
             .then((response) => {
               if (response.status) {
                 UserService.getMenus()
                            .then(res => {
                              if (res.status) {
                                localStorage.setItem(CommonUtils.menu, JSON.stringify(res.data))
                              }
                            })
                 onCancel()
               }
               else {
                 message.error(response.message)
               }
             })
             .finally(() => (saving.value = false))
}

handlerInitialize()
</script>
