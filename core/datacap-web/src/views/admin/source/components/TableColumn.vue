<template>
  <div>
    <Drawer v-model="visible"
            :title="$t('source.manager.visibleColumn')"
            :mask-closable="false"
            @close="handlerCancel">
      <CheckboxGroup v-model="applyColumns">
        <List header=""
              footer="">
          <ListItem v-for="column in columns"
                    :key="column">
            <Checkbox :label="column.field"></Checkbox>
          </ListItem>
        </List>
      </CheckboxGroup>
      <template #close>
        <Tooltip :content="$t('common.apply')"
                 transfer>
          <Button size="small"
                  type="primary"
                  :style="{marginTop: '3px'}"
                  @click="handlerCancel">
            {{ $t('common.apply') }}
          </Button>
        </Tooltip>
      </template>
    </Drawer>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  name: "TableColumn",
  props: {
    isVisible: {
      type: Boolean
    },
    columns: {
      type: Array,
      default: () => []
    }
  },
  data()
  {
    return {
      applyColumns: []
    }
  },
  created()
  {
    this.handlerInitialize();
  },
  methods: {
    handlerInitialize()
    {
      this.columns.filter((item: { checked: any; }) => item.checked)
        .forEach((item: { field: any; }) => {
          this.applyColumns.push(item.field)
        })
    },
    handlerCancel()
    {
      this.$emit('onClose', this.applyColumns)
      this.visible = false
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
