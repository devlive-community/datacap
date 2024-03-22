<template>
  <div class="w-full">
    <div>
      <h3 class="text-lg font-medium">{{ $t('user.common.profile') }}</h3>
      <p class="text-sm text-muted-foreground">{{ $t('user.tip.profile') }}</p>
    </div>
    <Separator class="my-4"/>
    <ProfileForm/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Separator } from '@/components/ui/separator'
import ProfileForm from '@/views/pages/admin/user/profile/components/ProfileForm.vue'

export default defineComponent({
  name: 'ProfileHome',
  components: {
    ProfileForm,
    Separator
  }
});
</script>
