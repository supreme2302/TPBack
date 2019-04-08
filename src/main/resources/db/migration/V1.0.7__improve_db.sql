ALTER TABLE group_course ADD COLUMN  school_id INTEGER;
ALTER TABLE group_course ADD CONSTRAINT groupconstr FOREIGN KEY (school_id) REFERENCES school(id);
ALTER TABLE unit ADD COLUMN  school_id INTEGER;
ALTER TABLE unit ADD CONSTRAINT unitconstr FOREIGN KEY (school_id) REFERENCES school(id);
ALTER TABLE task ADD COLUMN school_id INTEGER;
ALTER TABLE task ADD CONSTRAINT taskconstr FOREIGN KEY (school_id) REFERENCES school(id);