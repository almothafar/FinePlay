create sequence hibernate_sequence start 1 increment 1
create table CHANGE_USERS (id int8 not null, code varchar(64) not null, expireDateTime timestamp not null, newUserId varchar(256) not null, userId varchar(256) not null, primary key (id))
create table COMPANIES (id int8 not null, updateDateTime timestamp not null, primary key (id))
create table COMPANY_NAMES (COMPANY_ID int8 not null, NAME varchar(255) not null, LOCALE varchar(255) not null, primary key (COMPANY_ID, LOCALE))
create table DATETIMES (user_Id int8 not null, date date, dateTime timestamp, time time, primary key (user_Id))
create table INQUIRIES (id int8 not null, content varchar(1000) not null, dateTime timestamp not null, locale varchar(255) not null, name varchar(64), title varchar(64) not null, type varchar(255) not null, userId varchar(256) not null, primary key (id))
create table ORGANIZATION_UNIT_NAMES (ORGANIZATION_UNIT_ID int8 not null, NAME varchar(255) not null, LOCALE varchar(255) not null, primary key (ORGANIZATION_UNIT_ID, LOCALE))
create table ORGANIZATION_UNITS (id int8 not null, sortOrder int8, updateDateTime timestamp not null, organization_id int8 not null, PARENT_ID int8, primary key (id))
create table ORGANIZATIONS (id int8 not null, updateDateTime timestamp not null, company_id int8 not null, primary key (id))
create table REGIST_USERS (id int8 not null, code varchar(64) not null, expireDateTime timestamp not null, hashedPassword varchar(64) not null, locale varchar(255) not null, salt varchar(20) not null, userId varchar(256) not null, zoneId varchar(255) not null, primary key (id))
create table RESET_USERS (id int8 not null, code varchar(64) not null, expireDateTime timestamp not null, userId varchar(256) not null, primary key (id))
create table USER_ROLES (USER_ID int8 not null, ROLE varchar(255) not null, primary key (USER_ID, ROLE))
create table USERS (id int8 not null, expireDateTime timestamp not null, hashedPassword varchar(64) not null, locale varchar(255) not null, salt varchar(20) not null, signInDateTime timestamp, signOutDateTime timestamp, theme varchar(255) not null, updateDateTime timestamp not null, userId varchar(256) not null, zoneId varchar(255) not null, COMPANY_ID int8, primary key (id))
create index IDXdak6lbdx5sy6ccoo9pfx7qmh on COMPANIES (id)
create index IDXll1ocgmpjmxlscdrwsai4yqr1 on DATETIMES (user_Id)
create index IDXqwihetc0vsgc2d7rr084cdbkm on INQUIRIES (id)
create index IDXq7bt13gab7qw4sd581l6sojqu on ORGANIZATION_UNITS (id)
create index IDXduyb4ul8u6j6y0wsdvkuukly6 on ORGANIZATION_UNITS (organization_id)
alter table ORGANIZATION_UNITS add constraint UKheqgow0vceh46v6ptdl4j2jji unique (id, organization_id)
create index IDXny16blitg51dywfc2wxcmnhry on ORGANIZATIONS (id)
create index IDXqbny9kcakqery86xj4jt7mm66 on ORGANIZATIONS (company_id)
alter table ORGANIZATIONS add constraint UK8qnkl859g8hkp9l1ucowy3ud1 unique (id, company_id)
alter table ORGANIZATIONS add constraint UK_1akjfyqylxqvygx3326h6j2c unique (company_id)
create index IDXt9w5fsbas7m39pfy0d0soffwd on USERS (userId)
alter table USERS add constraint UKl99u5ervugmw7jijcxx57ykke unique (id, userId)
alter table USERS add constraint UK_207cn8vnfsr9hdx9f0a9y2hlj unique (userId)
alter table COMPANY_NAMES add constraint FKpohqee82rq2u73ck3yujbi463 foreign key (COMPANY_ID) references COMPANIES
alter table ORGANIZATION_UNIT_NAMES add constraint FKtgy1ak11cnjcd3x7t37pr7sem foreign key (ORGANIZATION_UNIT_ID) references ORGANIZATION_UNITS
alter table ORGANIZATION_UNITS add constraint FKc36lxm5lc90vm1kque8vr7cyd foreign key (organization_id) references ORGANIZATIONS
alter table ORGANIZATION_UNITS add constraint FKaxl5lltie4tsoy0wug3btb5hl foreign key (PARENT_ID) references ORGANIZATION_UNITS
alter table ORGANIZATIONS add constraint FKpl7c9r2u24rjr6pqmrcd9mxdq foreign key (company_id) references COMPANIES
alter table USER_ROLES add constraint FKk3qtke64s9k5pv5hoq6yyq7py foreign key (USER_ID) references USERS
alter table USERS add constraint FKdeidj66mcx6k8vsoflveak3gj foreign key (COMPANY_ID) references COMPANIES
