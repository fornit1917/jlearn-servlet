CREATE TABLE "user"
(
  id INTEGER PRIMARY KEY NOT NULL,
  email VARCHAR(128) NOT NULL,
  is_admin BOOLEAN DEFAULT false NOT NULL,
  is_active BOOLEAN DEFAULT false NOT NULL,
  hpassw VARCHAR(64) NOT NULL,
  auth_key VARCHAR(32) NOT NULL,
  create_date TIMESTAMP DEFAULT now() NOT NULL
);
CREATE UNIQUE INDEX user_email_uindex ON "user" (email);

CREATE TABLE invite
(
  code VARCHAR(32) PRIMARY KEY NOT NULL,
  user_id INTEGER,
  CONSTRAINT invite_user_id_fk FOREIGN KEY (user_id) REFERENCES "user" (id)
);
CREATE UNIQUE INDEX invite_code_uindex ON invite (code);
CREATE UNIQUE INDEX invite_user_id_uindex ON invite (user_id);
