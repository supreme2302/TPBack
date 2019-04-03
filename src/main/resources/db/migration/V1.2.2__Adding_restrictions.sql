ALTER TABLE unit ADD CONSTRAINT unitconstr UNIQUE (unit_name, course_id);
ALTER TABLE task ADD CONSTRAINT taskconstr UNIQUE (name, school_id);