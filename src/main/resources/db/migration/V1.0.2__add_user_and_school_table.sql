
ALTER TABLE main_user RENAME TO admin;


CREATE TABLE IF NOT EXISTS school (
  id SERIAL NOT NULL PRIMARY KEY,
  school_name citext NOT NULL UNIQUE ,
  device_id TEXT NOT NULL UNIQUE
);

ALTER TABLE admin ADD COLUMN school_id INTEGER UNIQUE ;

ALTER TABLE admin ADD CONSTRAINT adminconstr FOREIGN KEY (school_id) REFERENCES school(id);


CREATE TABLE IF NOT EXISTS course (
  id SERIAL NOT NULL PRIMARY KEY,
  course_name citext NOT NULL,
  school_id INTEGER REFERENCES school(id)
);

CREATE UNIQUE INDEX courseIx ON "course"(course_name, school_id);

CREATE TABLE IF NOT EXISTS unit (
  id SERIAL NOT NULL PRIMARY KEY ,
  current_position INTEGER NOT NULL UNIQUE ,
  course_id INTEGER REFERENCES course(id)
);

CREATE UNIQUE INDEX unitIx ON "unit"(current_position, course_id);

CREATE TABLE IF NOT EXISTS group_course (
  id SERIAL NOT NULL PRIMARY KEY,
  group_name citext,
  course_id INTEGER REFERENCES course(id),
  current_unit INTEGER REFERENCES unit(current_position)
);

CREATE UNIQUE INDEX group_courseIx ON "group_course"(group_name, course_id);


CREATE TABLE IF NOT EXISTS student (
  id SERIAL NOT NULL PRIMARY KEY ,
  email citext NOT NULL UNIQUE ,
  first_name citext NOT NULL ,
  last_name citext NOT NULL ,
  password TEXT NOT NULL,
  school_id INTEGER NOT NULL REFERENCES school(id)
);

CREATE TABLE IF NOT EXISTS student_group (
  id SERIAL NOT NULL PRIMARY KEY ,
  group_id INTEGER REFERENCES group_course(id),
  student_id INTEGER REFERENCES student(id)
);

CREATE TABLE IF NOT EXISTS task_type (
  id SERIAl NOT NULL PRIMARY KEY ,
  type_value INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS task (
  id SERIAL NOT NULL PRIMARY KEY ,
  task_type INTEGER NOT NULL REFERENCES task_type(id),
  unit_id INTEGER REFERENCES unit(id),
  task_ref TEXT NOT NULL
);