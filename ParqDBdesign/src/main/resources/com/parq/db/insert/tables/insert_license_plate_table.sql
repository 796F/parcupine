CREATE TABLE licenseplate
(plate_id BIGINT NOT NULL AUTO_INCREMENT,
 user_id BIGINT NOT NULL,
 plate_number TEXT(10) NOT NULL,
 is_primary BOOLEAN,
 is_deleted BOOLEAN DEFAULT FALSE,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 PRIMARY KEY (plate_id),
 FOREIGN KEY (user_id) references user(user_id));
