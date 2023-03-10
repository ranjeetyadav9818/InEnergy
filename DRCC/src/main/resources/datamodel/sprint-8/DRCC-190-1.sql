DROP TABLE IF EXISTS `TROVE_METER_FORECAST`;

DROP TABLE IF EXISTS `METER_FORECAST`;

CREATE TABLE `METER_FORECAST` (
  `ID`                   BIGINT(20) NOT NULL AUTO_INCREMENT,
  `UUID`                 VARCHAR(55)         DEFAULT NULL,
  `SERVICE_AGREEMENT_ID` VARCHAR(10),
  `MEASURE_TYPE`         VARCHAR(20),
  `MEASURE_DATE`         DATE,
  `HOUR_END_1`             BIGINT(10),
  `HOUR_END_2`             BIGINT(10),
  `HOUR_END_3`             BIGINT(10),
  `HOUR_END_4`             BIGINT(10),
  `HOUR_END_5`             BIGINT(10),
  `HOUR_END_6`             BIGINT(10),
  `HOUR_END_7`             BIGINT(10),
  `HOUR_END_8`             BIGINT(10),
  `HOUR_END_9`             BIGINT(10),
  `HOUR_END_10`            BIGINT(10),
  `HOUR_END_11`            BIGINT(10),
  `HOUR_END_12`            BIGINT(10),
  `HOUR_END_13`            BIGINT(10),
  `HOUR_END_14`            BIGINT(10),
  `HOUR_END_15`            BIGINT(10),
  `HOUR_END_16`            BIGINT(10),
  `HOUR_END_17`            BIGINT(10),
  `HOUR_END_18`            BIGINT(10),
  `HOUR_END_19`            BIGINT(10),
  `HOUR_END_20`            BIGINT(10),
  `HOUR_END_21`            BIGINT(10),
  `HOUR_END_22`            BIGINT(10),
  `HOUR_END_23`            BIGINT(10),
  `HOUR_END_24`            BIGINT(10),
  PRIMARY KEY (ID), constraint METER_FORECAST_FK FOREIGN KEY (`SERVICE_AGREEMENT_ID`) REFERENCES `SERVICE_AGREEMENT`(SERVICE_AGREEMENT_ID)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
