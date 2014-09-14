DELETE FROM flockhistory
WHERE flockid NOT IN (SELECT
                        flockid
                      FROM flocks);

DELETE FROM flockhistory24
WHERE flockid NOT IN (SELECT
                        flockid
                      FROM flocks);

DELETE FROM flockhistory24
WHERE dnum IN ('D42', 'D43');