CREATE TABLE IF NOT EXISTS
    "source"
(
    "id"   SERIAL not null PRIMARY KEY,
    "name" TEXT not null UNIQUE,
    "link" TEXT not null
);

CREATE TABLE IF NOT EXISTS
    "resource"
(
    "id"   SERIAL not null PRIMARY KEY,
    "name" TEXT not null UNIQUE,
    "link" TEXT not null,
    "source_id" INTEGER not null REFERENCES source,
    "public"    BOOLEAN not null DEFAULT false
);

CREATE TABLE IF NOT EXISTS
    "menuchoice"
(
    "id"        SERIAL not null PRIMARY KEY,
    "name"      TEXT not null UNIQUE,
    "parent_id" INTEGER null REFERENCES menuchoice,
    "public"    BOOLEAN not null DEFAULT false
);

CREATE TABLE IF NOT EXISTS
    "menuchoice_resource"
(
    "menuchoice_id" INTEGER not null REFERENCES menuchoice,
    "resource_id"   INTEGER not null REFERENCES resource,
    PRIMARY KEY (menuchoice_id, resource_id)
);

CREATE TABLE IF NOT EXISTS
    "metric"
(
    "id"               SERIAL not null PRIMARY KEY,
    "answer_found"     BOOLEAN not null,
    "timestamp"        TIMESTAMPTZ(0),
    "ticket_link"      TEXT,
    "user_name"        TEXT,
    "user_feedback"    TEXT
);

CREATE TABLE IF NOT EXISTS
    "metric_menuchoice"
(
    "metric_id" INTEGER not null REFERENCES metric,
    "menuchoice_id"   INTEGER not null REFERENCES menuchoice,
    PRIMARY KEY (metric_id, menuchoice_id)
);

CREATE TABLE IF NOT EXISTS
    "metric_resource"
(
    "metric_id" INTEGER not null REFERENCES metric,
    "resource_id"   INTEGER not null REFERENCES resource,
    PRIMARY KEY (metric_id, resource_id)
);