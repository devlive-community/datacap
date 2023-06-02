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

update `menus`
set url = '/system/function'
where url = '/admin/function';

update `menus`
set url = '/system/schedule'
where url = '/admin/schedule';

update `menus`
set url = '/system/template'
where url = '/admin/template';

update `menus`
set url = '/system/role'
where url = '/admin/role';

update `menus`
set url = '/system/menu'
where url = '/admin/menu';

update `menus`
set url = '/system/user'
where url = '/admin/user';
