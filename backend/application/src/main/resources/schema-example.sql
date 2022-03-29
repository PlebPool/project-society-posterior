DROP DATABASE IF EXISTS db_name;
DROP USER IF EXISTS 'user'@'%';
DROP EVENT IF EXISTS db_name.delete_expired;
CREATE DATABASE IF NOT EXISTS db_name;
CREATE USER IF NOT EXISTS 'user'@'%' IDENTIFIED BY 'pass';
GRANT ALL ON db_name.* TO 'user'@'%';

CREATE TABLE IF NOT EXISTS db_name.time_block_day(
    db_id VARCHAR(255) PRIMARY KEY,
    uuid VARCHAR(255), day_of_week VARCHAR(255),
    time_blocks BLOB # List with time blocks (sorted and indexed by time)
    );

CREATE TABLE IF NOT EXISTS db_name.sessions(
    id VARCHAR(255) PRIMARY KEY,
    original_id VARCHAR(255) NOT NULL,
    session_attrs BLOB,
    creation_time DATETIME NOT NULL,
    last_accessed_time DATETIME NOT NULL,
    expires_at DATETIME NOT NULL
    );

CREATE TABLE IF NOT EXISTS db_name.authorized_clients(
    principal_name VARCHAR(255) PRIMARY KEY,
    client_id VARCHAR(255) NOT NULL,
    body BLOB
    );

CREATE EVENT db_name.delete_expired ON SCHEDULE EVERY 1 MINUTE DO DELETE FROM db_name.sessions WHERE expires_at < NOW();