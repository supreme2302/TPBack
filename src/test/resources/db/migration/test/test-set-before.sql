
DROP TABLE IF EXISTS admin,school,student,course,unit,group_course CASCADE ;

CREATE TABLE IF NOT EXISTS school (
  id SERIAL NOT NULL PRIMARY KEY,
  school_name VARCHAR (255) NOT NULL UNIQUE ,
  device_id VARCHAR (255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS admin (
  id SERIAL NOT NULL PRIMARY KEY ,
  email VARCHAR (255) UNIQUE NOT NULL ,
  password VARCHAR (255) NOT NULL,
  school_id INTEGER REFERENCES school(id)
);

CREATE TABLE IF NOT EXISTS course (
  id SERIAL NOT NULL PRIMARY KEY,
  course_name VARCHAR(255) NOT NULL,
  school_id INTEGER REFERENCES school(id)
);

CREATE UNIQUE INDEX IF NOT EXISTS courseIx ON course(course_name, school_id);

CREATE TABLE IF NOT EXISTS unit (
  id SERIAL NOT NULL PRIMARY KEY ,
  unit_name VARCHAR(255) NOT NULL ,
  current_position INTEGER NOT NULL UNIQUE ,
  course_id INTEGER REFERENCES course(id),
  school_id INTEGER REFERENCES school(id)
);



CREATE TABLE IF NOT EXISTS group_course (
  id SERIAL NOT NULL PRIMARY KEY,
  group_name VARCHAR (255),
  course_id INTEGER REFERENCES course(id),
  current_unit INTEGER NULL REFERENCES unit(current_position),
  school_id INTEGER REFERENCES school(id)
);



CREATE TABLE IF NOT EXISTS student (
  id SERIAL NOT NULL PRIMARY KEY ,
  email VARCHAR (255) NOT NULL UNIQUE ,
  first_name VARCHAR (255) NOT NULL ,
  last_name VARCHAR (255) NOT NULL ,
  password VARCHAR (255) NOT NULL,
  school_id INTEGER NOT NULL REFERENCES school(id)
);

CREATE TABLE IF NOT EXISTS student_group (
  id SERIAL NOT NULL PRIMARY KEY ,
  group_id INTEGER REFERENCES group_course(id),
  student_id INTEGER REFERENCES student(id),
  school_id INTEGER REFERENCES school(id)
);

CREATE TABLE IF NOT EXISTS task_type (
                                       id SERIAl NOT NULL PRIMARY KEY ,
                                       type_value INTEGER NOT NULL ,
                                       type_name VARCHAR (255) NOT NULL
);

CREATE TABLE IF NOT EXISTS task (
  id SERIAL NOT NULL PRIMARY KEY ,
  task_type INTEGER NOT NULL REFERENCES task_type(id),
  unit_id INTEGER REFERENCES unit(id),
  task_ref VARCHAR (255) NOT NULL,
  description VARCHAR(255),
  school_id INTEGER REFERENCES school(id)
);



CREATE UNIQUE INDEX IF NOT EXISTS courseIx ON course(course_name, school_id);
CREATE UNIQUE INDEX IF NOT EXISTS unitIx ON unit(current_position, course_id);
CREATE UNIQUE INDEX IF NOT EXISTS group_courseIx ON group_course(group_name, course_id);


INSERT INTO admin (email, password) VALUES ('exist@e.ru', '$2a$10$rXn4xiRPY45wJVi39KAm.eKElUDcBQI4b58sqiEjrTXaTFrRn5nOW');
INSERT INTO school (id, school_name, device_id) VALUES (1,'SSOE','8893');
INSERT INTO school (id, school_name, device_id) VALUES (2,'EFG','3219');
INSERT INTO admin (email, password, school_id) VALUES ('existForKostyan@e.ru', '$2a$10$rXn4xiRPY45wJVi39KAm.eKElUDcBQI4b58sqiEjrTXaTFrRn5nOW', 2);
INSERT INTO course (id,course_name, school_id) VALUES (1,'TOEFL prep',1);
INSERT INTO course (id, course_name, school_id) VALUES (2, 'IELTS prep',1);
INSERT INTO course (id, course_name, school_id) VALUES (3, 'English for g...',2);
INSERT INTO unit (unit_name, course_id,current_position,school_id) VALUES ('English for g...',2,1,2);
INSERT INTO group_course (group_name, course_id, current_unit,school_id) VALUES ('HyperGroup',2,1,2);
INSERT INTO student (email, first_name, last_name, password, school_id) VALUES ('student@mail.ru', 'Polkovnik', 'Shatilov',  '$2a$10$rXn4xiRPY45wJVi39KAm.eKElUDcBQI4b58sqiEjrTXaTFrRn5nOW',2);
INSERT INTO task_type (type_value, type_name) VALUES (1, 'Test na muzhika');
INSERT INTO task (task_type, unit_id, task_ref,description,school_id) VALUES (1,1, 'Test na muzhika', 'Test nachala',2);
INSERT INTO student_group (group_id, student_id) VALUES (1,1);

-- TODO: Сделать так, чтобы нельзя было создать group_course с юнитом из другого курса