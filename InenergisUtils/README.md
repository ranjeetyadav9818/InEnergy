
Inenergis:
Author: Enrique Gamas

TEST CSV GENERATOR:
===============================

This program is aimed to create test csv to be loaded on redshift database

1. Create manually a csv file containing tuples of service agreement id and service point id separated by comma (see MeterDataIni.txt)

`6832883568,3289498902`
`1987918098,3081840692`
`3117484555,4962562556`
`6138058483,6252033985`
`...`

The initial data where this information is extracted depends on the customer.
Here some example command to process the customer file to extract those data replacing all contents of each line before a given word (in this example `SUBSTATION`):

`find test.csv -type f -exec sed -i "" 's/\(.*\)SUBSTATION\(.*\)/\2/g' {} \;`

2. Execute main method of CsvGenerator passing the following arguments by the command line (absolute paths):
     - the path of the csv file created on step 1. If not present it will be by default:
     `/<user folder>/IdeaProjects/inenergis/InenergisUtils/src/main/resources/MeterDataIni.txt`
     - the path for the destination file to be exported as a calc sheet (excel, numbers..) If not present it will be by default:
     `/<user folder>/IdeaProjects/inenergis/InenergisUtils/src/main/resources/MeterDataIni.txt_sa.csv`
     - the path for the destination file to be uploaded to S3 (absolute paths) If not present it will be by default:
     `/<user folder>/IdeaProjects/inenergis/InenergisUtils/src/main/resources/MeterDataIni.txt.csv`
     - the redshift table which the file is generated for 

3.- Open the file in the second path with your sheet program and save it to the desired format.

4.- Load on amazon S3 the file at the third path.

5.-Execute the following command on redshift (trough PSQL) with the correct file name uploaded to S3 and the correct amazon credentials:

`COPY <table_name>(service_point_id,secondary_sp_id,usage_value,date,<usage_time field>,units,is_estimate,daylight_savings) from 's3://csv-data-loading/<FILE NAME>' credentials 'aws_access_key_id=<AWS CREDENTIAL ID>;aws_secret_access_key=<AWS CREDENTIAL PASSWORD>' REGION 'us-east-1' csv;`

TO Download data from redshift: UNLOAD command:

`UNLOAD ('SELECT * FROM <table> WHERE <conditions> ORDER BY ...')
TO 's3://<folder>/<file name>.csv'
credentials ...
DELIMITER AS ','
MAXFILESIZE 5 GB
PARALLEL FALSE; // to download on a single file`