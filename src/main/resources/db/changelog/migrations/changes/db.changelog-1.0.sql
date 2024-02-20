CREATE TABLE IF NOT EXISTS person
(
    id BIGSERIAL PRIMARY KEY,
    user_profile_id BIGINT unique,
    login varchar(64) NOT NULL UNIQUE,
    password varchar(128) default '{noop}123',
    role varchar(64) not null default 'USER'
);