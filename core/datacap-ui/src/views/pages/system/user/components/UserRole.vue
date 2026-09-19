<template>
  <a-modal v-model:open="visible" :title="title" :footer="null">
    <div class="relative">
      <a-spin :spinning="loading">
        <a-form v-if="formState" :model="formState" @finish="onSubmit" @finishFailed="onError">
          <a-form-item name="roles"
                       :rules="[
                         { required: true, type: 'array', message: 'Please check the role' }
                     ]">
            <a-checkbox-group v-model:value="formState.roles">
              <a-checkbox v-for="item in data" :key="item.code" :value="item.code">{{ item.name }}</a-checkbox>
            </a-checkbox-group>
          </a-form-item>

          <div class="flex justify-end">
            <a-button type="primary" html-type="submit" :loading="submitted" :disabled="submitted">
              {{ title }}
            </a-button>
          </div>
        </a-form>
      </a-spin>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { StringUtils } from '@/utils/string'
import { UserModel, UserRoleModel } from '@/model/user'
import { FilterModel } from '@/model/filter'
import RoleService from '@/services/role'
import UserService from '@/services/user'
import { RoleModel } from '@/model/role'

defineOptions({ name: 'UserRole' })

const props = withDefaults(defineProps<{ isVisible?: boolean; info?: UserModel | null }>(), {
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
const submitted = ref(false)
const title = ref<string | null>(null)
const data = ref<RoleModel[]>([])
const formState = ref<UserRoleModel | null>(null)

const handlerInitialize = () => {
  if (props.info) {
    title.value = `${ StringUtils.replace(t('role.common.assignRole'), '$NAME', props.info.username as string) }`
    formState.value = {
      roles: props.info.roles?.map(v => v.code) as string[],
      userId: props.info.id as number
    }
    loading.value = true
    const filter: FilterModel = new FilterModel()
    filter.size = 1000
    RoleService.getAll(filter)
               .then((response) => {
                 if (response.status) {
                   data.value = response.data.content
                 }
               })
               .finally(() => (loading.value = false))
  }
}

const onClose = () => {
  visible.value = false
}

const onSubmit = () => {
  if (props.info) {
    submitted.value = true
    UserService.assignRole(formState.value as UserRoleModel)
               .then((response) => {
                 if (response.status) {
                   message.success(t('user.tip.assignRoleSuccess'))
                   onClose()
                 }
                 else {
                   message.error(response.message)
                 }
               })
               .finally(() => (submitted.value = false))
  }
  else {
    message.error(t('common.invalidParam'))
  }
}

const onError = (error: any) => {
  const names = (error?.errorFields || []).map((field: any) => (Array.isArray(field.name) ? field.name.join('.') : field.name))
  message.error(`Validation error field: [ ${ names.join(', ') } ]`)
}

handlerInitialize()
</script>
