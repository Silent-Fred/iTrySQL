/*
 * Copyright (c) 2015, Michael Kühweg
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
SELECT '/*' FROM INFORMATION_SCHEMA.SYSTEM_USERS
WHERE user_name = CURRENT_USER AND admin <> true;

SELECT 'DROP USER ' || user_name || ' CASCADE;' FROM INFORMATION_SCHEMA.SYSTEM_USERS
WHERE user_name = 'JOHN_DOE' AND user_name <> CURRENT_USER;

VALUES ('CREATE USER JOHN_DOE PASSWORD ''geheim'';');
VALUES ('CREATE SCHEMA JOHN_DOE AUTHORIZATION JOHN_DOE;';

VALUES ('DROP TABLE john_doe.ort IF EXISTS;');
VALUES ('CREATE TABLE john_doe.ort (ort VARCHAR(20) PRIMARY KEY, name VARCHAR(80), alt_min INTEGER, alt_max INTEGER, koordinaten VARCHAR(20) );');
VALUES ('COMMENT ON TABLE john_doe.ort IS ''Secret places'';');
VALUES ('INSERT INTO john_doe.ort (ort, name, alt_min, alt_max, koordinaten) VALUES (''US00051'', ''Area 51 (Groom Lake)'', 1401, null, ''N37D14MW115D48M'');');
VALUES ('INSERT INTO john_doe.ort (ort, name, alt_min, alt_max, koordinaten) VALUES (''US40121'', ''Fort Knox'', 230, null, ''N37D53MW85D58M'');');
VALUES ('COMMIT;');

VALUES ('CHECKPOINT;');

SELECT '*/' FROM INFORMATION_SCHEMA.SYSTEM_USERS
WHERE user_name = CURRENT_USER AND admin <> true;
