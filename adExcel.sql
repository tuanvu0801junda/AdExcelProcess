CREATE TABLE Campaign(
  campaignID int primary key,
  campaignName varchar(50),
  campaignStatus varchar(10),
  startDate date,
  endDate date,
  budget int
);

CREATE TABLE Advertisement(
  adID int primary key,
  adName varchar(50),
  adStatus varchar(10),
  adType varchar(10),
  bigModifier int
);

select * from Campaign;

select * from Advertisement;