<template>
  <a-drawer v-model:open="visible" :title="title" width="40%">
    <a-spin :spinning="loading">
      <a-form v-if="formState" :model="formState" layout="vertical" @finish="onSubmit">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item name="name"
                         :label="$t('common.name')"
                         :rules="[{ required: true, message: $t('common.name') }]">
              <a-input v-model:value="formState.name"/>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item name="icon" :label="$t('menu.common.icon')">
              <a-input v-model:value="formState.icon"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item name="url"
                         :label="$t('common.url')"
                         :rules="[{ required: true, message: $t('common.url') }]">
              <a-input v-model:value="formState.url"/>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item name="group" :label="$t('common.group')">
              <a-input v-model:value="formState.group"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item name="sorted" :label="$t('common.sorted')" :rules="[{ required: true, message: $t('common.sorted') }]">
              <a-input-number v-model:value="formState.sorted" :style="{ width: '100%' }"/>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item name="type"
                         :label="$t('common.type')"
                         :rules="[{ required: true, message: $t('common.type') }]">
              <a-select v-model:value="formState.type" :placeholder="$t('menu.tip.selectType')">
                <a-select-option value="VIEW">VIEW</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item :label="$t('common.parent')">
              <a-select v-model:value="formState.parent" :placeholder="$t('menu.tip.selectParent')" allow-clear>
                <a-select-option v-for="menu in fullMenus" :key="menu.id" :value="menu.id">{{ menu.name }}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item :label="$t('menu.common.redirect')">
              <a-select v-model:value="formState.redirect" :placeholder="$t('menu.tip.selectRedirect')" allow-clear>
                <a-select-option v-for="menu in fullMenus" :key="menu.id" :value="menu.id">{{ menu.name }}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item name="new" :label="$t('menu.common.new')">
              <a-switch v-model:checked="formState.new"/>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item name="i18nKey" :label="$t('menu.common.i18nKey')" :rules="[{ required: true, message: $t('menu.common.i18nKey') }]">
              <a-input v-model:value="formState.i18nKey"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item name="description" :label="$t('common.description')">
          <a-textarea v-model:value="formState.description"/>
        </a-form-item>

        <div class="flex justify-end">
          <a-button type="primary" html-type="submit" :loading="loading" :disabled="loading">
            {{ $t('common.save') }}
          </a-button>
        </div>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { cloneDeep } from 'lodash'
import { MenuModel, MenuRequest } from '@/model/menu'
import MenuService from '@/services/menu'
import { FilterModel } from '@/model/filter'

defineOptions({ name: 'MenuInfo' })

const props = withDefaults(defineProps<{ isVisible?: boolean; info?: MenuModel | null }>(), {
  isVisible: false,
  info: null
})
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const { t } = useI18n()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const title = ref<string | null>(null)
const formState = ref<MenuModel>(null as unknown as MenuModel)
const loading = ref(false)
const fullMenus = ref<MenuModel[]>([])
const saving = ref(false)

const handlerInitialize = () => {
  title.value = `${ t('menu.common.create') }`
  if (props.info) {
    formState.value = cloneDeep(props.info)
    title.value = `${ t('menu.common.modify').replace('$NAME', props.info.name as string) }`
  }
  else {
    formState.value = MenuRequest.of()
  }

  const filter: FilterModel = new FilterModel()
  loading.value = true
  MenuService.getAll(filter)
             .then((response) => {
               if (response.status) {
                 fullMenus.value = response.data.content
               }
             })
             .finally(() => (loading.value = false))
}

const onCancel = () => {
  visible.value = false
}

const onSubmit = () => {
  saving.value = true
  MenuService.saveOrUpdate(formState.value)
             .then(response => {
               if (response.status) {
                 message.success('successful')
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
