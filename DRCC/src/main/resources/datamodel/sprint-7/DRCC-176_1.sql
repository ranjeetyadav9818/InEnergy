ALTER TABLE PROGRAM_ELIGIBILITY_SNAPSHOT DROP FOREIGN KEY FK_ELIGIBILITY_APPLICATION;

ALTER TABLE PROGRAM_ELIGIBILITY_SNAPSHOT ADD CONSTRAINT `FK_ELIGIBILITY_APPLICATION`
FOREIGN KEY (`APPLICATION_ID`)
REFERENCES `PROGRAM_SA_ENROLLMENT` (`ID`)
  ON DELETE CASCADE;


ALTER TABLE PROGRAM_SA_ENROLLMENT_ATTRIBUTE DROP FOREIGN KEY FK_ATTRIBUTE_SA_ENROLLMENT;

ALTER TABLE PROGRAM_SA_ENROLLMENT_ATTRIBUTE ADD CONSTRAINT `FK_ATTRIBUTE_SA_ENROLLMENT`
FOREIGN KEY (`SA_ENROLLMENT_ID`)
REFERENCES `PROGRAM_SA_ENROLLMENT` (`ID`)
  ON DELETE CASCADE;


ALTER TABLE PROGRAM_FIRM_SERVICE_LEVEL DROP FOREIGN KEY FK_FSL_APPLICATION;

ALTER TABLE PROGRAM_FIRM_SERVICE_LEVEL ADD CONSTRAINT `FK_FSL_APPLICATION`
FOREIGN KEY (`APPLICATION_ID`) REFERENCES `PROGRAM_SA_ENROLLMENT` (`ID`) ON DELETE CASCADE;