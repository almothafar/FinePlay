alter table COMPANIES_COMPANY_NAMES drop constraint FK7dplmqp7bb1m74fik1aoxy0yq
alter table COMPANIES_COMPANY_NAMES drop constraint FKn3qhtm3r4eu9cd78si05xrv5h
alter table ORGANIZATION_UNITS drop constraint FKc36lxm5lc90vm1kque8vr7cyd
alter table ORGANIZATION_UNITS drop constraint FKaxl5lltie4tsoy0wug3btb5hl
alter table ORGANIZATION_UNITS_ORGANIZATION_UNIT_NAMES drop constraint FKpqciiy5y9smeeod0xvanq6wfg
alter table ORGANIZATION_UNITS_ORGANIZATION_UNIT_NAMES drop constraint FKciddj53m25d6p61rhine0fvbo
alter table ORGANIZATIONS drop constraint FKpl7c9r2u24rjr6pqmrcd9mxdq
alter table USER_ROLES drop constraint FKk3qtke64s9k5pv5hoq6yyq7py
alter table USERS drop constraint FKdeidj66mcx6k8vsoflveak3gj
drop table if exists CHANGE_USERS cascade
drop table if exists COMPANIES cascade
drop table if exists COMPANIES_COMPANY_NAMES cascade
drop table if exists COMPANY_NAMES cascade
drop table if exists DATETIMES cascade
drop table if exists DECIMALS cascade
drop table if exists ENTITIES cascade
drop table if exists INQUIRIES cascade
drop table if exists ORGANIZATION_UNIT_NAMES cascade
drop table if exists ORGANIZATION_UNITS cascade
drop table if exists ORGANIZATION_UNITS_ORGANIZATION_UNIT_NAMES cascade
drop table if exists ORGANIZATIONS cascade
drop table if exists REGIST_USERS cascade
drop table if exists RESET_USERS cascade
drop table if exists STRICT_DATETIMES cascade
drop table if exists USER_ROLES cascade
drop table if exists USERS cascade
drop sequence if exists hibernate_sequence
