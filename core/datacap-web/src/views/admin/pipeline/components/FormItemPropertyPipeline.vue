<template>
  <div>
    <div v-if="pipelineFromItems">
      <Divider orientation="left">{{ $t('common.configure') }}</Divider>
      <div v-for="item in pipelineFromItems"
           :key="item"
           style="margin-top: 10px;">
        <FormItem v-if="item.input"
                  :required="item.required"
                  :prop="item.field.replace('.', ' ')"
                  :label-width="200"
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
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import {defineComponent} from 'vue';

export default defineComponent({
  name: 'FormItemPropertyPipeline',
  props: {
    pipelineFromItems: {
      type: Array,
      default: () => []
    }
  }
});
</script>
