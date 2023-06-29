-- --------------------------------
--       Update to 1.12.0        --
-- --------------------------------
alter table `menus`
    add column `redirect` bigint default 0;
alter table `menus`
    add column `is_new` boolean default false;

alter table `user_chat`
    add column `model`             varchar(30) default 'gpt-3.5-turbo-0613',
    add column `prompt_tokens`     bigint      default 0,
    add column `completion_tokens` bigint      default 0,
    add column `total_tokens`      bigint      default 0;