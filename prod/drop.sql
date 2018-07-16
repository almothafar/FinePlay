alter table if exists COMPANIES_COMPANY_NAMES drop constraint if exists FK7dplmqp7bb1m74fik1aoxy0yq
alter table if exists COMPANIES_COMPANY_NAMES drop constraint if exists FKn3qhtm3r4eu9cd78si05xrv5h
alter table if exists ORGANIZATION_UNITS drop constraint if exists FKc36lxm5lc90vm1kque8vr7cyd
alter table if exists ORGANIZATION_UNITS drop constraint if exists FKaxl5lltie4tsoy0wug3btb5hl
alter table if exists ORGANIZATION_UNITS_ORGANIZATION_UNIT_NAMES drop constraint if exists FKpqciiy5y9smeeod0xvanq6wfg
alter table if exists ORGANIZATION_UNITS_ORGANIZATION_UNIT_NAMES drop constraint if exists FKciddj53m25d6p61rhine0fvbo
alter table if exists ORGANIZATIONS drop constraint if exists FKpl7c9r2u24rjr6pqmrcd9mxdq
alter table if exists USER_ROLES drop constraint if exists FKk3qtke64s9k5pv5hoq6yyq7py
alter table if exists USERS drop constraint if exists FKdeidj66mcx6k8vsoflveak3gj
drop table if exists CHANGE_USERS cascade
drop table if exists COMPANIES cascade
drop table if exists COMPANIES_COMPANY_NAMES cascade
drop table if exists COMPANY_NAMES cascade
drop table if exists DATETIMES cascade
drop table if exists INQUIRIES cascade
drop table if exists ORGANIZATION_UNIT_NAMES cascade
drop table if exists ORGANIZATION_UNITS cascade
drop table if exists ORGANIZATION_UNITS_ORGANIZATION_UNIT_NAMES cascade
drop table if exists ORGANIZATIONS cascade
drop table if exists REGIST_USERS cascade
drop table if exists RESET_USERS cascade
drop table if exists USER_ROLES cascade
drop table if exists USERS cascade
drop sequence if exists hibernate_sequence
