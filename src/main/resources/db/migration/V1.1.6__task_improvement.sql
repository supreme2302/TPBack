ALTER TABLE task DROP COLUMN task_type;
ALTER TABLE task DROP COLUMN task_ref;
ALTER TABLE task DROP COLUMN description;
ALTER TABLE task ADD COLUMN task_type INTEGER;
ALTER TABLE task ADD COLUMN task_val citext;
ALTER TABLE task ADD COLUMN name citext;