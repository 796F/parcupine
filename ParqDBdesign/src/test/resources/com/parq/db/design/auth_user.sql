
CREATE TABLE auth_user_info 
(auth_user_info_id BIGINT NOT NULL,  
 username TEXT(64),  
 first_name TEXT(64),
 last_name TEXT(64),
 phone_number TEXT(64),
 is_staff BOOLEAN DEFAULT FALSE, 
 is_superuser BOOLEAN DEFAULT FALSE,
 last_login DATETIME,
 date_joined DATETIME,
 PRIMARY KEY (auth_user_info_id),
 FOREIGN KEY (auth_user_info_id) references user(user_id));
 
CREATE VIEW auth_user AS 
	SELECT u.user_id AS id,           
		i.username,
		i.first_name,
		i.last_name,
		u.email,
		u.password,
		i.is_staff,
		NOT u.is_deleted AS is_active,
		i.is_superuser,
		i.last_login,
		i.date_joined
	FROM user AS u INNER JOIN auth_user_info AS i
	WHERE u.user_id = i.auth_user_info_id;

INSERT INTO auth_user (email, password) 
	VALUES ('test@parq.com', 'test');