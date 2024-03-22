<template>
  <div>
    <h3 class="text-lg font-medium">{{ $t('user.common.modifyUsername') }}</h3>
    <p class="text-sm text-muted-foreground">{{ $t('user.tip.modifyUsername') }}</p>
  </div>
  <Separator class="my-4"/>
  <UsernameForm/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import UsernameForm from '@/views/pages/admin/user/security/components/UsernameForm.vue'
import { Separator } from '@/components/ui/separator'

export default defineComponent({
  name: 'UsernameHome',
  components: {
    Separator,
    UsernameForm
  }
});
</script>
