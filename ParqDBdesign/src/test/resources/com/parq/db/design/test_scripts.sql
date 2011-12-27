
-- New table structure to suppor the Django framework use for the website development framework
-- The auth_user table is an framework specific user table equalivant
-- We are going to use sql triggers to keep the 2 table in sync
CREATE TABLE auth_user
(authuser_id BIGINT NOT NULL AUTO_INCREMENT,  
 user_id BIGINT NOT NULL DEFAULT 1,
 username TEXT(64),  
 first_name TEXT(64),
 last_name TEXT(64),
 email TEXT(64),
 password TEXT(64),
 phone_number TEXT(64),
 is_staff BOOLEAN DEFAULT FALSE,
 is_active BOOLEAN DEFAULT TRUE, 
 is_superuser BOOLEAN DEFAULT FALSE,
 last_login DATETIME,
 date_joined TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 PRIMARY KEY (authuser_id),
 FOREIGN KEY (user_id) references user(user_id));

DELIMITER |

-- following 3 script keep the user table in sync when the auth_user table is updated.
CREATE TRIGGER ins_trig_user_auth_before BEFORE INSERT ON auth_user
FOR EACH ROW
BEGIN
INSERT INTO user (email, password, phone_number)
  VALUES (NEW.email, NEW.password, NEW.phone_number);
SET NEW.user_id = (SELECT MAX(user_id) FROM user);
END;

CREATE TRIGGER upd_trig_user_auth_after AFTER UPDATE on auth_user
FOR EACH ROW
BEGIN
UPDATE user SET email = NEW.email, 
  password = NEW.password,
  phone_number = NEW.phone_number,
  is_deleted = NOT NEW.is_active
  WHERE user_id = OLD.user_id;
END;

CREATE TRIGGER del_trig_user_auth_after AFTER DELETE on auth_user
FOR EACH ROW
BEGIN
DELETE FROM user WHERE user_id = OLD.user_id;
END;

|
DELIMITER ;