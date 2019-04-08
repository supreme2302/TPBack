ALTER TABLE group_course DROP COLUMN current_unit;
ALTER TABLE group_course ADD COLUMN current_unit INTEGER NULL;
ALTER TABLE group_course ADD CONSTRAINT unit_group
FOREIGN KEY (current_unit) REFERENCES unit(id);