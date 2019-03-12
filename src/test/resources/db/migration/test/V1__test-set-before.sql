
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
  school_id INTEGER UNIQUE REFERENCES school(id)
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
  course_id INTEGER REFERENCES course(id)
);



CREATE TABLE IF NOT EXISTS group_course (
  id SERIAL NOT NULL PRIMARY KEY,
  group_name VARCHAR (255),
  course_id INTEGER REFERENCES course(id),
  current_unit INTEGER REFERENCES unit(current_position)
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
  student_id INTEGER REFERENCES student(id)
);

CREATE UNIQUE INDEX IF NOT EXISTS courseIx ON course(course_name, school_id);
CREATE UNIQUE INDEX IF NOT EXISTS unitIx ON unit(current_position, course_id);
CREATE UNIQUE INDEX IF NOT EXISTS group_courseIx ON group_course(group_name, course_id);


INSERT INTO admin (email, password) VALUES ('exist@e.ru', '$2a$10$rXn4xiRPY45wJVi39KAm.eKElUDcBQI4b58sqiEjrTXaTFrRn5nOW');
INSERT INTO school (id, school_name, device_id) VALUES (1,'SSOE','8893');
INSERT INTO school (id, school_name, device_id) VALUES (2,'EFG','3219');
INSERT INTO admin (email, password, school_id) VALUES ('existForKostyan@e.ru', '$2a$10$rXn4xiRPY45wJVi39KAm.eKElUDcBQI4b58sqiEjrTXaTFrRn5nOW', 1);
INSERT INTO course (id,course_name, school_id) VALUES (1,'TOEFL prep',1);
INSERT INTO course (course_name, school_id) VALUES ('IELTS prep',1);
INSERT INTO course (course_name, school_id) VALUES ('English for g...',2);
INSERT INTO unit (unit_name, course_id,current_position) VALUES ('English for g...',2,1);
INSERT INTO group_course (group_name, course_id, current_unit) VALUES ('HyperGroup',1,1);