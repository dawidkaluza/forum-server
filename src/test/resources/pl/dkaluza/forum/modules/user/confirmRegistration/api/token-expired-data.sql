SET TIMEZONE = 'UTC';

INSERT INTO users (email, name, encoded_password, enabled)
VALUES ('mark@gmail.com', 'mark.kram', 'encodedPassword$321', true);

INSERT INTO confirm_registration_mail_receiver (email)
VALUES ('mark@gmail.com');

INSERT INTO confirm_registration_mail (receiver_id, sent_at)
VALUES (
    (SELECT id FROM confirm_registration_mail_receiver WHERE email = 'mark@gmail.com'),
    now()
);

INSERT INTO confirm_registration_token (user_id, token, expires_at)
VALUES (
    (SELECT id FROM users WHERE email = 'mark@gmail.com'),
    '123xyz',
    now() - interval '1 minutes'
);
