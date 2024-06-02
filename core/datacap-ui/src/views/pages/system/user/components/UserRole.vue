<template>
  <Dialog :open="isVisible" @update:open="handlerCancel">
    <DialogContent>
      <form @submit="onSubmit">
        <DialogHeader class="border-b">
          <DialogTitle class="pb-3.5">{{ title }}</DialogTitle>
          <DialogDescription></DialogDescription>
        </DialogHeader>
        <CardContent class="grid gap-4 mt-5 justify-center">
          <Loader2 v-if="loading" class="w-4 h-4 mr-2 animate-spin"/>
          <div v-else>
            <FormField name="items">
              <FormItem>
                <FormField v-for="item in data" v-slot="{ value, handleChange }" :key="item.id" type="checkbox" :value="item.id" :unchecked-value="false" name="items">
                  <FormItem class="flex flex-row items-start space-x-3 space-y-0">
                    <FormControl>
                      <Checkbox :checked="value.includes(item.id)" @update:checked="handleChange"/>
                    </FormControl>
                    <FormLabel class="font-normal">
                      {{ item.name }}
                    </FormLabel>
                  </FormItem>
                </FormField>
              </FormItem>
            </FormField>
          </div>
        </CardContent>
        <DialogFooter class="border-t pt-3.5">
          <Button type="submit" :disabled="submitted">
            <Loader2 v-if="submitted" class="w-4 h-4 mr-2 animate-spin"/>
            {{ title }}
          </Button>
        </DialogFooter>
      </form>
    </DialogContent>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Loader2 } from 'lucide-vue-next'
import { CardContent } from '@/components/ui/card'
import { StringUtils } from '@/utils/string'
import { UserModel, UserRoleModel } from '@/model/user'
import { Button } from '@/components/ui/button'
import { FilterModel } from '@/model/filter'
import RoleService from '@/services/role'
import { Checkbox } from '@/components/ui/checkbox'
import { FormControl, FormField, FormItem, FormLabel } from '@/components/ui/form'
import { useForm } from 'vee-validate'
import { RoleModel } from '@/model/role'
import { ToastUtils } from '@/utils/toast'
import UserService from '@/services/user'

export default defineComponent({
  name: 'UserRole',
  components: {
    Checkbox, Button,
    CardContent,
    DialogDescription, DialogTitle, Dialog, DialogFooter, DialogContent, DialogHeader,
    Loader2,
    FormField, FormItem, FormLabel, FormControl
  },
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    info: {
      type: Object as () => UserModel | null,
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
    const { validate, values } = useForm({
      initialValues: {
        items: props.info?.roles?.map(role => role.id)
      }
    })

    return {
      validate,
      values
    }
  },
  data()
  {
    return {
      loading: false,
      submitted: false,
      title: null as string | null,
      data: [] as RoleModel[]
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      if (this.info) {
        this.title = `${ StringUtils.replace(this.$t('role.common.assignRole'), '$NAME', this.info.username as string) }`
      }
      this.loading = true
      const filter: FilterModel = new FilterModel()
      filter.size = 1000
      RoleService.getAll(filter)
                 .then((response) => {
                   if (response.status) {
                     this.data = response.data.content
                   }
                 })
                 .finally(() => this.loading = false)
    },
    handlerCancel()
    {
      this.visible = false
    },
    async onSubmit()
    {
      const valid = await this.validate()
      if (valid.valid) {
        if (this.info) {
          this.submitted = true
          const configure: UserRoleModel = {
            roles: this.values.items as number[],
            userId: this.info.id as number
          }
          UserService.assignRole(configure)
                     .then((response) => {
                       if (response.status) {
                         ToastUtils.success(this.$t('user.tip.assignRoleSuccess'))
                         this.handlerCancel()
                       }
                       else {
                         ToastUtils.error(response.message)
                       }
                     })
                     .finally(() => this.submitted = false)
        }
        else {
          ToastUtils.error(this.$t('common.invalidParam'))
        }
      }
    }
  }
})
</script>
