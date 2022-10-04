<template>
    <div>
        <a-card title="Plugin Audit" size="small">
            <a-table size="middle" :loading="loading" :columns="headers" :data-source="columns" :pagination="pagination"
                @change="handlerTableChange($event)">
                <template #headerCell="{ column }">
                    <template v-if="column.key === 'elapsed'">
                        <span>
                            {{column.title}} &nbsp;
                            <a-tooltip>
                                <template #title>The value is expressed in milliseconds</template>
                                <question-circle-outlined />
                            </a-tooltip>
                        </span>
                    </template>
                </template>

                <template #bodyCell="{ column, record }">
                    <template v-if="column.dataIndex === 'plugin'">
                        {{record.plugin.name}}
                    </template>
                    <template v-if="column.dataIndex === 'state'">
                        <a-tag :color="record.state === 'SUCCESS' ? '#87d068' : '#f50'">{{record.state}}</a-tag>
                    </template>
                    <template v-if="column.dataIndex === 'action'">
                        <a-space style="width: 100%">
                            <a-button type="primary" shape="circle" size="small"
                                @click="handlerShowContent(record.content)">
                                <a-tooltip>
                                    <template #title>SQL</template>
                                    <eye-outlined />
                                </a-tooltip>
                            </a-button>
                            <a-button :disabled="record.state === 'SUCCESS'" danger type="primary" shape="circle"
                                size="small" @click="handlerShowError(record.message)">
                                <a-tooltip>
                                    <template #title>Error</template>
                                    <warning-outlined />
                                </a-tooltip>
                            </a-button>
                        </a-space>
                    </template>
                </template>
            </a-table>
        </a-card>
    </div>

    <ConsoleSQLComponent v-if="visibleContent" :isVisible="visibleContent" :content="content"
        @close="handlerCloseContent($event)" />
</template>

<script lang="ts">
import ConsoleSQLComponent from "@/components/ConsoleSQL.vue";
import { AuditService } from "@/services/AuditService";
import { headers } from "@/views/pages/console/ConsoleGenerate";
import { Modal } from "ant-design-vue";
import { defineComponent } from "vue";

export default defineComponent({
    name: "ConsoleHistoryView",
    components: { ConsoleSQLComponent },
    data() {
        return {
            headers,
            columns: [],
            loading: false,
            content: '',
            visibleContent: false,
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
        },
        handlerShowError(message: string) {
            Modal.error({
                title: 'Error Message',
                content: message,
            });
        },
        handlerShowContent(content: string) {
            this.visibleContent = true;
            this.content = content;
        },
        handlerCloseContent(value: boolean) {
            this.visibleContent = value;
            this.content = '';
        }
    }
});
</script>
