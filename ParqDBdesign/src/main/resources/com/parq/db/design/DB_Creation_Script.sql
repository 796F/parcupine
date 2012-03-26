DROP DATABASE parq;

CREATE DATABASE parq;

USE parq;

CREATE TABLE user
(user_id BIGINT NOT NULL AUTO_INCREMENT,  
 email TEXT(64) NOT NULL,
 password TEXT(64) NOT NULL, 
 phone_number TEXT(64),
 account_type text(64) NOT NULL,
 is_deleted BOOLEAN DEFAULT FALSE,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (user_id));
 
CREATE TABLE usersetting
(user_id BIGINT NOT NULL,
 sound BOOLEAN NOT NULL DEFAULT FALSE,
 vibrate BOOLEAN NOT NULL DEFAULT FALSE,
 max_refill_mins INT,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (user_id),
 FOREIGN KEY (user_id) references user(user_id));
 
CREATE TABLE licenseplate
(plate_id BIGINT NOT NULL AUTO_INCREMENT,
 user_id BIGINT NOT NULL,
 plate_number TEXT(10) NOT NULL,
 is_primary BOOLEAN,
 is_deleted BOOLEAN DEFAULT FALSE,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 PRIMARY KEY (plate_id),
 FOREIGN KEY (user_id) references user(user_id));
 
CREATE TABLE client
(client_id BIGINT NOT NULL AUTO_INCREMENT,
 name TEXT(64) NOT NULL,
 address TEXT(256),
 client_desc TEXT(256),
 payment_method TEXT(64) NOT NULL,
 is_deleted BOOLEAN DEFAULT FALSE,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (client_id)); 

CREATE TABLE admin
(admin_id BIGINT NOT NULL AUTO_INCREMENT,
 password TEXT(64) NOT NULL,
 email TEXT(64) NOT NULL,
 client_id BIGINT NOT NULL,
 admin_role TEXT(64),
 is_deleted BOOLEAN DEFAULT FALSE,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (admin_id),
 FOREIGN KEY (client_id) references client(client_id)); 
 
CREATE TABLE parkinggrid
(grid_id BIGINT NOT NULL AUTO_INCREMENT,
 grid_name TEXT(16) NOT NULL,
 latitude DOUBLE NOT NULL,
 longitude DOUBLE NOT NULL,
 is_deleted BOOLEAN DEFAULT FALSE,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (grid_id));
 
CREATE TABLE parkinglocation
(location_id BIGINT NOT NULL AUTO_INCREMENT,
 location_identifier TEXT(16) NOT NULL,
 client_id BIGINT NOT NULL,
 grid_id BIGINT NOT NULL,
 location_name TEXT(64),
 location_type TEXT(64),
 is_deleted BOOLEAN DEFAULT FALSE,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (location_id),
 FOREIGN KEY (client_id) references client(client_id),
 FOREIGN KEY (grid_id) references parkinggrid(grid_id));
 
CREATE TABLE geopoint
(geopoint_id BIGINT NOT NULL AUTO_INCREMENT,
 location_id BIGINT NOT NULL,
 latitude DOUBLE NOT NULL,
 longitude DOUBLE NOT NULL,
 sort_order INT NOT NULL,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (geopoint_id),
 FOREIGN KEY (location_id) references parkinglocation(location_id));
 
CREATE TABLE parkingspace
(space_id BIGINT NOT NULL AUTO_INCREMENT,
 space_identifier TEXT(16) NOT NULL,
 location_id BIGINT NOT NULL,
 space_name TEXT(64),
 parking_level TEXT(16),
 latitude DOUBLE NOT NULL,
 longitude DOUBLE NOT NULL,
 is_deleted BOOLEAN DEFAULT FALSE,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (space_id),
 FOREIGN KEY (location_id) references parkinglocation(location_id));

CREATE TABLE parkingrate
(rate_id BIGINT NOT NULL AUTO_INCREMENT,
 location_id BIGINT NOT NULL,
 parking_rate_cents INT NOT NULL,
 time_increment_mins INT NOT NULL,
 priority INT NOT NULL,
 min_park_mins INT,
 max_park_mins INT,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (rate_id),
 FOREIGN KEY (location_id) references parkinglocation(location_id));
 
CREATE TABLE specialuserrate
(user_rate_id BIGINT NOT NULL AUTO_INCREMENT,
 user_id BIGINT NOT NULL,
 client_id BIGINT NOT NULL,
 parking_rate INT NOT NULL,
 location_id BIGINT,
 space_id BIGINT,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (user_rate_id),
 FOREIGN KEY (user_id) references user(user_id),
 FOREIGN KEY (client_id) references client(client_id),
 FOREIGN KEY (location_id) references parkinglocation(location_id),
 FOREIGN KEY (space_id) references parkingspace(space_id));

CREATE TABLE parkinginstance
(parkinginst_id BIGINT NOT NULL AUTO_INCREMENT,
 user_id BIGINT NOT NULL,
 space_id BIGINT NOT NULL,
 parkingrefnumber TEXT(64) NOT NULL,
 park_began_time DATETIME NOT NULL,
 park_end_time DATETIME NOT NULL,
 is_paid_parking BOOLEAN NOT NULL,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (parkinginst_id),
 FOREIGN KEY (user_id) references user(user_id),
 FOREIGN KEY (space_id) references parkingspace(space_id));
  
CREATE TABLE paymentaccount
(account_id BIGINT NOT NULL AUTO_INCREMENT,  
 user_id BIGINT NOT NULL,
 payment_method_id TEXT(64),
 customer_id TEXT(64),
 card_type TEXT(64),
 cc_stub TEXT(64),
 is_default_payment BOOLEAN DEFAULT FALSE,
 is_deleted BOOLEAN DEFAULT FALSE,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (account_id),
 FOREIGN KEY (user_id) references user(user_id));

CREATE TABLE prepaidaccountbalance
(user_id BIGINT NOT NULL,
 account_balance INT NOT NULL DEFAULT 0,
 is_deleted BOOLEAN DEFAULT FALSE,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 PRIMARY KEY (user_id),
 FOREIGN KEY (user_id) references user(user_id));
 
CREATE TABLE payment
(payment_id BIGINT NOT NULL AUTO_INCREMENT,
 parkinginst_id BIGINT NOT NULL,
 payment_type TEXT(64) NOT NULL,
 user_id BIGINT,
 account_id BIGINT,
 payment_ref_num TEXT(64) NOT NULL,
 payment_datetime DATETIME NOT NULL,
 amount_paid_cents INT NOT NULL,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (payment_id),
 FOREIGN KEY (parkinginst_id) references parkinginstance(parkinginst_id),
 FOREIGN KEY (user_id) references prepaidaccountbalance(user_id),
 FOREIGN KEY (account_id) references paymentaccount(account_id));
