-- Format of the commands for the queries to the database


-- Query command to get calculation data
-- <calc_id> is the id of the calculation to query
SELECT *
FROM Attacker a, Weapon w, Defender d, Calculations c
WHERE c.attacker_id = a.attacker_id
    AND c.weapon_id = w.weapon_id
    AND c.defender_id = d.defender_id
    AND c.calc_id = <calc_id>;

-- Insert command to insert into Attacker table
-- Replace <TYPE> in the VALUES row with the value for the corresponding column
INSERT INTO Attacker (user_id, unit_name, ballistic_skill, weapon_skill)
VALUES (<INT>, <VARCHAR(255)>, <INT>, <INT>);

-- Insert command to insert into Weapon table
-- Replace <TYPE> in the VALUES row with the value for the corresponding column
INSERT INTO Weapon (user_id, weapon_type, weapon_name, attacks, strength, armor_pen, damage)
VALUES (<INT>, <BOOLEAN>, <VARCHAR(255)>, <INT>, <INT>, <INT>, <DOUBLE>);

-- Insert command to insert into Defender table
-- Replace <TYPE> in the VALUES row with the value for the corresponding column
INSERT INTO Defender (user_id, unit_name, size, toughness, save, wounds, feel_no_pain)
VALUES (<INT>, <VARCHAR(255)>, <INT>, <INT>, <INT>, <INT>, <INT>);

-- Insert command to insert into Calculations table
-- Replace <TYPE> in the VALUES row with the value for the corresponding column
INSERT INTO Calculations (user_id, calc_id, attacker_id, weapon_id, defender_id, modifiers)
VALUES (<INT>, <INT>, <INT>, <INT>, <INT>, <SET>);


-- If the user wants to update multiple columns in a specific record, execute each command for each individual column change
-- <columnName> is the column for the record where <newValue> will be placed

-- Update command to update a specific record in the Attacker table
-- <attacker_id> is the id of the record to be updated in Attacker
UPDATE Attacker
SET <columnName> = <newValue>
WHERE Attacker.attacker_id = <attacker_id>;

-- Update command to update a specific record in the Weapon table
-- <weapon_id> is the id of the record to be updated in Weapon
UPDATE Weapon
SET <columnName> = <newValue>
WHERE Weapon.weapon_id = <weapon_id>;

-- Update command to update a specific record in the Defender table
-- <defender_id> is the id of the record to be updated in Defender
UPDATE Defender
SET <columnName> = <newValue>
WHERE Defender.defender_id = <defender_id>;

-- Update command to update a specific record in the Calculations table
-- <calc_id> is the id of the record to be updated in Calculations
UPDATE Calculations
SET <columnName> = <newValue>
WHERE Calculations.calc_id = <calc_id>;
