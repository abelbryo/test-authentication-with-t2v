# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "USERS" ("id" SERIAL NOT NULL PRIMARY KEY,"FIRST_NAME" VARCHAR(254) NOT NULL,"LAST_NAME" VARCHAR(254) NOT NULL,"USERNAME" VARCHAR(254) NOT NULL,"PASSWORD" VARCHAR(254) NOT NULL,"EMAIL" VARCHAR(254) NOT NULL,"PHONE_NUMBER" VARCHAR(254) NOT NULL,"PERMISSION" VARCHAR(254) NOT NULL,"CREATED_ON" TIMESTAMP NOT NULL);
create unique index "IDX_USER_USERNAME" on "USERS" ("USERNAME");
create unique index "IDX_USER_EMAIL" on "USERS" ("EMAIL");

# --- !Downs

drop table "USERS";

