ALTER TABLE `agrodb`.`flockhistory`
ADD CONSTRAINT `FK_flockhistory_FlockID`
FOREIGN KEY (`FlockID`) REFERENCES `flocks` (`FlockID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `agrodb`.`flockhistory24`
ADD CONSTRAINT `FK_flockhistory24_FlockID`
FOREIGN KEY (`FlockID`) REFERENCES `flocks` (`FlockID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

