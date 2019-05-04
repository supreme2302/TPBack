ALTER TABLE admin DROP CONSTRAINT adminconstr;
ALTER TABLE admin DROP COLUMN school_id;
ALTER TABLE admin ADD COLUMN school_id INTEGER;
ALTER TABLE admin ADD CONSTRAINT adminconstr FOREIGN KEY (school_id) REFERENCES school(id);
