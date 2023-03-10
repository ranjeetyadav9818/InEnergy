ALTER TABLE `PLAN_INSTANCE` ADD COLUMN `RATE_PLAN_SA_ENROLLMENT_ID` BIGINT(20);

ALTER TABLE `PLAN_INSTANCE` ADD CONSTRAINT `FK_RATE_PLAN_SA_ENROLLMENT`
FOREIGN KEY (`RATE_PLAN_SA_ENROLLMENT_ID`) REFERENCES `RATE_PLAN_SA_ENROLLMENT` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;