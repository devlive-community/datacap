<template>
  <Modal v-model="visible"
         :title="$t('common.create') + $t('common.chat')"
         width="30%"
         :closable="false"
         :mask-closable="false"
         @cancel="handlerCancel()">
    <Form :model="fromState">
      <FormItem :label="$t('common.name')" prop="name">
        <Input v-model="fromState['name']"/>
      </FormItem>
      <FormItem :label="$t('common.avatar')" prop="avatar">
        <Input v-model="fromState['avatar']"/>
      </FormItem>
      <FormItem :label="$t('common.description')" prop="description">
        <Input v-model="fromState['description']"
               type="textarea"
               :rows="2">
        </Input>
      </FormItem>
    </Form>
    <template #footer>
      <Button key="cancel" type="error" :disabled="saving" size="small" @click="handlerCancel()">
        {{ $t('common.cancel') }}
      </Button>
      <Button type="primary" size="small" :loading="saving" @click="handlerSave()">
        {{ $t('common.save') }}
      </Button>
    </template>
  </Modal>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import defaultEntity from "@/entity/CommonEntity";
import RoomService from "@/services/ChatService";

export default defineComponent({
  name: "CreateChat",
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    userId: {
      type: Number
    }
  },
  data()
  {
    return {
      saving: false,
      fromState: null
    }
  },
  created()
  {
    this.fromState = defaultEntity.set('name', null)
      .set('avatar', null)
      .set('description', null)
      .set('user', null)
      .build()
      .get()
  },
  methods: {
    handlerSave()
    {
      this.saving = true;
      this.fromState['user'] = {
        id: this.userId
      }
      RoomService.saveOrUpdate(this.fromState)
        .then(response => {
          if (response.status) {
            this.$Message.success("successful");
            this.handlerCancel();
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => {
          this.saving = false;
        });
    },
    handlerCancel()
    {
      this.fromState = null;
      this.visible = false;
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
