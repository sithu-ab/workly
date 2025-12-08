ALTER TABLE tasks DROP FOREIGN KEY fk_user;
DROP INDEX idx_tasks_user_id ON tasks;
ALTER TABLE tasks DROP COLUMN user_id;
DROP TABLE users;
DELETE FROM flyway_schema_history WHERE version = '02';
