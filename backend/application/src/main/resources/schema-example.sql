DROP DATABASE IF EXISTS project_society;
DROP USER IF EXISTS 'society'@'%';
DROP EVENT IF EXISTS project_society.delete_expired;
CREATE DATABASE IF NOT EXISTS project_society;
CREATE USER IF NOT EXISTS 'society'@'%' IDENTIFIED BY 'ThePassword';
GRANT ALL ON project_society.* TO 'society'@'%';

CREATE TABLE IF NOT EXISTS project_society.time_block_days(
         uuid VARCHAR(255) PRIMARY KEY,
         user_id VARCHAR(255) NOT NULL,
         day_of_week INTEGER NOT NULL,
         time_block_list BLOB
);

CREATE TABLE IF NOT EXISTS project_society.sessions(
    id VARCHAR(255) PRIMARY KEY,
    original_id VARCHAR(255) NOT NULL,
    session_attrs BLOB,
    creation_time DATETIME NOT NULL,
    last_accessed_time DATETIME NOT NULL,
    expires_at DATETIME NOT NULL
    );

CREATE EVENT project_society.delete_expired ON SCHEDULE EVERY 1 MINUTE DO DELETE FROM project_society.sessions WHERE expires_at < NOW();