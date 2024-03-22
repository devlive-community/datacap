<template>
  <div>
    <h3 class="text-lg font-medium">{{ $t('user.common.modifyPassword') }}</h3>
    <p class="text-sm text-muted-foreground">{{ $t('user.tip.modifyPassword') }}</p>
  </div>
  <Separator class="my-4"/>
  <PasswordForm/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Separator } from '@/components/ui/separator'
import PasswordForm from '@/views/pages/admin/user/security/components/PasswordForm.vue'

export default defineComponent({
  name: 'PasswordHome',
  components: {
    PasswordForm,
    Separator
  }
});
</script>