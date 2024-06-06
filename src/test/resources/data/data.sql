DELETE FROM users;
INSERT INTO users (id, username, password, full_name, date_of_birth, role, is_non_locked, image)
VALUES
(1, 'Admin', '$2a$10$QqbD8Up32CATm2DSVVjIDea08KuxC/RL9.9SFVcMP6FW5nHGl5PIG', 'Admin', '1990-01-01', 'ADMIN', true, null),
(2, 'Ivan', '$2a$10$JfoL9fN.fl4DtP.mUQAF0..OzWxIE2ffAq7nWY4XtXKazpYCd5HSK', 'Ivanov Ivan', '2000-01-01', 'USER', true, 'lock.png'),
(3, 'Katya', '$2a$10$f0A/1pjXviu82xuuG5AKreDlb0tiAoWzBMnbphJz1oPNkzaZ2omRe', 'Petrova Katya', '2010-01-01', 'USER', true, null);

--(for H2 database)
ALTER TABLE users ALTER COLUMN id RESTART WITH (SELECT MAX(id) FROM users) + 1;

--(for postgresql database)
-- SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));


DELETE FROM task;
INSERT INTO task (id, description, date_of_creation, due_date, is_completed, user_id)
VALUES
(1, 'Ivan task1', '2023-05-01', '2025-05-11', 'Not completed', (SELECT id FROM users WHERE username = 'Ivan')),
(2, 'Ivan task2', '2023-05-10', '2025-05-20', 'Not completed', (SELECT id FROM users WHERE username = 'Ivan')),
(3, 'Ivan task3', '2023-05-20', '2025-05-30', 'Not completed', (SELECT id FROM users WHERE username = 'Ivan')),
(4, 'Katya task1', '2023-07-01', '2025-05-11', 'Not completed', (SELECT id FROM users WHERE username = 'Katya')),
(5, 'Katya task2', '2023-07-10', '2025-05-20', 'Not completed', (SELECT id FROM users WHERE username = 'Katya')),
(6, 'Katya task3', '2023-07-20', '2025-05-30', 'Not completed', (SELECT id FROM users WHERE username = 'Katya'));

--(for H2 database)
ALTER TABLE task ALTER COLUMN id RESTART WITH (SELECT MAX(id) FROM task) + 1;

--(for postgresql database)
-- SELECT SETVAL('task_id_seq', (SELECT MAX(id) FROM task));