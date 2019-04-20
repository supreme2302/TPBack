ALTER TABLE course ADD COLUMN image_link CITEXT DEFAULT 'default_course.jpg';
ALTER TABLE school DROP COLUMN school_logo;
ALTER TABLE school ADD COLUMN image_link CITEXT DEFAULT 'default_school.jpg';
