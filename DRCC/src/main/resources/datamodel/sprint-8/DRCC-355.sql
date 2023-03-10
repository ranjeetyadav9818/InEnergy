DROP TABLE IF EXISTS `IMPACTED_CUSTOMER`;

CREATE TABLE `IMPACTED_CUSTOMER` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `UUID` varchar(55) NOT NULL DEFAULT '',
  `EVENT_ID` bigint(20) unsigned NOT NULL,
  `ENROLLMENT_ID` bigint(20) NOT NULL,
  `DRMS_ID` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`ID`),
  KEY `EVENT_ENROLLMENT_LINK_EVENT_FK` (`EVENT_ID`),
  KEY `EVENT_ENROLLMENT_LINK_ENROLLMENT_FK` (`ENROLLMENT_ID`),
  CONSTRAINT `EVENT_ENROLLMENT_LINK_ENROLLMENT_FK` FOREIGN KEY (`ENROLLMENT_ID`) REFERENCES `PROGRAM_SA_ENROLLMENT` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `EVENT_ENROLLMENT_LINK_EVENT_FK` FOREIGN KEY (`EVENT_ID`) REFERENCES `EVENT` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;