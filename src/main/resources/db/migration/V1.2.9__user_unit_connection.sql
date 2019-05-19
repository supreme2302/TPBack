CREATE TABLE user_unit(
      id SERIAL NOT NULL PRIMARY KEY,
      status citext,
      student_id INTEGER REFERENCES student(id),
      unit_id INTEGER REFERENCES unit(id)

)