--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.1
-- Dumped by pg_dump version 9.6.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: book; Type: TABLE; Schema: public; Owner: books
--

CREATE TABLE book (
  id integer NOT NULL,
  author character varying(255) NOT NULL,
  title character varying(255) NOT NULL,
  user_id integer NOT NULL,
  status smallint DEFAULT 1 NOT NULL,
  is_fiction boolean NOT NULL
);


ALTER TABLE book OWNER TO books;

--
-- Name: book_id_seq; Type: SEQUENCE; Schema: public; Owner: books
--

CREATE SEQUENCE book_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE book_id_seq OWNER TO books;

--
-- Name: book_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: books
--

ALTER SEQUENCE book_id_seq OWNED BY book.id;


--
-- Name: book_reading; Type: TABLE; Schema: public; Owner: books
--

CREATE TABLE book_reading (
  id integer NOT NULL,
  book_id integer NOT NULL,
  status smallint NOT NULL,
  start_month smallint,
  start_year integer,
  end_month smallint,
  end_year integer,
  is_reread boolean NOT NULL
);


ALTER TABLE book_reading OWNER TO books;

--
-- Name: book_reading_id_seq; Type: SEQUENCE; Schema: public; Owner: books
--

CREATE SEQUENCE book_reading_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE book_reading_id_seq OWNER TO books;

--
-- Name: book_reading_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: books
--

ALTER SEQUENCE book_reading_id_seq OWNED BY book_reading.id;


--
-- Name: invite; Type: TABLE; Schema: public; Owner: books
--

CREATE TABLE invite (
  id integer NOT NULL,
  code character varying(64) NOT NULL
);


ALTER TABLE invite OWNER TO books;

--
-- Name: invite_id_seq; Type: SEQUENCE; Schema: public; Owner: books
--

CREATE SEQUENCE invite_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE invite_id_seq OWNER TO books;

--
-- Name: invite_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: books
--

ALTER SEQUENCE invite_id_seq OWNED BY invite.id;


--
-- Name: user; Type: TABLE; Schema: public; Owner: books
--

CREATE TABLE "user" (
  id integer NOT NULL,
  email character varying(128) NOT NULL,
  is_admin boolean DEFAULT false NOT NULL,
  is_active boolean DEFAULT false NOT NULL,
  hpassw character varying(64) NOT NULL,
  create_date timestamp without time zone DEFAULT now() NOT NULL,
  auth_key character varying(32),
  invite_id integer
);


ALTER TABLE "user" OWNER TO books;

--
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: books
--

CREATE SEQUENCE user_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE user_id_seq OWNER TO books;

--
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: books
--

ALTER SEQUENCE user_id_seq OWNED BY "user".id;


--
-- Name: book id; Type: DEFAULT; Schema: public; Owner: books
--

ALTER TABLE ONLY book ALTER COLUMN id SET DEFAULT nextval('book_id_seq'::regclass);


--
-- Name: book_reading id; Type: DEFAULT; Schema: public; Owner: books
--

ALTER TABLE ONLY book_reading ALTER COLUMN id SET DEFAULT nextval('book_reading_id_seq'::regclass);


--
-- Name: invite id; Type: DEFAULT; Schema: public; Owner: books
--

ALTER TABLE ONLY invite ALTER COLUMN id SET DEFAULT nextval('invite_id_seq'::regclass);


--
-- Name: user id; Type: DEFAULT; Schema: public; Owner: books
--

ALTER TABLE ONLY "user" ALTER COLUMN id SET DEFAULT nextval('user_id_seq'::regclass);


--
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: books
--

ALTER TABLE ONLY book
  ADD CONSTRAINT book_pkey PRIMARY KEY (id);


--
-- Name: book_reading book_reading_pkey; Type: CONSTRAINT; Schema: public; Owner: books
--

ALTER TABLE ONLY book_reading
  ADD CONSTRAINT book_reading_pkey PRIMARY KEY (id);


--
-- Name: invite invite_pkey; Type: CONSTRAINT; Schema: public; Owner: books
--

ALTER TABLE ONLY invite
  ADD CONSTRAINT invite_pkey PRIMARY KEY (id);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: books
--

ALTER TABLE ONLY "user"
  ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: book_reading_end_year_index; Type: INDEX; Schema: public; Owner: books
--

CREATE INDEX book_reading_end_year_index ON book_reading USING btree (end_year);


--
-- Name: book_user_id_index; Type: INDEX; Schema: public; Owner: books
--

CREATE INDEX book_user_id_index ON book USING btree (user_id);


--
-- Name: invite_code_uindex; Type: INDEX; Schema: public; Owner: books
--

CREATE UNIQUE INDEX invite_code_uindex ON invite USING btree (code);


--
-- Name: user_email_uindex; Type: INDEX; Schema: public; Owner: books
--

CREATE UNIQUE INDEX user_email_uindex ON "user" USING btree (email);


--
-- Name: user_invite_id_uindex; Type: INDEX; Schema: public; Owner: books
--

CREATE UNIQUE INDEX user_invite_id_uindex ON "user" USING btree (invite_id);


--
-- Name: book_reading book_reading_book_fk; Type: FK CONSTRAINT; Schema: public; Owner: books
--

ALTER TABLE ONLY book_reading
  ADD CONSTRAINT book_reading_book_fk FOREIGN KEY (book_id) REFERENCES book(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: book book_user_fk; Type: FK CONSTRAINT; Schema: public; Owner: books
--

ALTER TABLE ONLY book
  ADD CONSTRAINT book_user_fk FOREIGN KEY (user_id) REFERENCES "user"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: user user_invite_fk; Type: FK CONSTRAINT; Schema: public; Owner: books
--

ALTER TABLE ONLY "user"
  ADD CONSTRAINT user_invite_fk FOREIGN KEY (invite_id) REFERENCES invite(id);


--
-- PostgreSQL database dump complete
--

