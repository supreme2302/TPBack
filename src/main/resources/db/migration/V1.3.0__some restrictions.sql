ALTER TABLE user_unit ADD UNIQUE (student_id,unit_id);
ALTER TABLE user_unit ALTER status SET DEFAULT 'not started';