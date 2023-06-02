-- --------------------------------
--       Update to 1.11.0        --
-- --------------------------------
alter table `audit_plugin`
    add column `count` bigint(20) default 0;

update `menus`
set url = '/admin'
where name = '全局 - 管理主菜单';

update `menus`
set url = '/system'
where name = '管理员 - 系统主菜单';
