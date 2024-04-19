USE `datacap`;

TRUNCATE TABLE `datacap_source_query`;

TRUNCATE TABLE `datacap_chat`;
TRUNCATE TABLE `datacap_chat_user_relation`;

TRUNCATE TABLE `datacap_dashboard`;
TRUNCATE TABLE `datacap_dashboard_report_relation`;
TRUNCATE TABLE `datacap_dashboard_user_relation`;

TRUNCATE TABLE `datacap_dataset`;
TRUNCATE TABLE `datacap_dataset_column`;
TRUNCATE TABLE `datacap_dataset_column_relation`;
TRUNCATE TABLE `datacap_dataset_source_relation`;
TRUNCATE TABLE `datacap_dataset_user_relation`;
TRUNCATE TABLE `datacap_dataset_history`;
TRUNCATE TABLE `datacap_dataset_history_relation`;

TRUNCATE TABLE `datacap_message`;
TRUNCATE TABLE `datacap_message_chat_relation`;
TRUNCATE TABLE `datacap_message_user_relation`;

TRUNCATE TABLE `datacap_metadata_column`;
TRUNCATE TABLE `datacap_metadata_column_table_relation`;

TRUNCATE TABLE `datacap_metadata_database`;
TRUNCATE TABLE `datacap_metadata_database_source_relation`;

TRUNCATE TABLE `datacap_metadata_table`;
TRUNCATE TABLE `datacap_metadata_table_database_relation`;

TRUNCATE TABLE `datacap_pipeline`;
TRUNCATE TABLE `datacap_pipeline_user_relation`;

TRUNCATE TABLE `datacap_report`;
TRUNCATE TABLE `datacap_report_source_relation`;
TRUNCATE TABLE `datacap_report_user_relation`;
TRUNCATE TABLE `datacap_report_dataset_relation`;

TRUNCATE TABLE `datacap_scheduled_history`;
TRUNCATE TABLE `datacap_scheduled_history_relation`;
TRUNCATE TABLE `datacap_scheduled_history_source_relation`;

TRUNCATE TABLE `datacap_user_log`;

TRUNCATE TABLE `datacap_source`;

TRUNCATE TABLE `datacap_snippet`;

mysqldump -u root -p datacap > $(pwd)/configure/schema/datacap.sql
