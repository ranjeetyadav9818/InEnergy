CREATE TABLE `RATE_PLAN` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `UUID` varchar(55) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `CODE_ID` varchar(255) NOT NULL,
  `CODE_SECTOR` varchar(255) NOT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `STATUS` varchar(55) NOT NULL DEFAULT '',
  `LAST_UPDATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5669 DEFAULT CHARSET=utf8;