DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS projects;
DELETE FROM flyway_schema_history WHERE version = '01';
