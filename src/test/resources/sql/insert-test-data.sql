INSERT INTO admin (email, password)
VALUES ('exist@e.ru', '$2a$05$c8YM1tfJdPl7xAp5CV.pU.3qlXoj0Haf7vSYGL85PfVS1pkR.sXqm');
INSERT INTO school (school_name, ownerid) VALUES ('SSOE', 1);
UPDATE admin SET school_id = 1 WHERE id = 1;


INSERT INTO course (id, course_name, school_id,description) VALUES (1, 'TOEFL prep', 1, 'Preparation for TOEFL');
-- INSERT INTO course (course_name, school_id, description) VALUES ( 'IELTS prep',1, 'Preparation for IELTS');
-- INSERT INTO course (course_name, school_id) VALUES ('English for g...',2);
-- INSERT INTO unit (unit_name, course_id,school_id) VALUES ('English for g...',2,1,2);
-- INSERT INTO group_course (group_name, course_id, current_unit,school_id) VALUES ('HyperGroup',2,1,2);
-- INSERT INTO student (email, first_name, last_name, password, school_id) VALUES ('studentDTO@mail.ru', 'Polkovnik', 'Shatilov',  '$2a$05$c8YM1tfJdPl7xAp5CV.pU.3qlXoj0Haf7vSYGL85PfVS1pkR.sXqm',2);
-- INSERT INTO student (email, first_name, last_name, password, school_id) VALUES ('colonel@mail.ru', 'Polkovnik', 'Pogorelow',  '$2a$05$c8YM1tfJdPl7xAp5CV.pU.3qlXoj0Haf7vSYGL85PfVS1pkR.sXqm',2);
-- INSERT INTO student (email, first_name, last_name, password, school_id) VALUES ('prapor@mail.ru', 'Praporshik', 'Shmatko',  '$2a$05$c8YM1tfJdPl7xAp5CV.pU.3qlXoj0Haf7vSYGL85PfVS1pkR.sXqm',2);
-- INSERT INTO student (email, first_name, last_name, password, school_id) VALUES ('egor@mail.ru', 'Coursant', 'Chuvashow',  '$2a$05$c8YM1tfJdPl7xAp5CV.pU.3qlXoj0Haf7vSYGL85PfVS1pkR.sXqm',2);
-- INSERT INTO task_type (type_value, type_name) VALUES (1, 'Test na muzhika');
-- INSERT INTO task (task_type, task_ref,description,school_id) VALUES (1,1, 'Test na muzhika', 'Test nachala',2);
-- INSERT INTO student_group (group_id, student_id) VALUES (1,1);
-- INSERT INTO student_group(group_id,student_id) VALUES(2,2);
-- INSERT INTO student_group(group_id,student_id) VALUES(3,2);


-- TODO: Сделать так, чтобы нельзя было создать group_course с юнитом из другого курса