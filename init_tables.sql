CREATE TABLE 40kBattleSim.Attacker (
    user_id INT,
    attacker_id INT NOT NULL auto_increment,
    unit_name VARCHAR(255),
    ballistic_skill INT,
    weapon_skill INT,
    PRIMARY KEY (`attacker_id`)
);

-- Foreign key of attacker_id, since each weapon will reference an attacker's ballistic_skill or weapon_skill
CREATE TABLE 40kBattleSim.Weapon (
    user_id INT,
    weapon_id INT NOT NULL auto_increment,
    weapon_type BOOLEAN,
    weapon_name VARCHAR(255),
    attacks INT,
    strength INT,
    armor_pen INT,
    damage DOUBLE,
    PRIMARY KEY (`weapon_id`),
    FOREIGN KEY (`attacker_id`)
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
    PRIMARY KEY (`defender_id`)
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
    modifiers SET('hit+1', 'hit-1', 'reroll_hits', 'reroll_hit_1', 'reroll_wounds', 'exploding_hits', 'mortal_wounds_hit',
                  'mortal_wounds_wound', 'additiona_ap_wound', 'save+1', 'save-1', 'invulnerable_save', 'reroll_save',
                  'reroll_save_1', 'damage-1'),
    PRIMARY KEY (calc_id),
    FOREIGN KEY (attacker_id) REFERENCES 40kBattleSim.Attacker(`attacker_id`),
    FOREIGN KEY (weapon_id) REFERENCES 40kBattleSim.Weapon(`weapon_id`),
    FOREIGN KEY (defender_id) REFERENCES 40kBattleSim.Defender(`defender_id`)
);
