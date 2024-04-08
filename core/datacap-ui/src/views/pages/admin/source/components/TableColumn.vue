<template>
  <Drawer :is-visible="visible" :width="'10%'" @close="handlerCancel">
    <template #title>{{ $t('source.common.visibleColumn') }}</template>
    <div class="mt-3">
      <FormField name="items">
        <FormItem>
          <FormField v-for="item in columns" v-slot="{ value, handleChange }" :key="item.field" type="checkbox" :value="item.field" :unchecked-value="false" name="items">
            <FormItem class="flex flex-row items-start space-x-3 space-y-0">
              <FormControl>
                <Checkbox :checked="value.includes(item.field)" @update:checked="handleChange"/>
              </FormControl>
              <FormLabel class="font-normal">
                {{ item.field }}
              </FormLabel>
            </FormItem>
          </FormField>
        </FormItem>
      </FormField>
    </div>
    <template #footer>
      <div class="space-x-5">
        <Button @click="handlerCancel">
          {{ $t('common.apply') }}
        </Button>
      </div>
    </template>
  </Drawer>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Drawer from '@/views/ui/drawer'
import Button from '@/views/ui/button'
import { FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form'
import { Checkbox } from '@/components/ui/checkbox'
import { useForm } from 'vee-validate'

export default defineComponent({
  name: 'TableColumn',
  components: {
    Button,
    Drawer,
    Checkbox,
    FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage
  },
  props: {
    isVisible: {
      type: Boolean
    },
    columns: {
      type: Array<any>,
      default: () => [] as any[]
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
    const { values } = useForm({
      initialValues: {
        items: props.columns?.map(item => item.field)
      }
    })

    return {
      values
    }
  },
  methods: {
    handlerCancel()
    {
      this.$emit('change', this.values.items)
      this.visible = false
    }
  }
})
</script>
