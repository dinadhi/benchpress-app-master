# --- First database schema

# --- !Ups

create table benchpress (
  id                        bigint not null,
  url                       varchar(255),
  parallelism               bigint,
  constraint pk_benchpress primary key (id))
;

create table stats (
  id                        bigint not null,
  status                    varchar(255),
  time_taken                bigint,
  benchpress_id             bigint,
  constraint pk_stats primary key (id))
;

create sequence benchpress_seq start with 1000;
create sequence stats_seq start with 1000;

alter table stats add constraint fk_stats_benchpress_1 foreign key (benchpress_id) references benchpress (id) on delete restrict on update restrict;
create index ix_stats_benchpress_1 on stats (benchpress_id);

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists benchpress;

drop table if exists stats;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists benchpress_seq;

drop sequence if exists stats_seq;