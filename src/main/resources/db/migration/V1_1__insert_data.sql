-- SOURCES
INSERT INTO inappassistant.source (id, name, link) VALUES (1, 'ForeFlight Mobile Guide', 'https://cloudfront.foreflight.com/docs/ff/14.4.1/ForeFlightMobile%20Pilot%27s%20Guide%20v14.4.pdf');
INSERT INTO inappassistant.source (id, name, link) VALUES (2, 'ForeFlight Support Center', 'https://foreflight.com/support/support-center/');
INSERT INTO inappassistant.source (id, name, link) VALUES (3, 'ForeFlight YouTube Channel', 'https://www.youtube.com/c/foreflight');
INSERT INTO inappassistant.source (id, name, link) VALUES (4, 'ForeFlight Video Library', 'https://foreflight.com/support/video-library/');
--INSERT INTO inappassistant.source (id, name, link) VALUES (, '', '');


-- RESOURCES
INSERT INTO inappassistant.resource (id, name, link, source_id) VALUES (2, 'Traffic in ForeFlight Mobile Guide', 'https://cloudfront.foreflight.com/docs/ff/14.4.1/ForeFlightMobile%20Pilot%27s%20Guide%20v14.4.pdf?_ga=2.116183110.1338571124.1657573675-139986742.1652281831#page=95', 1);
INSERT INTO inappassistant.resource (id, name, link, source_id) VALUES (3, 'Traffic Support Center', 'https://foreflight.com/support/support-center/category/about-foreflight-mobile/4406036663063', 2);
INSERT INTO inappassistant.resource (id, name, link, source_id) VALUES (4, 'Traffic Video', 'https://www.youtube.com/watch?v=OvZRBd4HXp4', 3);
INSERT INTO inappassistant.resource (id, name, link, source_id) VALUES (5, 'Radar in ForeFlight Mobile Guide', 'https://cloudfront.foreflight.com/docs/ff/14.4.1/ForeFlightMobile%20Pilot%27s%20Guide%20v14.4.pdf?_ga=2.116183110.1338571124.1657573675-139986742.1652281831#page=82', 1);
INSERT INTO inappassistant.resource (id, name, link, source_id) VALUES (6, 'Radar Support Center', 'https://foreflight.com/support/support-center/category/?search=Radar&page=1', 2);
INSERT INTO inappassistant.resource (id, name, link, source_id) VALUES (7, 'Radar Video', 'https://foreflight.com/support/video-library/search/?q=Radar', 4);
INSERT INTO inappassistant.resource (id, name, link, source_id) VALUES (8, 'Satellite in ForeFlight Mobile Guide', 'https://cloudfront.foreflight.com/docs/ff/14.4.1/ForeFlightMobile%20Pilot%27s%20Guide%20v14.4.pdf?_ga=2.116183110.1338571124.1657573675-139986742.1652281831#page=255', 1);
INSERT INTO inappassistant.resource (id, name, link, source_id) VALUES (9, 'Satellite Support Center', 'https://foreflight.com/support/support-center/category/?search=Satellite&page=1', 2);
INSERT INTO inappassistant.resource (id, name, link, source_id) VALUES (10, 'Satellite Video', 'https://foreflight.com/support/video-library/search/?q=Satellite', 4);
INSERT INTO inappassistant.resource (id, name, link, source_id) VALUES (11, 'Air/Segment in ForeFlight Mobile Guide', 'https://cloudfront.foreflight.com/docs/ff/14.4.1/ForeFlightMobile%20Pilot%27s%20Guide%20v14.4.pdf?_ga=2.116183110.1338571124.1657573675-139986742.1652281831#page=89', 1);
INSERT INTO inappassistant.resource (id, name, link, source_id) VALUES (12, 'Air/Segment Support Center', 'https://foreflight.com/support/support-center/category/?search=sigmet&page=1', 2);
-- INSERT INTO inappassistant.resource (id, name, link, source_id) VALUES (, '', '', );


-- MENU CHOICES
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (4, 'Airports', null);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (5, 'Maps', null);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (6, 'Plates', null);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (7, 'Documents', null);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (8, 'Imagery', null);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (9, 'Flights', null);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (10, 'ScratchPads', null);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (11, 'Checklist', null);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (12, 'Logbook', null);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (13, 'Charts/Layers', 5);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (14, 'FPL', 5);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (15, 'Settings', 5);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (16, 'Synthetic Vision', 5);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (17, 'Instruments', 5);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (18, 'Favorites', 5);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (19, 'Charts', 13);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (20, 'Layers', 13);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (21, 'Radar', 20);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (22, 'Satellite', 20);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (24, 'Air/Segment', 20);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (25, 'TFRs', 20);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (26, 'Flight Category', 20);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (27, 'Surface Wind', 20);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (28, 'Winds Aloft', 20);
INSERT INTO inappassistant.menuchoice (id, name, parent_id) VALUES (23, 'Traffic', 20);


-- MENU CHOICE + RESOURCE RELATIONS
INSERT INTO inappassistant.menuchoice_resource (menuchoice_id, resource_id) VALUES (23, 2);
INSERT INTO inappassistant.menuchoice_resource (menuchoice_id, resource_id) VALUES (23, 3);
INSERT INTO inappassistant.menuchoice_resource (menuchoice_id, resource_id) VALUES (23, 4);
INSERT INTO inappassistant.menuchoice_resource (menuchoice_id, resource_id) VALUES (21, 5);
INSERT INTO inappassistant.menuchoice_resource (menuchoice_id, resource_id) VALUES (21, 6);
INSERT INTO inappassistant.menuchoice_resource (menuchoice_id, resource_id) VALUES (21, 7);
INSERT INTO inappassistant.menuchoice_resource (menuchoice_id, resource_id) VALUES (22, 8);
INSERT INTO inappassistant.menuchoice_resource (menuchoice_id, resource_id) VALUES (22, 9);
INSERT INTO inappassistant.menuchoice_resource (menuchoice_id, resource_id) VALUES (22, 10);
INSERT INTO inappassistant.menuchoice_resource (menuchoice_id, resource_id) VALUES (24, 11);
INSERT INTO inappassistant.menuchoice_resource (menuchoice_id, resource_id) VALUES (24, 12);
