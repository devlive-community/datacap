<template>
  <footer class="font-sans bg-[#f8f8f9] py-8 px-10 mt-5">
    <div :class="`grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-${data.length} gap-8`">
      <div v-for="item in data" :key="item.title">
        <h4 class="text-[#808695] font-bold text-lg mb-5">{{ item.title }}</h4>
        <ul class="space-y-4">
          <li v-for="children in item.children" :key="children.title">
            <DcLink :external="children.external" :link="children.link" :target="children.blank"
                    class="hover:text-[#5cadff] text-[#808695] text-[15px] font-semibold transition-all">
              {{ children.title }}
            </DcLink>
          </li>
        </ul>
      </div>
    </div>
    <div class="border-t text-center border-[#dcdee2] pt-8 mt-8 space-y-2">
      <p class="text-gray-300 text-[15px] font-semibold">
        Copyright © 2022 - {{ new Date().getFullYear() }} Devlive Community All Rights Reserved
      </p>
      <p>{{ $t('common.version') }}:
        <Text type="danger"
              strong>
          {{ version }}
        </Text>
      </p>
    </div>
  </footer>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FooterModel } from '@/views/layouts/common/components/model/footer.ts'
import DcLink from '@/views/components/link/DcLink.vue'
import { PackageUtils } from '@/utils/package.ts'

export default defineComponent({
  name: 'LayoutFooter',
  components: { DcLink },
  props: {
    data: {
      type: Array as () => Array<FooterModel>,
      default: () => new Array<FooterModel>()
    }
  },
  setup()
  {
    const version = PackageUtils.get('version')
    return { version }
  }
})
</script>
