CREATE TABLE IF NOT EXISTS
    "resource"
(
    "id"   SERIAL not null PRIMARY KEY,
    "name" TEXT null UNIQUE,
    "link" TEXT null
);

CREATE TABLE IF NOT EXISTS
    "menuchoice"
(
    "id"        SERIAL not null PRIMARY KEY,
    "name"      TEXT null UNIQUE,
    "parent_id" INTEGER null REFERENCES menuchoice
);

CREATE TABLE IF NOT EXISTS
    "menuchoice_resource"
(
    "menuchoice_id" INTEGER not null REFERENCES menuchoice,
    "resource_id"   INTEGER not null REFERENCES resource,
    PRIMARY KEY (menuchoice_id, resource_id)
);