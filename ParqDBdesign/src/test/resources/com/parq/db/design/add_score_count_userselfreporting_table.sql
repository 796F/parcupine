USE parq;

DROP TABLE score;
DROP TABLE userselfreporting;
DROP TABLE count;

CREATE TABLE score
(score_id BIGINT NOT NULL AUTO_INCREMENT,
 user_id BIGINT NOT NULL,
 score_1 BIGINT DEFAULT 0,
 score_2 BIGINT DEFAULT 0,
 score_3 BIGINT DEFAULT 0,
 is_deleted BOOLEAN DEFAULT FALSE,
 lastupdatedatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 PRIMARY KEY (score_id),
 FOREIGN KEY (user_id) references user(user_id));

CREATE TABLE userselfreporting
(report_id BIGINT NOT NULL AUTO_INCREMENT,
 user_id BIGINT NOT NULL,
 space_id BIGINT NOT NULL,
 space_status TEXT(16) NOT NULL,
 report_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 score_1 BIGINT DEFAULT 0,
 score_2 BIGINT DEFAULT 0,
 score_3 BIGINT DEFAULT 0,
 score_4 BIGINT DEFAULT 0,
 score_5 BIGINT DEFAULT 0,
 score_6 BIGINT DEFAULT 0,
 PRIMARY KEY (report_id),
 FOREIGN KEY (user_id) references user(user_id),
 FOREIGN KEY (space_id) references parkingspace(space_id));

CREATE TABLE count
(count_id BIGINT NOT NULL AUTO_INCREMENT,
 tempValue TEXT(64),
 PRIMARY KEY (count_id));