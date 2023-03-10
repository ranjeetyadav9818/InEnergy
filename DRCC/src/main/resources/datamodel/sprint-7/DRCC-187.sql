DROP TABLE IF EXISTS `TROVE_METER_FORECAST`;

CREATE TABLE `TROVE_METER_FORECAST` (

  `ID`                   BIGINT(20) NOT NULL AUTO_INCREMENT,
  `UUID`                 VARCHAR(55)         DEFAULT NULL,
  `SERVICE_AGREEMENT_ID` VARCHAR(10),
  `MEASURE_TYPE`         VARCHAR(20),
  `MEASURE_DATE`         DATE,
  `HEADER_1`             INT(10),
  `HEADER_2`             INT(10),
  `HEADER_3`             INT(10),
  `HEADER_4`             INT(10),
  `HEADER_5`             INT(10),
  `HEADER_6`             INT(10),
  `HEADER_7`             INT(10),
  `HEADER_8`             INT(10),
  `HEADER_9`             INT(10),
  `HEADER_10`            INT(10),
  `HEADER_11`            INT(10),
  `HEADER_12`            INT(10),
  `HEADER_13`            INT(10),
  `HEADER_14`            INT(10),
  `HEADER_15`            INT(10),
  `HEADER_16`            INT(10),
  `HEADER_17`            INT(10),
  `HEADER_18`            INT(10),
  `HEADER_19`            INT(10),
  `HEADER_20`            INT(10),
  `HEADER_21`            INT(10),
  `HEADER_22`            INT(10),
  `HEADER_23`            INT(10),
  `HEADER_24`            INT(10),
  PRIMARY KEY (ID)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8