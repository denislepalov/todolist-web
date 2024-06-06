--liquibase formatted sql

--changeset lepdv:1
CREATE TABLE IF NOT EXISTS users (
    id bigserial PRIMARY KEY,
    username varchar(128) UNIQUE NOT NULL,
    password varchar(128) NOT NULL,
    full_name varchar(128),
    date_of_birth date,
    role varchar(20) NOT NULL,
    is_non_locked boolean NOT NULL,
    image varchar(128),
    create_at timestamp,
    modified_at timestamp
);


--changeset lepdv:2
CREATE TABLE IF NOT EXISTS task (
    id bigserial PRIMARY KEY,
    description text NOT NULL,
    date_of_creation date NOT NULL,
    due_date date,
    is_completed varchar(15),
    user_id int REFERENCES users(id) ON DELETE CASCADE,
    create_at timestamp,
    modified_at timestamp,
    created_by varchar(64),
    modified_by varchar(64)
);


--changeset lepdv:3
CREATE TABLE IF NOT EXISTS revision (
    id serial PRIMARY KEY,
    timestamp bigint,
    date_time timestamp,
    modified_by varchar(64)
);


--changeset lepdv:4
CREATE TABLE IF NOT EXISTS users_aud (
    id bigint NOT NULL,
    rev integer NOT NULL REFERENCES revision(id),
    revtype smallint,
    username varchar(128),
    password varchar(128),
    full_name varchar(128),
    date_of_birth date,
    role varchar(20) CHECK (role in ('USER','ADMIN')),
    is_non_locked boolean,
    image varchar(128),
    modified_at timestamp,
    PRIMARY KEY (rev, id)
);


--changeset lepdv:5
CREATE TABLE IF NOT EXISTS task_aud (
    id bigint NOT NULL,
    rev integer NOT NULL REFERENCES revision(id),
    revtype smallint,
    description text,
    date_of_creation date,
    due_date date,
    is_completed varchar(15),
    user_id bigint,
    modified_at timestamp,
    modified_by varchar(128),
    PRIMARY KEY (rev, id)
);















