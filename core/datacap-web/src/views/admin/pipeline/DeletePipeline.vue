<template>
  <div>
    <Modal v-model="visible"
           :title="title"
           :closable="false"
           :maskClosable="false"
           :z-index="9"
           :styles="{}">
      <List>
        <ListItem>{{ $t('pipeline.delete.deleteTip1') }}</ListItem>
        <ListItem>{{ $t('pipeline.delete.deleteTip2') }}</ListItem>
        <ListItem>{{ $t('pipeline.delete.deleteTip3') }}</ListItem>
        <ListItem>
          <Input v-model="confirmValue">
          </Input>
        </ListItem>
      </List>
      <template #footer>
        <Button @click="handlerCancel()">
          {{ $t('common.cancel') }}
        </Button>
        <Button type="error"
                :disabled="info?.name !== confirmValue"
                @click="handlerAction()">
          {{ $t('common.delete') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import PipelineService from '@/services/user/PipelineService';
import {defineComponent} from 'vue';

export default defineComponent({
  name: 'DeletePipeline',
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
      confirmValue: null
    }
  },
  methods: {
    handlerInitialize()
    {
      this.title = `${this.$t('common.delete')} [ ${this.info.name} ]`
    },
    handlerAction()
    {
      PipelineService.deleteById(this.info.id)
        .then(response => {
          if (response.status) {
            this.$Message.success(this.title + ' ' + this.$t('common.success'));
            this.handlerCancel();
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
