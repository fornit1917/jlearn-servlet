CREATE TABLE "user"
(
  id INTEGER PRIMARY KEY NOT NULL,
  email VARCHAR(128) NOT NULL,
  is_admin BOOLEAN DEFAULT false NOT NULL,
  is_active BOOLEAN DEFAULT false NOT NULL,
  hpassw VARCHAR(64) NOT NULL,
  create_date TIMESTAMP DEFAULT now() NOT NULL,
  auth_key VARCHAR(32),
  invite_id INTEGER,
  CONSTRAINT user_invite_fk FOREIGN KEY (invite_id) REFERENCES invite (id)
);
CREATE UNIQUE INDEX user_email_uindex ON "user" (email);
CREATE UNIQUE INDEX user_invite_id_uindex ON "user" (invite_id);

CREATE TABLE invite
(
  id INTEGER PRIMARY KEY NOT NULL,
  code VARCHAR(64) NOT NULL
);
CREATE UNIQUE INDEX invite_code_uindex ON invite (code);