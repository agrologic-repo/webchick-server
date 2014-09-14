-- Replaced some data ID fields
-- From 50574 to 50934

LOAD DATA INFILE 'D:/file.txt' REPLACE INTO TABLE datatable FIELDS TERMINATED BY ','  LINES TERMINATED BY '\r\n';