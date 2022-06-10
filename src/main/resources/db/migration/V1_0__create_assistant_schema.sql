CREATE TYPE resource_source AS ENUM('PilotGuide', 'Jira', 'Confluence');

CREATE TABLE IF NOT EXISTS
    "resource"
(
    "id"   SERIAL not null PRIMARY KEY,
    "name" TEXT not null UNIQUE,
    "link" TEXT null,
    "source" resource_source not null
);

CREATE TABLE IF NOT EXISTS
    "menuchoice"
(
    "id"        SERIAL not null PRIMARY KEY,
    "name"      TEXT not null UNIQUE,
    "parent_id" INTEGER null REFERENCES menuchoice
);

CREATE TABLE IF NOT EXISTS
    "menuchoice_resource"
(
    "menuchoice_id" INTEGER not null REFERENCES menuchoice,
    "resource_id"   INTEGER not null REFERENCES resource,
    PRIMARY KEY (menuchoice_id, resource_id)
);