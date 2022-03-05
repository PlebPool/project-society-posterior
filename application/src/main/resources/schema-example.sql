DROP DATABASE IF EXISTS db_name;
DROP USER IF EXISTS 'user'@'%';
DROP EVENT IF EXISTS db_name.delete_expired;
CREATE DATABASE IF NOT EXISTS db_name;
CREATE USER IF NOT EXISTS 'user'@'%' IDENTIFIED BY 'pass';
GRANT ALL ON db_name.* TO 'user'@'%';

CREATE TABLE IF NOT EXISTS db_name.course_progress(user_id_and_id VARCHAR(255) PRIMARY KEY, id VARCHAR(255), name VARCHAR(255), progress DOUBLE);
CREATE TABLE IF NOT EXISTS db_name.time_block_day(db_id VARCHAR(255) PRIMARY KEY, uuid VARCHAR(255), day_of_week VARCHAR(255), time_blocks TEXT);

CREATE TABLE IF NOT EXISTS db_name.sessions(id VARCHAR(255) PRIMARY KEY, original_id VARCHAR(255), session_attrs BLOB, creation_time DATETIME, last_accessed_time DATETIME, expires_at DATETIME);
CREATE TABLE IF NOT EXISTS db_name.session_attribute(id_and_attribute_name VARCHAR(255) PRIMARY KEY, attribute_name VARCHAR(255), attribute_value TEXT);

CREATE EVENT db_name.delete_expired ON SCHEDULE EVERY 5 MINUTE DO DELETE FROM db_name.sessions WHERE expires_at < NOW();

SELECT sessions.* FROM db_name.sessions;
SELECT * FROM db_name.session_attribute;
SELECT UTC_TIMESTAMP();
SELECT NOW();

SET SQL_SAFE_UPDATES = 0;
UPDATE db_name.sessions SET original_id = "123" WHERE id = "321";

SELECT * FROM db_name.time_block_list;
