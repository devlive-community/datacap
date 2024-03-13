<template>
  <div>
    <div class="hidden flex-col md:flex">
      <LayoutHeader></LayoutHeader>


      <div class="flex-1 space-y-4 p-8 pt-6">
        <RouterView></RouterView>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import LayoutHeader from '@/views/layouts/common/components/LayoutHeader.vue';
import { Button } from '@/components/ui/button';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';

export default defineComponent({
  name: 'LayoutContainer',
  components: {AvatarFallback, AvatarImage, Avatar, Button, LayoutHeader}
});
</script>
