DROP TABLE IF EXISTS USERS, CATEGORIES, EVENTS, COMPILATIONS, REQUESTS, COMPILATIONS_EVENTS;

CREATE TABLE IF NOT EXISTS USERS (
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name    VARCHAR(64) NOT NULL,
    email   VARCHAR(64) NOT NULL,

    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS CATEGORIES (
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name    VARCHAR(64) NOT NULL UNIQUE,

    CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS EVENTS (
    id                  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation          VARCHAR(512),
    title               VARCHAR(512),
    category_id         INTEGER,
    created_on          TIMESTAMP,
    description         VARCHAR,
    event_date          TIMESTAMP,
    initiator_id        INTEGER,
    lat                 FLOAT,
    lon                 FLOAT,
    paid                BOOLEAN,
    participant_limit   INTEGER,
    confirmed_requests  INTEGER,
    published_on        TIMESTAMP,
    request_moderation  BOOLEAN,
    state               VARCHAR(15) NOT NULL,

    CONSTRAINT FK_EVENTS_USERS FOREIGN KEY (initiator_id) REFERENCES USERS (id),
    CONSTRAINT FK_EVENTS_CATEGORIES FOREIGN KEY (category_id) REFERENCES CATEGORIES (id)
);

CREATE TABLE IF NOT EXISTS COMPILATIONS (
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title   VARCHAR(512),
    pinned  BOOLEAN
);

CREATE TABLE IF NOT EXISTS COMPILATIONS_EVENTS (
    id                BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    compilation_id    INTEGER,
    event_id          INTEGER,

    CONSTRAINT UQ_COMPILATIONS_EVENTS UNIQUE (compilation_id, event_id)
);

CREATE TABLE IF NOT EXISTS REQUESTS (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id INTEGER,
    requester INTEGER,
    created TIMESTAMP,
    status VARCHAR NOT NULL,

    CONSTRAINT FK_REQUESTS_EVENTS FOREIGN KEY (event_id) REFERENCES EVENTS (id),
    CONSTRAINT FK_REQUESTS_USERS FOREIGN KEY (requester) REFERENCES USERS (id),
    CONSTRAINT UQ_EVENT_REQUESTER UNIQUE (event_id, requester)
);