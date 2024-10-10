CREATE SCHEMA IF NOT EXISTS memes;

CREATE TABLE IF NOT EXISTS memes.used
(
    id                      BIGSERIAL NOT NULL PRIMARY KEY,
    date                    TIMESTAMP,
    link                    VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS memes.salary
(
    id                      BIGSERIAL NOT NULL PRIMARY KEY,
    chat_id                 VARCHAR(20),
    first_day               SMALLINT,
    second_day              SMALLINT
);

CREATE TABLE IF NOT EXISTS memes.scheduler
(
    id                      BIGSERIAL NOT NULL PRIMARY KEY,
    chat_id                 VARCHAR(20),
    type                    VARCHAR(20),
    day_week                SMALLINT,
    hour                    SMALLINT,
    minute                  SMALLINT
);




