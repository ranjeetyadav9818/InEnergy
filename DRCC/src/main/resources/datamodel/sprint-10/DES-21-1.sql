CREATE TABLE `RATE_CODE` (
   `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
   `UUID` varchar(55) NOT NULL DEFAULT '',
   `NAME`       varchar(55),
   `SECTOR` varchar(55),
   `DESCRIPTION` varchar(55),
   `RATE_STATUS` varchar(55),
   `LAST_UPDATE` DATETIME NULL,
  PRIMARY KEY (`ID`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;