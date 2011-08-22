-- -----------------------------------------------------
-- Table `wisematches`.`player_friends`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wisematches`.`player_friends` (
  `person` BIGINT(20)  NOT NULL ,
  `friend` BIGINT(20)  NOT NULL ,
  `registered` TIMESTAMP NULL ,
  `comment` VARCHAR(254) NULL ,
  PRIMARY KEY (`person`, `friend`) )
ENGINE = InnoDB;