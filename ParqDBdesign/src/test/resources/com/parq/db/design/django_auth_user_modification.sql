
CREATE TABLE auth_user
(id INT NOT NULL AUTO_INCREMENT,  
 username TEXT(64),  
 first_name TEXT(64),
 last_name TEXT(64),
 email TEXT(64),
 password TEXT(64),
 is_staff BOOLEAN DEFAULT FALSE, 
 is_active BOOLEAN DEFAULT TRUE,
 is_superuser BOOLEAN DEFAULT FALSE,
 last_login DATETIME,
 date_joined DATETIME,
 PRIMARY KEY (id));
 
ALTER TABLE user ADD(
 django_user_id INT,
 FOREIGN KEY (django_user_id) references auth_user(id)
);

ALTER TABLE admin ADD(
 django_user_id INT,
 FOREIGN KEY (django_user_id) references auth_user(id)
);
 
 
INSERT INTO auth_user (email, username, password) 
	SELECT email, email, password FROM user;

INSERT INTO auth_user (email, username, password) 
	SELECT email, email, password FROM admin;
	
UPDATE user AS u SET u.django_user_id = (SELECT a.id FROM auth_user AS a WHERE u.email = a.email);
UPDATE admin AS m SET m.django_user_id = (SELECT a.id FROM auth_user AS a WHERE m.email = a.email);

