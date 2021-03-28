SET TIMEZONE = 'UTC';

INSERT INTO users (email, name, encoded_password, enabled, created_at)
VALUES ('mark@gmail.com', 'mark.kram', '$v3ryh4rd3nc0d3dp4ssw0rd#', false, now());
