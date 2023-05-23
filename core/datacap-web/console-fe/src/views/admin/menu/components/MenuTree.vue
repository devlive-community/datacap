<template>
  <div>
    <Modal v-model="visible" :title="$t('common.menu')" :closable="false" :mask-closable="false" @cancel="handlerCancel()">
      {{ fullMenus }}
    </Modal>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import {Filter} from "@/model/Filter";
import MenuService from '@/services/admin/MenuService';

export default defineComponent({
  name: 'MenuTreeComponent',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    }
  },
  data()
  {
    return {
      loading: false,
      fullMenus: []
    }
  },
  created()
  {
    const filter: Filter = new Filter();
    filter.size = 1000;
    this.handlerInitialize(filter);
  },
  methods: {
    handlerInitialize(filter: Filter)
    {
      this.loading = true;
      MenuService.getAll(filter)
        .then((response) => {
          if (response.status) {
            this.fullMenus = response.data.content;
          }
        })
        .finally(() => {
          this.loading = false
        })
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
})
</script>
