<template>
  <DcDrawer :is-visible="visible" :title="title as string" width="35%">
    <UserForm class="mt-3" @close="handlerCancel"/>
  </DcDrawer>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { UserModel } from '@/model/user.ts'
import DcDrawer from '@/views/ui/drawer/drawer.vue'
import UserForm from '@/views/pages/system/user/components/UserForm.vue'

export default defineComponent({
  name: 'UserInfo',
  components: {
    UserForm,
    DcDrawer
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
  props: {
    isVisible: {
      type: Boolean
    },
    info: {
      type: Object as () => UserModel | null
    }
  },
  data()
  {
    return {
      title: null as string | null
    }
  },
  created()
  {
    if (this.info) {
      this.title = this.$t('user.common.edit').replace('$VALUE', this.info.username as string)
    }
    else {
      this.title = this.$t('user.common.create')
    }
  },
  methods: {
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>
