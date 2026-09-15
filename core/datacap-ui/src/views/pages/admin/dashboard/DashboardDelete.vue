<template>
  <a-modal v-model:open="visible"
           :title="$t('dashboard.common.delete') + ' [ ' + data?.name + ' ]'"
           :footer="null"
           @cancel="onCancel">
    <a-space direction="vertical" :style="{ width: '100%' }">
      <a-alert type="error" :message="$t('dashboard.tip.deleteTip1')"/>
      <a-alert type="error" :message="$t('dashboard.tip.deleteTip2')"/>
      <a-alert type="error" :message="$t('dashboard.tip.deleteTip3').replace('$NAME', data?.name as string)"/>
    </a-space>

    <a-form :model="formState" class="mt-3" @finish="onSubmit">
      <a-form-item name="name"
                   :rules="[
                         { required: true, message: $t('dashboard.validator.name.required') },
                         { validator: validateMatch }
                   ]">
        <a-input v-model:value="formState.name" :placeholder="$t('dashboard.placeholder.name')"/>
      </a-form-item>

      <div class="flex justify-end">
        <a-space>
          <a-button @click="onCancel">
            {{ $t('common.cancel') }}
          </a-button>

          <a-button type="primary" danger html-type="submit" :loading="loading">
            {{ $t('dashboard.common.delete') }}
          </a-button>
        </a-space>
      </div>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import DashboardService from '@/services/dashboard'
import { DashboardModel } from '@/model/dashboard'

defineOptions({ name: 'DashboardDelete' })

const props = withDefaults(defineProps<{ isVisible?: boolean; data?: DashboardModel | null }>(), {
  isVisible: false,
  data: null
})
const emit = defineEmits<{ (e: 'close', value: boolean): void }>()

const { t } = useI18n()

const visible = computed({
  get: () => props.isVisible,
  set: (value: boolean) => emit('close', value)
})

const loading = ref(false)
const formState = ref({ name: '' })

const validateMatch = (_rule: any, value: string) => {
  if (value !== String(props.data?.name)) {
    return Promise.reject(new Error(t('dashboard.validator.name.match').replace('$VALUE', String(props.data?.name))))
  }
  return Promise.resolve(true)
}

const onCancel = () => {
  visible.value = false
}

const onSubmit = () => {
  if (props.data) {
    loading.value = true
    DashboardService.deleteByCode(props.data.code)
                    .then((response) => {
                      if (response.status) {
                        message.success(`${ t('dashboard.common.delete') } [ ${ props.data?.name } ] ${ t('common.successfully') }`)
                        onCancel()
                      }
                      else {
                        message.error(response.message)
                      }
                    })
                    .finally(() => (loading.value = false))
  }
}
</script>
