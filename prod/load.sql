INSERT INTO USERS (ID, USERID, HASHEDPASSWORD, LOCALE, ZONEID, THEME, EXPIREDATETIME, SIGNINDATETIME, SIGNOUTDATETIME, UPDATEDATETIME, VERSION) VALUES (1, 'admin@example.com', '$2a$10$mz/sIgVyNqibPNS5WCos8u3niuxNnOW0d4aAZgm21aqqTK39.66da', 'en-US', 'UTC', 'DEFAULT', '9999-12-31 23:59:59', NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
INSERT INTO USER_ROLES (USER_ID, ROLE) VALUES (1, 'ADMIN')
INSERT INTO USERS (ID, USERID, HASHEDPASSWORD, LOCALE, ZONEID, THEME, EXPIREDATETIME, SIGNINDATETIME, SIGNOUTDATETIME, UPDATEDATETIME, VERSION) VALUES (2, 'customer@example.com', '$2a$10$3tbEElVEZ8kMPsuU5f/qWOp1HkDVhvKEOk6rpQioA6nL3LXeVJ5Em', 'en-US', 'UTC', 'DEFAULT', '9999-12-31 23:59:59', NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
INSERT INTO USER_ROLES (USER_ID, ROLE) VALUES (2, 'CUSTOMER')
INSERT INTO USERS (ID, USERID, HASHEDPASSWORD, LOCALE, ZONEID, THEME, EXPIREDATETIME, SIGNINDATETIME, SIGNOUTDATETIME, UPDATEDATETIME, VERSION) VALUES (3, 'guest@example.com', '$2a$10$1UTEiFDg6nn.XOiWQMmQPuoqJ68RbyVo5dnVKAer328TdyKonLMyW', 'en-US', 'UTC', 'DEFAULT', '9999-12-31 23:59:59', NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
INSERT INTO USER_ROLES (USER_ID, ROLE) VALUES (3, 'GUEST')
INSERT INTO USERS (ID, USERID, HASHEDPASSWORD, LOCALE, ZONEID, THEME, EXPIREDATETIME, SIGNINDATETIME, SIGNOUTDATETIME, UPDATEDATETIME, VERSION) VALUES (4, 'adminjajp@example.com', '$2a$10$lhb6bb0FtTsjJXwUaPsSwOJre8br.5j7WSCw0h1xRbvUglsWrIGEy', 'ja-JP', 'Asia/Tokyo', 'DEFAULT', '9999-12-31 23:59:59', NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
INSERT INTO USER_ROLES (USER_ID, ROLE) VALUES (4, 'ADMIN')
