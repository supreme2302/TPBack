ALTER TABLE task DROP COLUMN task_val;
ALTER TABLE task ADD COLUMN task_val jsonb;