CREATE TABLE `ruledb`.`mainrule` (
  `ruleId` INT NOT NULL AUTO_INCREMENT,
  `ruleName` VARCHAR(45) NULL,
  `ruleTemplate` VARCHAR(45) NULL,
  `whenCondition` BLOB NULL,
  `thenCondition` BLOB NULL,
  PRIMARY KEY (`ruleId`),
  UNIQUE INDEX `ruleId_UNIQUE` (`ruleId` ASC) VISIBLE);
