CREATE TABLE campaign(
  campaign_id int primary key,
  campaign_name varchar(50),
  campaign_status varchar(10),
  start_date date,
  end_date date,
  budget int
);

CREATE TABLE advertisement(
  ad_id int primary key,
  ad_name varchar(50),
  ad_status varchar(10),
  ad_type varchar(10),
  big_modifier int
);

select * from Campaign;

select * from Advertisement;

delete from Campaign;
delete from Advertisement;

drop table Campaign;
drop table Advertisement;