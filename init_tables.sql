-- For most up to date MySQL diagrams, check the specs document.

CREATE TABLE 40kBattleSim.User_IDs (
    user_id INT NOT NULL,
    PRIMARY KEY (`user_id`)
);

CREATE TABLE 40kBattleSim.Attacker (
    user_id INT,
    attacker_id INT NOT NULL auto_increment,
    unit_name VARCHAR(255),
    ballistic_skill INT,
    weapon_skill INT,
    PRIMARY KEY (`attacker_id`),
    FOREIGN KEY (`user_id`) REFERENCES 40kBattleSim.User_IDs(`user_id`)
);

CREATE TABLE 40kBattleSim.Weapon (
    user_id INT,
    weapon_id INT NOT NULL auto_increment,
    isRanged BOOLEAN,
    weapon_name VARCHAR(255),
    attacks INT,
    strength INT,
    armor_pen INT,
    damage INT,
    number INT,
    PRIMARY KEY (`weapon_id`),
    FOREIGN KEY (`user_id`) REFERENCES 40kBattleSim.User_IDs(`user_id`)
);

CREATE TABLE 40kBattleSim.Defender (
    user_id INT,
    defender_id INT NOT NULL auto_increment,
    unit_name VARCHAR(255),
    size INT,
    toughness INT,
    save INT,
    wounds INT,
    feel_no_pain INT,
    PRIMARY KEY (`defender_id`),
    FOREIGN KEY (`user_id`) REFERENCES 40kBattleSim.User_IDs(`user_id`)
);

-- modifiers is a set, meaning the SQL entry for that column can contain any number of items in the set or none at all.
-- This acts as a "checkbox" style field that will be used for calculations.
--
-- Foreign keys of attacker_id, weapon_id, and defender_id represent the entries for attacker, weapon, and defender respectively.
-- These are required for calculations to be performed.
CREATE TABLE 40kBattleSim.Calculations ( 
    user_id INT,
    calc_id INT NOT NULL auto_increment,
    attacker_id INT NOT NULL,
    weapon_id INT NOT NULL,
    defender_id INT NOT NULL,
    `hit+1` BOOLEAN,
    `hit-1` BOOLEAN,
    `reroll_hits` BOOLEAN,
    `reroll_hit_1` BOOLEAN,
    `reroll_wounds` BOOLEAN,
    `exploding_hits` BOOLEAN,
    `mortal_wounds_hit` BOOLEAN,
    `mortal_wounds_wound` BOOLEAN,
    `additional_ap_wound` BOOLEAN,
    `save+1` BOOLEAN,
    `save-1` BOOLEAN, 
    `invulnerable_save` BOOLEAN,
    `reroll_save` BOOLEAN,
    `reroll_save_1` BOOLEAN,
    `damage-1` BOOLEAN,
    PRIMARY KEY (calc_id),
    FOREIGN KEY (attacker_id) REFERENCES 40kBattleSim.Attacker(`attacker_id`),
    FOREIGN KEY (weapon_id) REFERENCES 40kBattleSim.Weapon(`weapon_id`),
    FOREIGN KEY (defender_id) REFERENCES 40kBattleSim.Defender(`defender_id`)
);


-- Insertion of dummy testing data.
-- A dummy "global" User ID will be staticly coded at 0

-- Insert a test attacker row into Attacker table.
-- test_attacker_1 has ballistic skill = 5 and weapon skill = 5
-- No need to add in unit ID since it should be auto incremented by the table
-- Attacker_ID = 1
INSERT INTO 40kBattleSim.Attacker(user_id, unit_name, ballistic_skill, weapon_skill)
VALUES (0, 'test_attacker_1', '5', '5');

-- Insert a second test attacker row into Attacker table.
-- test_attacker_2 has ballistic skill = 1 and weapon skill = 1
-- No need to add in unit ID since it should be auto incremented by the table
-- Attacker_ID = 2
INSERT INTO 40kBattleSim.Attacker(user_id, unit_name, ballistic_skill, weapon_skill)
VALUES (0, 'test_attacker_2', '1', '1');

-- Insert a ballistic weapon into the Weapon table.
-- weapon_type = 0 to indicate ballistic type
-- No need to add in weapon ID since it should be auto incremented by the table
-- References test_attacker_1 with ballistic skill of 5
-- weapon_type = 0, attacks = 2, strength = 2, armor_pen = 2, damage = 4.1
INSERT INTO 40kBattleSim.Weapon(user_id, weapon_type, weapon_name, attacks, strength, armor_pen, damage, attacker_id)
VALUES (0, 0, 'test_ballistic', '2', '2', '2', '4.1', '1');

-- Insert a non-ballistic weapon into the Weapon table.
-- weapon_type = 1 to indicate non-ballistic type
-- No need to add in weapon ID since it should be auto incremented by the table
-- References test_attacker_2 with weapon skill of 1
-- weapon_type = 1, attacks = 5, strength = 5, armor_pen = 5, damage = 1.0
INSERT INTO 40kBattleSim.Weapon(user_id, weapon_type, weapon_name, attacks, strength, armor_pen, damage, attacker_id)
VALUES (0, 1, 'test_non_ballistic', '5', '5', '5', '1.0', '2');

-- Insert a defender into the Defender table.
-- No need to add in defender_id since it should be auto-incremented by the table
-- size = 1, toughness = 1, save = 1, wounds = 1, feel_no_pain = 3
-- Defender ID = 1
INSERT INTO 40kBattleSim.Defender(user_id, unit_name, size, toughness, save, wounds, feel_no_pain)
VALUES (0, 'test_def_1', '1', '1', '1', '1', '3');

-- Insert a defender into the Defender table.
-- No need to add in defender_id since it should be auto-incremented by the table
-- size = 4, toughness = 4, save = 3, wounds = 2, feel_no_pain = 1
-- Defender ID = 2
INSERT INTO 40kBattleSim.Defender(user_id, unit_name, size, toughness, save, wounds, feel_no_pain)
VALUES (0, 'test_def_2', '4', '4', '3', '2', '1');