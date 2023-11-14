<template>
  <div>
    <Modal v-model="visible"
           :title="$t('source.manager.filter')"
           :closable="false"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <CircularLoading v-if="loading"
                       :show="loading"
                       style="margin-top: 150px;">
      </CircularLoading>
      <div v-else>
      </div>
      <template #footer>
        <Button danger
                @click="handlerCancel">
          {{ $t('common.cancel') }}
        </Button>
        <!--        <Button type="primary"-->
        <!--                :loading="loadingChange"-->
        <!--                @click="handlerSave()">-->
        <!--          {{ $t('common.submit') }}-->
        <!--        </Button>-->
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent, watch} from "vue";
import CircularLoading from "@/components/loading/CircularLoading.vue";

export default defineComponent({
  name: "TableFilter",
  components: {CircularLoading},
  props: {
    isVisible: {
      type: Boolean
    }
  },
  created()
  {
    this.handlerInitialize();
    this.watchId();
  },
  data()
  {
    return {
      loading: false
    }
  },
  methods: {
    handlerInitialize()
    {
      console.log(1)
    },
    handlerCancel()
    {
      this.visible = false;
    },
    watchId()
    {
      watch(
        () => this.id,
        () => {
          this.handlerInitialize();
        }
      );
    }
  },
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible;
      },
      set(value: boolean)
      {
        this.$emit('close', value);
      }
    }
  }
});
</script>
