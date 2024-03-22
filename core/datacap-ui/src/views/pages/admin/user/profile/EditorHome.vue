<template>
  <div class="w-full">
    <div>
      <h3 class="text-lg font-medium">{{ $t('user.common.editor') }}</h3>
      <p class="text-sm text-muted-foreground">{{ $t('user.tip.editor') }}</p>
    </div>
    <Separator class="my-4"/>
    <EditorForm/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Separator } from '@/components/ui/separator'
import EditorForm from '@/views/pages/admin/user/profile/components/EditorForm.vue'

export default defineComponent({
  name: 'EditorHome',
  components: {
    EditorForm,
    Separator
  }
});
</script>
