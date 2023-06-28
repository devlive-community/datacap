-- --------------------------------
--       Update to 1.12.0        --
-- --------------------------------
alter table `menus`
    add column `redirect` bigint default 0;
alter table `menus`
    add column `is_new` boolean default false;
