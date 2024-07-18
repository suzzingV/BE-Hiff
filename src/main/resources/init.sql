ALTER TABLE hobby_tb ALTER COLUMN id RESTART WITH 200;
INSERT INTO hobby_tb (id, name, count) VALUES (1, '클라이밍', 5);
INSERT INTO hobby_tb (id, name, count) VALUES (2, '축구', 3);
INSERT INTO hobby_tb (id, name, count) VALUES (3, '농구', 7);