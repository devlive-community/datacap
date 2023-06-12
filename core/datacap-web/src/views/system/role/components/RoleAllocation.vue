<template>
  <div>
    <Modal v-model="visible"
           :title="$t('common.authority')"
           :closable="false"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <CheckboxGroup v-model="selectRoles">
        <Checkbox v-for="role in fullRoles"
                  v-bind:key="role.id"
                  border
                  :label="role.id">
          {{ role.name }}
        </Checkbox>
      </CheckboxGroup>
      <Spin fix
            :show="loading">
      </Spin>
      <template #footer>
        <Button key="cancel"
                size="small"
                type="error"
                :disabled="saving"
                @click="handlerCancel()">
          {{ $t('common.cancel') }}
        </Button>
        <Button type="primary"
                size="small"
                :loading="saving"
                @click="handlerSave()">
          {{ $t('common.save') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import RoleService from "@/services/admin/RoleService";
import {Filter} from "@/model/Filter";
import UserService from "@/services/admin/UserService";
import CommonEntity from '@/entity/CommonEntity';

export default defineComponent({
  name: 'RoleAllocationComponent',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    userId: {
      type: Number,
      required: true,
      default: () => 0
    }
  },
  data()
  {
    return {
      loading: false,
      saving: false,
      fullRoles: [],
      selectRoles: []
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true;
      const filter: Filter = new Filter();
      filter.size = 1000;
      RoleService.getAll(filter)
        .then((response) => {
          if (response.status) {
            this.fullRoles = response.data.content;
            UserService.getById(this.userId)
              .then(userResponse => {
                if (userResponse.status) {
                  this.selectRoles = userResponse.data
                    ?.roles
                    .map(role => role.id)
                }
              })
          }
        })
        .finally(() => {
          this.loading = false
        })
    },
    handlerSave()
    {
      this.saving = true;
      const configure = CommonEntity
        .set('roles', this.selectRoles)
        .set('userId', this.userId)
        .build()['props'];
      UserService.allocationRole(configure)
        .then((response) => {
          if (response.status) {
            this.saving = false;
            this.handlerCancel();
          }
          else {
            this.$Message.error(response.message);
          }
        })
        .finally(() => {
          this.saving = false
        })
    },
    handlerCancel()
    {
      this.$emit('close', false)
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
