USE parq;

ALTER TABLE user DROP FOREIGN KEY user_ibfk_1
ALTER TABLE user DROP COLUMN django_user_id;

ALTER TABLE admin DROP FOREIGN KEY admin_ibfk_1;
ALTER TABLE admin DROP COLUMN django_user_id;
