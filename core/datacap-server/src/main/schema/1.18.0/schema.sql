ALTER TABLE `datacap_user`
    ADD COLUMN `avatar_configure` LONGTEXT COMMENT 'avatar configure';

UPDATE `datacap_user`
SET `avatar_configure` = '{}'
WHERE `avatar_configure` IS NULL;

UPDATE `datacap_user`
SET `editor_configure` = '{}'
WHERE `editor_configure` IS NULL;
