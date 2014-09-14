SELECT * FROM alarmbylanguage where langid=7;
LOAD DATA INFILE 'D:/alarmss.txt' INTO TABLE alarmbylanguage character set UTF8 FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n';

delete FROM relaybylanguage where langid=7;
LOAD DATA INFILE 'D:/relayss.txt' INTO TABLE relaybylanguage character set UTF8 FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n';

delete FROM systemstatebylanguage  where langid=7;
LOAD DATA INFILE 'D:/systemstatess.txt' INTO TABLE systemstatebylanguage FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n';


delete FROM screenbylanguage where langid=7;
LOAD DATA INFILE 'D:/screenss.txt' INTO TABLE screenbylanguage character set UTF8 FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n';

delete FROM tablebylanguage where langid=7;
LOAD DATA INFILE 'D:/screentabless.txt' INTO TABLE tablebylanguage character set UTF8 FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n';

delete FROM databylanguage where langid=7;
LOAD DATA INFILE 'D:/datas.txt' INTO TABLE databylanguage character set UTF8 FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n';
