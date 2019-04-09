INSERT INTO admin (email, password) VALUES ('Makarenko.A@mail.ussr','$2a$05$fZeCfCuDnwlKhOdtZzut7.Rl7D1NXekss54GpJAQoApx02MBpq196');
INSERT INTO school (school_name, ownerid, main_color, secondary_color, language, school_logo) VALUES ('SchoolRus', 1,'#86030b','#86030b','Russian',null);
UPDATE admin SET school_id=1 WHERE id=1;
INSERT INTO admin (email, password) VALUES ('Aristotel@lingva.macedon','$2a$05$fZeCfCuDnwlKhOdtZzut7.Rl7D1NXekss54GpJAQoApx02MBpq196');
INSERT INTO school (school_name, ownerid, main_color, secondary_color, language, school_logo) VALUES ('GreekCsarSchool', 1,'#ffffff','#937ef6','Greek',null);
UPDATE admin SET school_id=2 WHERE id=2;
INSERT INTO course (course_name, school_id, description) VALUES ('Russian for besprizorniki',1,'This is not a course, this is a miracle.');
INSERT INTO course (course_name, school_id, description) VALUES ('Math for besprizorniki',2,'This is not a course, this is math.');
INSERT INTO unit (course_id, unit_name, school_id, description, prev_unit, next_unit) VALUES (1,'Subverbs',1,'And another one bites te dust',null,null);
INSERT INTO unit (coпurse_id, unit_name, school_id, description, prev_unit, next_unit) VALUES (1,'Proverbs',1,'And another one bites te dust',1,null);
UPDATE unit SET next_unit=2 WHERE id=1;
INSERT INTO unit (course_id, unit_name, school_id, description, prev_unit, next_unit) VALUES (1,'Multiverbs',1,'And another one bites te dust',2,null);
UPDATE unit SET next_unit=3 WHERE id=2;
INSERT INTO unit (course_id, unit_name, school_id, description, prev_unit, next_unit) VALUES (1,'Megaverbs',1,'And another one bites te dust',3,null);
UPDATE unit SET next_unit=4 WHERE id=3;
INSERT INTO task (school_id, task_type, name, task_val) VALUES (1,1,'Love task', '{"text": "What is love?", "answers": ["No more", "I dont care", "you and me"], "correct": "Baby dont hurt me"}');
INSERT INTO task (school_id, task_type, name, task_val) VALUES (1,1,'Dartangan task', '{"text": "Пора-пора-порадуемся?", "answers": ["Мерси боку", "лети моя голубка", "Зеленоглазое такси"], "correct": "На своем веку"}');
INSERT INTO task (school_id, task_type, name, task_val) VALUES (1,1,'Hertz task', '{"text": "Mein hertz?", "answers": ["du hast", "Went", "Deutchland"], "correct": "Brent"}');
INSERT INTO task (school_id, task_type, name, task_val) VALUES (1,1,'Panzerkampf task', '{"text": "Into the motherland?", "answers": ["The winged hussars arrive", "Prima victoria", "Comrads stand side by side"], "correct": "the german army march"}');
INSERT INTO task_unit (task_id, unit_id) VALUES (1,1);
INSERT INTO task_unit (task_id, unit_id) VALUES (2,1);
INSERT INTO task_unit (task_id, unit_id) VALUES (3,1);
INSERT INTO task_unit (task_id, unit_id) VALUES (4,2);
INSERT INTO group_course (group_name, course_id, school_id, description, current_unit) VALUES ('3d class',1,1,'Group of little alcoholics',1);
INSERT INTO group_course (group_name, course_id, school_id, description, current_unit) VALUES ('7a class',1,1,'Group of big alcoholics',1);
INSERT INTO student (email, first_name, last_name, password, school_id) VALUES ('Papa@Stalin.ussr','Joseph','Jugashvili','$2a$05$fZeCfCuDnwlKhOdtZzut7.Rl7D1NXekss54GpJAQoApx02MBpq196',1);
INSERT INTO student (email, first_name, last_name, password, school_id) VALUES ('Uncle@MAO.cpr','Dze','Dongu','$2a$05$fZeCfCuDnwlKhOdtZzut7.Rl7D1NXekss54GpJAQoApx02MBpq196',1);
INSERT INTO student (email, first_name, last_name, password, school_id) VALUES ('Great@Leader.ussr','Lenin','Vovka','$2a$05$fZeCfCuDnwlKhOdtZzut7.Rl7D1NXekss54GpJAQoApx02MBpq196',1);
INSERT INTO student (email, first_name, last_name, password, school_id) VALUES ('Cuban@Revolt.csr','Che','Gevara','$2a$05$fZeCfCuDnwlKhOdtZzut7.Rl7D1NXekss54GpJAQoApx02MBpq196',1);
INSERT INTO student_group (group_id, student_id) VALUES (1,1);
INSERT INTO student_group (group_id, student_id) VALUES (1,2);
INSERT INTO student_group (group_id, student_id) VALUES (2,2);
INSERT INTO student_group (group_id, student_id) VALUES (2,3);
INSERT INTO student_group (group_id, student_id) VALUES (2,4);