ALTER TABLE customer ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER';

INSERT INTO customer (name, email, age, gender, password, role)
VALUES (
           'Admin User',
           'admin@bank.com',
           30,
           'MALE',
           '$2aJ17%wNqvVvW9qZ9k5oGgXkLgqeO9V4fHjT8zB2rN6eK8rY1oP0mU1qG4a',
           'ROLE_ADMIN'
       );