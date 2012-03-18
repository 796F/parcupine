CREATE TABLE paymentmethod
(method_id BIGINT NOT NULL AUTO_INCREMENT,
 client_id BIGINT NOT NULL,
 method_name TEXT(64) NOT NULL,
 is_deleted BOOLEAN DEFAULT FALSE,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (method_id),
 FOREIGN KEY (client_id) references client(client_id));
