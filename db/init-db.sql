CREATE TABLE invite
(
  id SERIAL PRIMARY KEY NOT NULL,
  code VARCHAR(64) NOT NULL
);
CREATE UNIQUE INDEX invite_code_uindex ON invite (code);

CREATE TABLE "user"
(
  id SERIAL PRIMARY KEY NOT NULL,
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

CREATE TABLE book
(
  id SERIAL PRIMARY KEY NOT NULL,
  author VARCHAR(255) NOT NULL,
  title VARCHAR(255) NOT NULL,
  user_id INTEGER NOT NULL,
  status SMALLINT DEFAULT 1 NOT NULL,
  is_fiction BOOLEAN NOT NULL,
  CONSTRAINT book_user_fk FOREIGN KEY (user_id) REFERENCES "user" (id)
);
CREATE INDEX book_user_id_index ON book (user_id);

CREATE TABLE book_reading
(
  id SERIAL PRIMARY KEY NOT NULL,
  book_id INTEGER NOT NULL,
  status SMALLINT NOT NULL,
  start_month SMALLINT NOT NULL,
  start_year INTEGER NOT NULL,
  end_month SMALLINT NOT NULL,
  end_year INTEGER NOT NULL,
  is_reread BOOLEAN NOT NULL,
  CONSTRAINT book_reading_book_fk FOREIGN KEY (book_id) REFERENCES book (id)
);
CREATE INDEX book_reading_end_year_index ON book_reading (end_year);
