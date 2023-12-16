<template>
  <div>
    <Modal v-model="visible"
           :mask-closable="false"
           @cancel="handlerCancel()">
      <template #header>
        {{ `[ ${data.name} ] ${$t('common.configure')}` }}
      </template>
      <div v-for="item in data.configure"
           :key="item"
           style="margin-top: 10px;">
        <Form>
          <FormItem v-if="item.input"
                    :required="item.required"
                    :prop="item.field.replace('.', ' ')"
                    :label-width="120"
                    label-position="top">
            <template #label>
              {{ item.field }}
              <Tooltip v-if="item.description"
                       :content="item.description"
                       max-width="300"
                       transfer>
                <Icon type="md-help-circle"/>
              </Tooltip>
            </template>
            <Row v-if="item.type === 'INPUT'">
              <Input v-model="item.value"
                     type="text"
                     :disabled="item.override && item.tooltip"
                     :style="{width: item.width + 'px'}">
              </Input>
              <Tooltip v-if="item.tooltip"
                       :content="item.tooltip"
                       max-width="300"
                       transfer>
                <Checkbox v-model="item.override"
                          style="margin-left: 5px;">
                </Checkbox>
              </Tooltip>
            </Row>
            <Row v-if="item.type === 'TEXT'">
              <Input v-model="item.value"
                     type="textarea"
                     show-word-limit
                     :rows="3"
                     :disabled="item.override && item.tooltip"
                     :style="{width: item.width + 'px'}">
              </Input>
              <Tooltip v-if="item.tooltip"
                       :content="item.tooltip"
                       max-width="300"
                       transfer>
                <Checkbox v-model="item.override"
                          style="margin-left: 5px;">
                </Checkbox>
              </Tooltip>
            </Row>
            <Row v-if="item.type === 'SWITCH'">
              <i-switch v-model="item.value"
                        :disabled="item.override && item.tooltip"
                        :style="{width: item.width + 'px'}">
              </i-switch>
              <Tooltip v-if="item.tooltip"
                       :content="item.tooltip"
                       max-width="300"
                       transfer>
                <Checkbox v-model="item.override"
                          style="margin-left: 5px;">
                </Checkbox>
              </Tooltip>
            </Row>
            <Row v-if="item.type === 'SELECT'">
              <Select v-model="item.value"
                      :disabled="item.override && item.tooltip"
                      :style="{width: item.width + 'px'}"
                      transfer>
                <Option v-for="option in item.defaultValues"
                        :key="option"
                        :value="option">
                  {{ option }}
                </Option>
              </Select>
              <Tooltip v-if="item.tooltip"
                       :content="item.tooltip"
                       max-width="300"
                       transfer>
                <Checkbox v-model="item.override"
                          style="margin-left: 5px;">
                </Checkbox>
              </Tooltip>
            </Row>
            <Row v-if="item.type === 'NUMBER'">
              <InputNumber v-model="item.value"
                           :disabled="item.override && item.tooltip"
                           :style="{width: item.width + 'px'}"
                           transfer>
              </InputNumber>
              <Tooltip v-if="item.tooltip"
                       :content="item.tooltip"
                       max-width="300"
                       transfer>
                <Checkbox v-model="item.override"
                          style="margin-left: 5px;">
                </Checkbox>
              </Tooltip>
            </Row>
          </FormItem>
        </Form>
      </div>
      <template #footer>
        <Button type="primary"
                @click="handlerSave()">
          <FontAwesomeIcon icon="save"/>
          {{ $t('common.save') }}
        </Button>
      </template>
    </Modal>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import {Configuration} from "@/components/editor/flow/Configuration";

export default defineComponent({
  name: "FlowConfigure",
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    data: {
      type: Configuration
    }
  },
  methods: {
    handlerSave()
    {
      const configure = {
        id: this.data.id,
        protocol: this.data.protocol,
        configures: this.filterConfigure(this.data.configure, this.data.nodeType),
        type: this.data.nodeType
      }
      this.$emit('onChange', configure);
    },
    filterConfigure(meta: any)
    {
      const result = {};
      meta.filter((item: { input: any; }) => item.input)
        .forEach((item: { origin: any; value: any; }) => {
          const key = item.origin;
          const value = item.value;
          if (value !== null) {
            result[key] = value;
          }
        })
      return result
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
