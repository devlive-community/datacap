<template>
  <Dialog :open="isVisible" persistent @update:open="handlerCancel">
    <DialogContent>
      <form @submit.prevent="onSubmit">
        <DialogHeader class="border-b">
          <DialogTitle class="pb-3.5">{{ title }}</DialogTitle>
          <DialogDescription></DialogDescription>
        </DialogHeader>
        <CardContent class="grid gap-4 mt-5">
          <div class="grid gap-2">
            <FormField v-slot="{ componentField }" name="name">
              <FormItem>
                <FormLabel>{{ $t('role.common.name') }}</FormLabel>
                <FormControl>
                  <Input v-model="formState.name" :default-value="formState.name" v-bind="componentField" :placeholder="$t('role.tip.name')"/>
                </FormControl>
                <FormMessage/>
              </FormItem>
            </FormField>
          </div>
          <div class="grid gap-2">
            <FormField v-slot="{ componentField }" name="description">
              <FormItem>
                <FormLabel>{{ $t('role.common.description') }}</FormLabel>
                <FormControl>
                  <Textarea v-model="formState.description" :default-value="formState.description" v-bind="componentField" :placeholder="$t('role.tip.description')"/>
                </FormControl>
                <FormMessage/>
              </FormItem>
            </FormField>
          </div>
        </CardContent>
        <DialogFooter class="border-t pt-3.5">
          <Button type="submit" :disabled="loading">
            <Loader2 v-if="loading" class="w-4 h-4 mr-2 animate-spin"/>
            {{ title }}
          </Button>
        </DialogFooter>
      </form>
    </DialogContent>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent, inject, ref } from 'vue'
import { Button } from '@/components/ui/button'
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog'
import { RoleModel, RoleRequest } from '@/model/role'
import { StringUtils } from '@/utils/string'
import { CardContent } from '@/components/ui/card'
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Loader2 } from 'lucide-vue-next'
import * as z from 'zod'
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'
import { cloneDeep } from 'lodash'
import { Textarea } from '@/components/ui/textarea'
import { ToastUtils } from '@/utils/toast'
import RoleService from '@/services/role'

export default defineComponent({
  name: 'RoleInfo',
  components: {
    FormField, FormControl, FormMessage, FormLabel, FormItem,
    DialogFooter, DialogDescription, DialogTitle, DialogHeader, DialogContent, DialogTrigger, Dialog,
    Loader2,
    CardContent,
    Button, Textarea, Input
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
  setup(props)
  {
    let info = RoleRequest.of()
    if (props.info) {
      info = cloneDeep(props.info) as RoleModel
    }
    const formState = ref<RoleModel>(info)
    const $t: any = inject('$t')
    const validator = z
        .object({
          name: z.string({required_error: $t('role.tip.name')})
              .min(2, $t('role.validate.nameSize'))
              .max(20, $t('role.validate.nameSize')),
          description: z.string({required_error: $t('role.tip.description')})
              .min(3, $t('role.validate.descriptionSize'))
              .max(50, $t('role.validate.descriptionSize'))
        })

    const {values, validate} = useForm({
      validationSchema: toTypedSchema(validator)
    })

    return {
      formState,
      validate,
      values
    }
  },
  data()
  {
    return {
      loading: false,
      title: null as string | null
    }
  },
  created()
  {
    this.title = this.$t('role.common.create')
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      if (this.info) {
        this.title = `${StringUtils.replace(this.$t('role.common.edit'), '$NAME', this.info.name)}`
      }
    },
    handlerCancel()
    {
      this.visible = false
    },
    async onSubmit()
    {
      const valid = await this.validate()
      if (!valid.valid) {
        return
      }
      this.loading = true
      const configure = {...this.values, id: this.formState.id}
      RoleService.saveOrUpdate(configure)
          .then(response => {
            if (response.status) {
              ToastUtils.success(`${this.title} ${this.$t('common.successfully')}`)
              this.handlerCancel()
            }
            else {
              ToastUtils.error(response.message)
            }
          })
          .finally(() => this.loading = false)
    }
  }
})
</script>
