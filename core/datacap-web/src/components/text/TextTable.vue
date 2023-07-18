<template>
  <div>
    <Card dis-hover
          :padding="5"
          :bordered="false"
          style="margin-top: 4px; max-height: 400px">
      <Scroll>
        <List size="small"
              item-layout="vertical">
          <ListItem v-for="column in configure.columns"
                    :key="column"
                    style="white-space: nowrap;">
                <span v-for="header in configure.headers"
                      :key="header">
                  <span v-for="line in column[header].split('\n')"
                        :key="line">
                    <p>{{ line }}</p>
                  </span>
                </span>
          </ListItem>
        </List>
      </Scroll>
    </Card>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import {TableConfigure} from "@/components/table/TableConfigure";

export default defineComponent({
  name: "TextTable",
  props: {
    configure: {
      type: TableConfigure,
      default: () => null
    }
  }
});
</script>
