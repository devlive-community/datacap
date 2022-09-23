<template>
    <div>
        <a-card title="Plugin Audit" size="small">
            <a-table size="middle" :loading="loading" :columns="headers" :data-source="columns" :pagination="pagination"
                @change="handlerTableChange($event)">
                <template #bodyCell="{ column, record }">
                    <template v-if="column.dataIndex === 'plugin'">
                        {{record.plugin.name}}
                    </template>
                    <template v-if="column.dataIndex === 'state'">
                        <a-tag :color="record.state === 'SUCCESS' ? '#87d068' : '#f50'">{{record.state}}</a-tag>
                    </template>
                </template>
            </a-table>
        </a-card>
    </div>
</template>

<script lang="ts">
import { AuditService } from "@/services/AuditService";
import { headers } from "@/views/pages/console/ConsoleGenerate";
import { defineComponent } from "vue";

export default defineComponent({
    name: "ConsoleHistoryView",
    components: {},
    data() {
        return {
            headers,
            columns: [],
            loading: false,
            pagination: {
                total: 0,
                current: 1,
                pageSize: 10,
                showSizeChanger: true,
                pageSizeOptions: ["10", "20", "50", "100"],
                showTotal: (total: number) => `Total <${total}> Elements`
            }
        }
    },
    created() {
        this.handlerInitialize(this.pagination.current, this.pagination.pageSize)
    },
    methods: {
        handlerInitialize(page: number, size: number) {
            this.loading = true;
            new AuditService()
                .getPluginAudits(page, size)
                .then((response) => {
                    if (response.status) {
                        this.columns = response.data.content;
                        this.pagination.total = response.data.total;
                    }
                    this.loading = false;
                })
        },
        handlerTableChange(pagination: any) {
            this.pagination.current = pagination.current;
            this.pagination.pageSize = pagination.pageSize;
            this.handlerInitialize(pagination.current, pagination.pageSize)
        }
    }
});
</script>
