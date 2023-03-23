CREATE TABLE 40kBattleSim.Attacker (
    user_id INT,
    attacker_id INT NOT NULL auto_increment,
    unit_name VARCHAR(255),
    ballistic_skill INT,
    weapon_skill INT,
    PRIMARY KEY (`attacker_id`)
);

CREATE TABLE 40kBattleSim.Weapon (
    user_id INT,
    weapon_id INT NOT NULL auto_increment,
    weapon_type BOOLEAN,
    weapon_name VARCHAR(255),
    attacks INT,
    strength INT,
    armor_pen INT,
    damage DOUBLE,
    PRIMARY KEY (`weapon_id`)
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