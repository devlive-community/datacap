<template>
  <div>
    <div class="hidden flex-col md:flex">
      <LayoutHeader></LayoutHeader>
      <LayoutBreadcrumb></LayoutBreadcrumb>
      <div class="flex-1 space-y-4 pl-8 pr-8">
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
import LayoutBreadcrumb from '@/views/layouts/common/components/LayoutBreadcrumb.vue';

export default defineComponent({
  name: 'LayoutContainer',
  components: {LayoutBreadcrumb, AvatarFallback, AvatarImage, Avatar, Button, LayoutHeader}
});
</script>
