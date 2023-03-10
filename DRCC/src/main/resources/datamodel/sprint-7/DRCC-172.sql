########################################################################

DROP TABLE IF EXISTS `SUBLAP`;

CREATE TABLE `SUBLAP` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `UUID` varchar(55) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `CODE` varchar(5) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `SUBLAP` (`ID`, `UUID`, `NAME`, `CODE`)
VALUES
	(1,'1','Central Coast','PGCC'),
	(2,'2','East Bay (Bay Area)','PGEB'),
	(3,'3','Fresno','PGF1'),
	(4,'4','Geysers','PGFG'),
	(5,'5','Humboldt','PGHB'),
	(6,'6','Los Padres','PGLP'),
	(7,'7','North Bay','PGNB'),
	(8,'8','North Coast','PGNC'),
	(9,'9','North Valley','PGNV'),
	(10,'10','Peninsula (Bay Area)','PGP2'),
	(11,'11','Sacramento Valley','PGSA'),
	(12,'12','San Francisco (Bay Area)','PGSF'),
	(13,'13','San Joaquin','PGSN'),
	(14,'14','Sierra','PGSI'),
	(15,'15','South Bay (Bay Area)','PGSB'),
	(16,'16','Stockton','PGST');

########################################################################

DROP TABLE IF EXISTS `LSE`;

CREATE TABLE `LSE` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `UUID` varchar(55) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `CODE` varchar(5) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `LSE` (`ID`, `UUID`, `NAME`, `CODE`)
VALUES
	(1,'1','3 Phases Renewables LSE','LTPES'),
	(2,'2','Calpine PowerAmerica-CA, LLC LSE','LCALP'),
	(3,'3','City of Cerritos LSE','LCER'),
	(4,'4','City of Corona LSE','LCOR'),
	(5,'5','City of Vernon LSE','LVERN'),
	(6,'6','Commerce Energy, Inc. LSE','LCECO'),
	(7,'7','Constellation NewEnergy, Inc. LSE','LNEI1'),
	(8,'8','Direct Energy Business LSE','LSEL1'),
	(9,'9','Eastside Power AuthorityLSE','LEPA'),
	(10,'10','EDF Industrial Power Services (CA), LLC  LSE','LEDF'),
	(11,'11','Glacial Energy of California Inc LSE','LGEC'),
	(12,'12','Gexa Energy California, LLC','LGEXA'),
	(13,'13','Liberty Power Holdings, LLC LSE','LLPH'),
	(14,'14','Marin Clean Energy LSE','LMCE'),
	(15,'15','Pacific Gas & Electric LSE','LPGE'),
	(16,'16','Pilot Power Group, Inc. LSE','LPIPO'),
	(17,'17','San Diego Gas & Electric LSE','LSDGE'),
	(18,'18','Shell Energy North America LSE','LCRLL'),
	(19,'19','Southern California Edison Company LSE1','LSCE'),
	(20,'20','Noble Americas Energy Solutions LLC LSE','LSEES');

########################################################################

DROP TABLE IF EXISTS `DATA_MAPPING`;

CREATE TABLE `DATA_MAPPING` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `UUID` varchar(55) NOT NULL,
  `TYPE` enum('SUBLAP','LSE') NOT NULL,
  `SRC` varchar(255) NOT NULL,
  `DEST` varchar(255) NOT NULL,
  `DEST_CODE` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=289 DEFAULT CHARSET=utf8;