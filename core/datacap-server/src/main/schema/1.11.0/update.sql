-- --------------------------------
--       Update to 1.11.0        --
-- --------------------------------
alter table `audit_plugin`
    add column `count` bigint(20) default 0;
