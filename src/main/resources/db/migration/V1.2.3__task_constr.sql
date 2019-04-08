DELETE FROM task_unit;
DELETE FROM task;
ALTER TABLE task ADD CONSTRAINT tasksSchoolConstr UNIQUE (name, school_id);