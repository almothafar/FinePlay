create sequence hibernate_sequence start 1 increment 1
create table CHANGE_USERS (id int8 not null, code varchar(64) not null, expireDateTime timestamp not null, newUserId varchar(256) not null, userId varchar(256) not null, primary key (id))
create table COMPANIES (id int8 not null, temp int8 not null, updateDateTime timestamp not null, version timestamp, primary key (id))
create table COMPANIES_COMPANY_NAMES (Company_id int8 not null, names_company_Id int8 not null, names_locale varchar(255) not null, locale varchar(255) not null, primary key (Company_id, locale))
create table COMPANY_NAMES (company_Id int8 not null, locale varchar(255) not null, name varchar(255) not null, primary key (company_Id, locale))
create table DATETIMES (user_Id int8 not null, date date, dateTime timestamp, time time, primary key (user_Id))
create table INQUIRIES (id int8 not null, content varchar(1000) not null, dateTime timestamp not null, locale varchar(255) not null, name varchar(64), title varchar(64) not null, type varchar(255) not null, userId varchar(256) not null, primary key (id))
create table ORGANIZATION_UNIT_NAMES (locale varchar(255) not null, organizationUnit_Id int8 not null, name varchar(255) not null, primary key (locale, organizationUnit_Id))
create table ORGANIZATION_UNITS (id int8 not null, sortOrder int8, updateDateTime timestamp not null, version timestamp, organization_id int8 not null, PARENT_ID int8, primary key (id))
create table ORGANIZATION_UNITS_ORGANIZATION_UNIT_NAMES (OrganizationUnit_id int8 not null, names_locale varchar(255) not null, names_organizationUnit_Id int8 not null, locale varchar(255) not null, primary key (OrganizationUnit_id, locale))
create table ORGANIZATIONS (id int8 not null, updateDateTime timestamp not null, version timestamp, company_id int8 not null, primary key (id))
create table REGIST_USERS (id int8 not null, code varchar(64) not null, expireDateTime timestamp not null, hashedPassword varchar(60) not null, locale varchar(255) not null, userId varchar(256) not null, zoneId varchar(255) not null, primary key (id))
create table RESET_USERS (id int8 not null, code varchar(64) not null, expireDateTime timestamp not null, userId varchar(256) not null, primary key (id))
create table STRICT_DATETIMES (user_Id int8 not null, dateTime timestamp, primary key (user_Id))
create table USER_ROLES (USER_ID int8 not null, ROLE varchar(255) not null, primary key (USER_ID, ROLE))
create table USERS (id int8 not null, expireDateTime timestamp not null, hashedPassword varchar(60) not null, locale varchar(255) not null, signInDateTime timestamp, signOutDateTime timestamp, theme varchar(255) not null, updateDateTime timestamp not null, userId varchar(256) not null, version timestamp, zoneId varchar(255) not null, COMPANY_ID int8, primary key (id))
create index IDXktdkurexwv3uxufg7vru7np5s on COMPANIES (id)
alter table COMPANIES_COMPANY_NAMES add constraint UK_iesn9bki7ydmkpr1n55aexfne unique (names_company_Id, names_locale)
create index IDX1tpvc9ive4udpna3chg5aw07f on COMPANY_NAMES (company_Id, locale)
create index IDXah93ixt4cvqik2jmrnl9e65mk on DATETIMES (user_Id)
create index IDX8onnr9rxv6ljsw9c4qvf3t66t on INQUIRIES (id)
create index IDXetd0b080e385fr04pv351o821 on ORGANIZATION_UNIT_NAMES (organizationUnit_Id, locale)
create index IDXskx418hmltv8cn8gwt25wveho on ORGANIZATION_UNITS (id)
create index IDXduyb4ul8u6j6y0wsdvkuukly6 on ORGANIZATION_UNITS (organization_id)
alter table ORGANIZATION_UNITS add constraint UKejy8rsqnmvdocs3wy7mtbo8ld unique (id, organization_id)
alter table ORGANIZATION_UNITS_ORGANIZATION_UNIT_NAMES add constraint UK_5hiq31t2io2yup2ul08dabu6q unique (names_locale, names_organizationUnit_Id)
create index IDXje9qh8x6dwp148liub4c17xmv on ORGANIZATIONS (id)
create index IDXqbny9kcakqery86xj4jt7mm66 on ORGANIZATIONS (company_id)
alter table ORGANIZATIONS add constraint UK8wgif8llthydveottfhshbwdu unique (id, company_id)
alter table ORGANIZATIONS add constraint UK_1akjfyqylxqvygx3326h6j2c unique (company_id)
create index IDX6b0b990kcx631hn7fv8nsj9x1 on STRICT_DATETIMES (user_Id)
create index IDX207cn8vnfsr9hdx9f0a9y2hlj on USERS (userId)
alter table USERS add constraint UKqiqomcfkvxicts5dvpm0swvvl unique (id, userId)
alter table USERS add constraint UK_207cn8vnfsr9hdx9f0a9y2hlj unique (userId)
alter table COMPANIES_COMPANY_NAMES add constraint FK7dplmqp7bb1m74fik1aoxy0yq foreign key (names_company_Id, names_locale) references COMPANY_NAMES
alter table COMPANIES_COMPANY_NAMES add constraint FKn3qhtm3r4eu9cd78si05xrv5h foreign key (Company_id) references COMPANIES
alter table ORGANIZATION_UNITS add constraint FKc36lxm5lc90vm1kque8vr7cyd foreign key (organization_id) references ORGANIZATIONS
alter table ORGANIZATION_UNITS add constraint FKaxl5lltie4tsoy0wug3btb5hl foreign key (PARENT_ID) references ORGANIZATION_UNITS
alter table ORGANIZATION_UNITS_ORGANIZATION_UNIT_NAMES add constraint FKpqciiy5y9smeeod0xvanq6wfg foreign key (names_locale, names_organizationUnit_Id) references ORGANIZATION_UNIT_NAMES
alter table ORGANIZATION_UNITS_ORGANIZATION_UNIT_NAMES add constraint FKciddj53m25d6p61rhine0fvbo foreign key (OrganizationUnit_id) references ORGANIZATION_UNITS
alter table ORGANIZATIONS add constraint FKpl7c9r2u24rjr6pqmrcd9mxdq foreign key (company_id) references COMPANIES
alter table USER_ROLES add constraint FKk3qtke64s9k5pv5hoq6yyq7py foreign key (USER_ID) references USERS
alter table USERS add constraint FKdeidj66mcx6k8vsoflveak3gj foreign key (COMPANY_ID) references COMPANIES
