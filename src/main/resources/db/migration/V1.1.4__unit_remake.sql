ALTER TABLE group_course DROP COLUMN current_unit;
ALTER TABLE group_course ADD COLUMN current_unit INTEGER NULL REFERENCES unit(id);
ALTER TABLE unit DROP COLUMN current_position;
ALTER TABLE unit ADD COLUMN prev_unit INTEGER NULL REFERENCES unit(id);
ALTER TABLE unit ADD COLUMN next_unit INTEGER NULL REFERENCES unit(id);