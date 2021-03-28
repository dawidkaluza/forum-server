SET TIMEZONE = 'UTC';

INSERT INTO users (email, name, encoded_password, enabled, created_at)
VALUES ('mark@gmail.com', 'mark.kram', '$2y$10$V8oi03sIIcJY4AY6Tyq2c.qUfX8TLgA5pgtdTKsaFNCtOK2u6fQdO', true, now());
-- Password is: v3ryh4rdp4ssw0rd
