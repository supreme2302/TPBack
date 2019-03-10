
DROP TABLE IF EXISTS admin CASCADE ;


INSERT INTO admin (email, password) VALUES ('exist@e.ru', '$2a$10$rXn4xiRPY45wJVi39KAm.eKElUDcBQI4b58sqiEjrTXaTFrRn5nOW');

CREATE TABLE IF NOT EXISTS school (
  id SERIAL NOT NULL PRIMARY KEY,
  school_name citext NOT NULL UNIQUE ,
  device_id TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS admin (
  id SERIAL NOT NULL PRIMARY KEY ,
  email VARCHAR (255) UNIQUE NOT NULL ,
  password VARCHAR (255) NOT NULL,
  school_id INTEGER UNIQUE REFERENCES school(id)
);

CREATE TABLE IF NOT EXISTS group_course (
  id SERIAL NOT NULL PRIMARY KEY,
  group_name VARCHAR (255),
  course_id INTEGER REFERENCES course(id),
  current_unit INTEGER REFERENCES unit(current_position)
);

CREATE UNIQUE INDEX group_courseIx ON "group_course"(group_name, course_id);


CREATE TABLE IF NOT EXISTS student (
  id SERIAL NOT NULL PRIMARY KEY ,
  email VARCHAR (255) NOT NULL UNIQUE ,
  first_name VARCHAR (255) NOT NULL ,
  last_name VARCHAR (255) NOT NULL ,
  password TEXT NOT NULL,
  school_id INTEGER NOT NULL REFERENCES school(id)
);

CREATE TABLE IF NOT EXISTS student_group (
  id SERIAL NOT NULL PRIMARY KEY ,
  group_id INTEGER REFERENCES group_course(id),
  student_id INTEGER REFERENCES student(id)
);