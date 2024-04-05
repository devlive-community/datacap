<template>
  <Drawer :is-visible="visible" :width="'40%'" @close="handlerCancel">
    <template #title>{{ title }}</template>
    <CircularLoading v-if="loading" :show="loading"/>
    <div v-else class="flex w-full flex-col">
      <div class="flex flex-1 flex-col gap-4 p-1">
        <div class="grid items-center gap-3 md:grid-cols-2 md:gap-4 lg:grid-cols-2">
          <FormField v-slot="{ componentField }" name="name">
            <FormItem>
              <FormLabel>{{ $t('common.name') }}</FormLabel>
              <FormControl>
                <Input v-model="formState.name" :default-value="formState.name" v-bind="componentField"/>
              </FormControl>
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="code">
            <FormItem>
              <FormLabel>{{ $t('common.code') }}</FormLabel>
              <FormControl>
                <Input v-model="formState.code" :default-value="formState.code" v-bind="componentField"/>
              </FormControl>
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="url">
            <FormItem>
              <FormLabel>{{ $t('common.url') }}</FormLabel>
              <FormControl>
                <Input v-model="formState.url" :default-value="formState.url" v-bind="componentField"/>
              </FormControl>
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="group">
            <FormItem>
              <FormLabel>{{ $t('common.group') }}</FormLabel>
              <FormControl>
                <Input v-model="formState.group" :default-value="formState.group" v-bind="componentField"/>
              </FormControl>
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="sorted">
            <FormItem>
              <FormLabel>{{ $t('common.sorted') }}</FormLabel>
              <FormControl>
                <Input v-model="formState.sorted" :default-value="formState.sorted" type="number" v-bind="componentField"/>
              </FormControl>
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="type">
            <FormItem>
              <FormLabel>{{ $t('common.type') }}</FormLabel>
              <FormControl>
                <Select v-model="formState.type" :default-value="formState.type" v-bind="componentField">
                  <SelectTrigger class="w-full">
                    <SelectValue :placeholder="$t('menu.tip.selectType')"/>
                  </SelectTrigger>
                  <SelectContent>
                    <SelectGroup>
                      <SelectItem value="VIEW">VIEW</SelectItem>
                    </SelectGroup>
                  </SelectContent>
                </Select>
              </FormControl>
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="parent">
            <FormItem>
              <FormLabel>{{ $t('menu.common.parent') }}</FormLabel>
              <FormControl>
                <Select v-model="formState.parent" :default-value="formState.parent" v-bind="componentField">
                  <SelectTrigger class="w-full">
                    <SelectValue class="cursor-pointer" :placeholder="$t('menu.tip.selectParent')"/>
                  </SelectTrigger>
                  <SelectContent>
                    <SelectGroup>
                      <SelectItem v-for="menu in fullMenus" :value="menu.id">{{ menu.name }}</SelectItem>
                    </SelectGroup>
                  </SelectContent>
                </Select>
              </FormControl>
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="redirect">
            <FormItem>
              <FormLabel>{{ $t('menu.common.redirect') }}</FormLabel>
              <FormControl>
                <Select v-model="formState.redirect" :default-value="formState.redirect" v-bind="componentField">
                  <SelectTrigger class="w-full">
                    <SelectValue class="cursor-pointer" :placeholder="$t('menu.tip.selectRedirect')"/>
                  </SelectTrigger>
                  <SelectContent>
                    <SelectGroup>
                      <SelectItem v-for="menu in fullMenus" :value="menu.id">{{ menu.name }}</SelectItem>
                    </SelectGroup>
                  </SelectContent>
                </Select>
              </FormControl>
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="new">
            <FormItem>
              <FormLabel>{{ $t('menu.common.new') }}</FormLabel>
              <FormControl>
                <br/>
                <Switch v-model="formState.new" :default-checked="formState.new" v-bind="componentField"/>
              </FormControl>
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="i18nKey">
            <FormItem>
              <FormLabel>{{ $t('menu.common.i18nKey') }}</FormLabel>
              <FormControl>
                <Input v-model="formState.i18nKey" :default-value="formState.i18nKey" v-bind="componentField"/>
              </FormControl>
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="icon">
            <FormItem>
              <FormLabel>{{ $t('menu.common.icon') }}</FormLabel>
              <FormControl>
                <Input v-model="formState.icon" :default-value="formState.icon" v-bind="componentField"/>
              </FormControl>
            </FormItem>
          </FormField>
        </div>
        <FormField v-slot="{ componentField }" name="description">
          <FormItem>
            <FormLabel>{{ $t('common.description') }}</FormLabel>
            <FormControl>
              <Textarea v-model="formState.description" :default-value="formState.description" v-bind="componentField"/>
            </FormControl>
          </FormItem>
        </FormField>
      </div>
    </div>
    <template #footer>
      <Button :disabled="saving" variant="destructive" @click="handlerCancel()">{{ $t('common.cancel') }}</Button>
      <Button :disabled="saving" @click="handlerSave()">
        <Loader2 v-if="saving" class="w-full justify-center animate-spin"/>
        {{ $t('common.save') }}
      </Button>
    </template>
  </Drawer>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { MenuModel, MenuRequest } from '@/model/menu'
import Drawer from '@/views/ui/drawer'
import { cloneDeep } from 'lodash'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from '@/components/ui/select'
import CircularLoading from '@/views/components/loading/CircularLoading.vue'
import MenuService from '@/services/menu'
import { FilterModel } from '@/model/filter'
import { Switch } from '@/components/ui/switch'
import { Textarea } from '@/components/ui/textarea'
import { Button } from '@/components/ui/button'
import { Loader2 } from 'lucide-vue-next'
import { ToastUtils } from '@/utils/toast'

export default defineComponent({
  name: 'MenuInfo',
  components: {
    Button,
    Textarea,
    Switch,
    CircularLoading,
    Input,
    Label,
    Drawer,
    FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage,
    Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue,
    Loader2
  },
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    info: {
      type: Object as () => MenuModel | null,
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
      title: null as string | null,
      formState: null as unknown as MenuModel,
      loading: false,
      fullMenus: [] as MenuModel[],
      saving: false
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.title = `${this.$t('menu.common.create')}`
      if (this.info) {
        this.formState = cloneDeep(this.info)
        this.title = `${this.$t('menu.common.modify').replace('$NAME', this.info.name as string)}`
      }
      else {
        this.formState = MenuRequest.of()
      }

      const filter: FilterModel = new FilterModel()
      this.loading = true
      MenuService.getAll(filter)
          .then((response) => {
            if (response.status) {
              this.fullMenus = response.data.content
            }
          })
          .finally(() => this.loading = false)
    },
    handlerSave()
    {
      this.saving = true
      MenuService.saveOrUpdate(this.formState)
          .then(response => {
            if (response.status) {
              ToastUtils.success('successful');
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
