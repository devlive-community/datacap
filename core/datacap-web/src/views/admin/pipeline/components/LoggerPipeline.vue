<template>
  <div>
    <Modal v-model="visible"
           :title="title"
           width="80%"
           :closable="false"
           :maskClosable="false">
      <Scroll>
        <div v-for="(log, index) in logs" :key="index">
          <div v-html="log" style="margin-bottom: 5px; font-size: 16px"></div>
        </div>
      </Scroll>
      <template #footer>
        <Button @click="handlerCancel()">
          {{ $t('common.cancel') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import PipelineService from '@/services/user/PipelineService';
import {AnsiUp} from 'ansi_up';
import {defineComponent} from 'vue';

export default defineComponent({
  name: 'LoggerPipeline',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    info: {
      type: Object,
      default: () => null
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  data()
  {
    return {
      title: null,
      logs: []
    }
  },
  methods: {
    handlerInitialize()
    {
      this.title = `[ ${this.info.name} ] ${this.$t('common.log')}`
      PipelineService.logger(this.info.id)
        .then(response => {
          if (response.status) {
            const ansi_up = new AnsiUp()
            const array = response.data
            for (const i in array) {
              this.logs[i] = ansi_up.ansi_to_html(array[i])
            }
          }
          else {
            this.$Message.error(response.message);
          }
        })
    },
    handlerCancel()
    {
      this.visible = false;
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
});
</script>
