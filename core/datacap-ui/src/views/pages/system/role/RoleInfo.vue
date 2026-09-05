<template>
  <a-modal v-model:open="visible" :title="title" :footer="null">
    <div class="relative">
      <a-form v-if="formState" :model="formState" layout="vertical" @finish="onSubmit">
        <a-form-item name="name"
                     :label="$t('role.common.name')"
                     :rules="[
                       { required: true, message: $t('role.tip.name') },
                       { min: 2, message: $t('role.validate.nameSize') },
                       { max: 20, message: $t('role.validate.nameSize') }
                   ]">
          <a-input v-model:value="formState.name" :placeholder="$t('role.tip.name')"/>
        </a-form-item>

        <a-form-item name="description"
                     :label="$t('role.common.description')"
                     :rules="[
                       { required: true, message: $t('role.tip.description') },
                       { min: 3, message: $t('role.validate.descriptionSize') },
                       { max: 50, message: $t('role.validate.descriptionSize') }
                   ]">
          <a-textarea v-model:value="formState.description" :placeholder="$t('role.tip.description')"/>
        </a-form-item>

        <div class="flex justify-end">
          <a-button type="primary" html-type="submit" :loading="loading" :disabled="loading">
            {{ title }}
          </a-button>
        </div>
      </a-form>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { cloneDeep } from 'lodash'
import { RoleModel, RoleRequest } from '@/model/role'
import { StringUtils } from '@/utils/string'
import RoleService from '@/services/role'

defineOptions({ name: 'RoleInfo' })

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
const title = ref<string | null>(null)
const formState = ref<RoleModel>(null as unknown as RoleModel)

const handlerInitialize = () => {
  if (props.info) {
    title.value = `${ StringUtils.replace(t('role.common.edit'), '$NAME', props.info.name as string) }`
    formState.value = cloneDeep(props.info)
  }
  else {
    title.value = t('role.common.create')
    formState.value = RoleRequest.of()
  }
}

const onCancel = () => {
  visible.value = false
}

const onSubmit = () => {
  loading.value = true
  RoleService.saveOrUpdate(formState.value)
             .then(response => {
               if (response.status) {
                 message.success(`${ title.value } ${ t('common.successfully') }`)
                 onCancel()
               }
               else {
                 message.error(response.message)
               }
             })
             .finally(() => (loading.value = false))
}

handlerInitialize()
</script>
