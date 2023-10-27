INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER'), (3, 'ROLE_GUEST');
INSERT INTO users (enabled, id, first_name, last_name, email, password) 
VALUES (true, 1, 'Юрий', 'Петров', 'admin@gmail.com', '$2y$10$kbBc2/YyhalAHuK.SRiFPeu/iENCtVjUS9sK4A3/4b5EXdgqcj0cy');
INSERT INTO users_role VALUES (1, 1);
