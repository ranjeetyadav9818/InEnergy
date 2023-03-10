CREATE TABLE `RATE_PLAN_PROFILE` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `UUID` varchar(55) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `TARIFF_RESOLUTION_NUMBER` varchar(255),
  `TARIFF_REFERENCES_NOTE` TEXT,
  `LAST_UPDATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `TARIFF_ISSUE_DATE` datetime DEFAULT NULL,
  `EFFECTIVE_START_DATE` datetime DEFAULT NULL,
  `EFFECTIVE_END_DATE` datetime DEFAULT NULL,
  `RATE_PLAN_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_Rate_PROFILE_PLAN`
  FOREIGN KEY (`RATE_PLAN_ID`)
  REFERENCES `RATE_PLAN` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;