CREATE DATABASE IF NOT EXISTS dev_journey;
CREATE DATABASE IF NOT EXISTS prod_journey;
create user IF NOT EXISTS 'Journey'@'%' identified by 'test_password123';
grant all on dev_journey.* to 'Journey'@'%';
grant select, insert, delete, update on prod_journey.* to 'Journey'@'%';