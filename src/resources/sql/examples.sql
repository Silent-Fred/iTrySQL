/*
 * Copyright (c) 2013, Michael Kühweg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
DROP TABLE werk IF EXISTS;
DROP TABLE person IF EXISTS;
DROP TABLE ort IF EXISTS;
DROP TABLE epoche IF EXISTS;
--
-- ort
--
CREATE TABLE ort (ort VARCHAR(20) PRIMARY KEY, name VARCHAR(80), alt_min INTEGER, alt_max INTEGER, koordinaten VARCHAR(20) );
INSERT INTO ort (ort, name, alt_min, alt_max, koordinaten) VALUES ('FR00001', 'Paris', 28, 131, 'N48D51ME02D21M');
INSERT INTO ort (ort, name, alt_min, alt_max, koordinaten) VALUES ('EN01196', 'Stratford-upon-Avon', 72, NULL, 'N52D11MW01D42M');
INSERT INTO ort (ort, name, alt_min, alt_max, koordinaten) VALUES ('IT00001', 'Roma', 37, NULL, 'N41D53ME12D29M');
INSERT INTO ort (ort, name, alt_min, alt_max, koordinaten) VALUES ('US74840', 'Tupelo MS', 85, NULL, 'N34D16MW88D44M');
INSERT INTO ort (ort, name, alt_min, alt_max, koordinaten) VALUES ('US48000', 'Memphis TN', 78, NULL, 'N35D09MW90D03M');
INSERT INTO ort (ort, name, alt_min, alt_max, koordinaten) VALUES ('DE00001', 'Berlin', 34, 115, 'N52D31ME13D24M');
INSERT INTO ort (ort, name, alt_min, alt_max, koordinaten) VALUES ('EN00001', 'London', 15, NULL, 'N51D31MW00D07M');
--
-- person
--
CREATE TABLE person (persnr INTEGER PRIMARY KEY, vorname VARCHAR(80), name VARCHAR(80), wohnort VARCHAR(20) REFERENCES ort(ort), geburtsort VARCHAR(20) REFERENCES ort(ort), geburtstag DATE, mutter_persnr INTEGER REFERENCES person(persnr));
INSERT INTO person (persnr, vorname, name, wohnort, geburtsort, geburtstag, mutter_persnr) VALUES (1622, 'Jean-Baptiste', 'Poquelin', 'FR00001', 'FR00001', '1622-01-15', NULL);
INSERT INTO person (persnr, vorname, name, wohnort, geburtsort, geburtstag, mutter_persnr) VALUES (1694, 'François-Marie', 'Arouet', 'FR00001', 'FR00001', '1694-11-21', NULL);
INSERT INTO person (persnr, vorname, name, wohnort, geburtsort, geburtstag, mutter_persnr) VALUES (1564, 'William', 'Shakespeare', 'EN01196', 'EN01196', '1564-04-26', NULL);
INSERT INTO person (persnr, vorname, name, wohnort, geburtsort, geburtstag, mutter_persnr) VALUES (1912, 'Gladys Love', 'Presley', NULL, NULL, '1912-04-25', NULL);
INSERT INTO person (persnr, vorname, name, wohnort, geburtsort, geburtstag, mutter_persnr) VALUES (1935, 'Elvis Aaron', 'Presley', 'US48000', 'US74840', '1935-01-08', 1912);
INSERT INTO person (persnr, vorname, name, wohnort, geburtsort, geburtstag, mutter_persnr) VALUES (1888, 'Irving', 'Berlin', NULL, NULL, '1888-05-11', NULL);
INSERT INTO person (persnr, vorname, name, wohnort, geburtsort, geburtstag, mutter_persnr) VALUES (1876, 'Jack', 'London', NULL, NULL, '1876-01-12', NULL);
--
-- werk
--
CREATE TABLE werk (werk INTEGER PRIMARY KEY, titel VARCHAR(80), untertitel VARCHAR(80), autor INTEGER REFERENCES person(persnr), sprache VARCHAR(2), veroeffentlichung DATE);
INSERT INTO werk (werk, titel, untertitel, autor, sprache, veroeffentlichung) VALUES (1, 'The Taming of the Shrew', NULL, 1564,'EN', '1594-01-01');
INSERT INTO werk (werk, titel, untertitel, autor, sprache, veroeffentlichung) VALUES (2, 'Romeo and Juliet', 'The Most Excellent and Lamentable Tragedy Of Romeo and Juliet', 1564,'EN', '1597-01-01');
INSERT INTO werk (werk, titel, untertitel, autor, sprache, veroeffentlichung) VALUES (3, 'Le Malade imaginaire', NULL, 1622, 'FR', '1673-02-10');
INSERT INTO werk (werk, titel, untertitel, autor, sprache, veroeffentlichung) VALUES (4, 'Hound Dog', NULL, 1935, 'EN', '1956-06-05');
INSERT INTO werk (werk, titel, untertitel, autor, sprache, veroeffentlichung) VALUES (5, 'White Christmas', NULL, 1888, 'EN', '1941-12-25');
INSERT INTO werk (werk, titel, untertitel, autor, sprache, veroeffentlichung) VALUES (6, 'Martin Eden', NULL, 1876, 'EN', '1909-01-01');
--
-- epoche
--
CREATE TABLE epoche (epoche INTEGER PRIMARY KEY, bezeichnung VARCHAR(80), beginn DATE, ende DATE);
INSERT INTO epoche (epoche, bezeichnung, beginn, ende) VALUES (1, 'Renaissance', '1470-01-01', '1611-12-31');
INSERT INTO epoche (epoche, bezeichnung, beginn, ende) VALUES (2, 'Barock', '1600-01-01', '1770-12-31');
INSERT INTO epoche (epoche, bezeichnung, beginn, ende) VALUES (3, 'Aufklärung', '1720-01-01', '1800-12-31');
INSERT INTO epoche (epoche, bezeichnung, beginn, ende) VALUES (4, 'Sturm und Drang', '1769-01-01', '1785-12-31');
INSERT INTO epoche (epoche, bezeichnung, beginn, ende) VALUES (5, 'Klassik', '1786-01-01', '1832-12-31');
INSERT INTO epoche (epoche, bezeichnung, beginn, ende) VALUES (6, 'Romantik', '1795-01-01', '1848-12-31');
INSERT INTO epoche (epoche, bezeichnung, beginn, ende) VALUES (7, 'Biedermeier', '1815-01-01', '1850-12-31');
INSERT INTO epoche (epoche, bezeichnung, beginn, ende) VALUES (8, 'Realismus', '1850-01-01', '1890-12-31');
INSERT INTO epoche (epoche, bezeichnung, beginn, ende) VALUES (9, 'Moderne', '1890-01-01', '1980-12-31');
INSERT INTO epoche (epoche, bezeichnung, beginn, ende) VALUES (10, 'Naturalismus', '1880-01-01', '1900-12-31');
INSERT INTO epoche (epoche, bezeichnung, beginn, ende) VALUES (11, 'Expressionismus', '1905-01-01', '1925-12-31');
INSERT INTO epoche (epoche, bezeichnung, beginn, ende) VALUES (12, 'Postmoderne', '1980-01-01', NULL);
--
-- Ende
--
COMMIT;
