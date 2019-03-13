
ALTER TABLE unit DROP COLUMN course_id;
ALTER TABLE unit DROP COLUMN course_id;

CREATE TABLE IF NOT EXISTS unit_to_course(
    id SERIAL NOT NULL PRIMARY KEY,
    unit_id INT NOT NULL REFERENCES unit(id),
    course_id INT NOT NULL REFERENCES course(id)
)