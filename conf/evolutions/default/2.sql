# --- Sample dataset

# --- !Ups

insert into benchpress (id, url, parallelism) values (1, 'http://www.google.com', 100);
insert into stats (id, status, time_taken, benchpress_id) values (1, 'OK', 3000, 1);
insert into stats (id, status, time_taken, benchpress_id) values (2, 'ERROR', 1000, 1);

# --- !Downs

delete from benchpress;
delete from stats;