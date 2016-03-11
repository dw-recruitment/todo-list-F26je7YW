CREATE TABLE todos
(id bigint auto_increment,
name VARCHAR(30),
description VARCHAR(30),
checked BOOLEAN DEFAULT FALSE,
date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP);