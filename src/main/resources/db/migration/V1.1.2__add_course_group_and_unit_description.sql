ALTER TABLE course ADD COLUMN IF NOT EXISTS description citext;
ALTER TABLE group_course ADD COLUMN IF NOT EXISTS description citext;
ALTER TABLE unit ADD COLUMN IF NOT EXISTS description citext;

CREATE TABLE IF NOT EXISTS task_unit (
                                           id SERIAL NOT NULL PRIMARY KEY ,
                                           task_id INTEGER REFERENCES task(id),
                                           unit_id INTEGER REFERENCES unit(id)
);

