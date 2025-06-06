    CREATE TABLE IF NOT EXISTS USERS (
        ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
        NAME varchar(250) NOT NULL,
        EMAIL varchar(254) NOT NULL,
        CONSTRAINT users_pk PRIMARY KEY (ID),
        CONSTRAINT email_uq UNIQUE (EMAIL)
    );


    CREATE TABLE IF NOT EXISTS CATEGORIES (
        ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
        NAME varchar(50) NOT NULL,
        CONSTRAINT categories_pk PRIMARY KEY (ID),
        CONSTRAINT name_uq UNIQUE (NAME)
    );

    CREATE TABLE IF NOT EXISTS LOCATIONS (
        ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
        LAT REAL,
        LON REAL,
        CONSTRAINT locations_pk PRIMARY KEY (ID)
    );

    CREATE TABLE IF NOT EXISTS EVENTS (
        ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
        ANNOTATION varchar NOT NULL,
        CATEGORY_ID BIGINT NOT NULL,
        CONFIRMED_REQUESTS BIGINT,
        CREATED_ON TIMESTAMP,
        DESCRIPTION varchar,
        EVENT_DATE TIMESTAMP NOT NULL,
        INITIATOR_ID BIGINT NOT NULL,
        LOCATION_ID BIGINT NOT NULL,
        PAID BOOLEAN NOT NULL,
        PARTICIPANT_LIMIT INT,
        PUBLISHED_ON TIMESTAMP,
        REQUEST_MODERATION BOOLEAN,
        EVENT_STATE varchar,
        TITLE varchar NOT NULL,
        CONSTRAINT category_fk FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORIES(ID),
        CONSTRAINT initiator_fk FOREIGN KEY (INITIATOR_ID) REFERENCES USERS(ID),
        CONSTRAINT location_fk FOREIGN KEY (LOCATION_ID) REFERENCES LOCATIONS(ID),
        CONSTRAINT events_pk PRIMARY KEY (ID)
    );
    --Индексы для таблицы EVENTS
    CREATE INDEX IF NOT EXISTS idx_EVENTS_CATEGORY_ID ON EVENTS (CATEGORY_ID);
    CREATE INDEX IF NOT EXISTS idx_EVENTS_LOCATION_ID ON EVENTS (LOCATION_ID);
    CREATE INDEX IF NOT EXISTS idx_EVENTS_INITIATOR_ID ON EVENTS (INITIATOR_ID);

    CREATE TABLE IF NOT EXISTS PARTICIPATION_REQUESTS (
        ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
        CREATED TIMESTAMP,
        EVENT_ID BIGINT,
        REQUESTER_ID BIGINT,
        REQUEST_STATUS varchar,
        CONSTRAINT requester_fk FOREIGN KEY (REQUESTER_ID) REFERENCES USERS(ID),
        CONSTRAINT event_fk FOREIGN KEY (EVENT_ID) REFERENCES EVENTS(ID),
        CONSTRAINT participation_pk PRIMARY KEY (ID)
    );
    --Индексы для таблицы PARTICIPATION_REQUESTS
    CREATE INDEX IF NOT EXISTS idx_PARTICIPATION_REQUESTS_EVENT_ID ON PARTICIPATION_REQUESTS (EVENT_ID);
    CREATE INDEX IF NOT EXISTS idx_PARTICIPATION_REQUESTS_REQUESTER_ID ON PARTICIPATION_REQUESTS (REQUESTER_ID);


    CREATE TABLE IF NOT EXISTS COMPILATIONS (
        ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
        PINNED BOOLEAN,
        TITLE varchar,
        CONSTRAINT compilations_pk PRIMARY KEY (ID)
    );

    CREATE TABLE IF NOT EXISTS COMPILATIONS_EVENTS (
        COMPILATIONS_ID BIGINT,
        EVENT_ID BIGINT,
        CONSTRAINT compilations_fk FOREIGN KEY (COMPILATIONS_ID) REFERENCES COMPILATIONS(ID),
        CONSTRAINT event_fk FOREIGN KEY (EVENT_ID) REFERENCES EVENTS(ID),
        PRIMARY KEY (COMPILATIONS_ID, EVENT_ID)
    );

        CREATE TABLE IF NOT EXISTS COMMENTS (
            ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
            TEXT VARCHAR NOT NULL,
            AUTHOR_ID BIGINT NOT NULL,
            EVENT_ID BIGINT NOT NULL,
            CREATED TIMESTAMP NOT NULL,
            CONSTRAINT AUTHOR_FK FOREIGN KEY (AUTHOR_ID) REFERENCES USERS(ID) ON DELETE CASCADE,
            CONSTRAINT EVENT_FK FOREIGN KEY (EVENT_ID) REFERENCES EVENTS(ID) ON DELETE CASCADE,
            PRIMARY KEY (ID)
        );
    --Индекс для таблицы COMMENTS
    CREATE INDEX IF NOT EXISTS idx_COMMENTS_EVENT_ID ON COMMENTS (EVENT_ID);