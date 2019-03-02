
DROP TABLE IF EXISTS main_user CASCADE ;

CREATE TABLE IF NOT EXISTS main_user(
  id SERIAL NOT NULL PRIMARY KEY ,
  email VARCHAR (255) UNIQUE NOT NULL ,
  password VARCHAR (255) NOT NULL
);

INSERT INTO main_user (email, password) VALUES ('exist@e.ru', '$2a$10$rXn4xiRPY45wJVi39KAm.eKElUDcBQI4b58sqiEjrTXaTFrRn5nOW');
