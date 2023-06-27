-- --------------------------------
--       Update to 1.12.0        --
-- --------------------------------
alter table menus
    add column `redirect` long default 0;
