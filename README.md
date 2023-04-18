# Interface for bridge between DB and frontend

This is a rough draft.
Do it differently if you have a better idea.


## `boolean userExists(UserId userId)`

Return whether the user ID is that of an existing user.

## `void addUser(UserId userId)`

Add the user ID to the list of known users.

*Optional.*
*May be implemented in the other methods, or may be unnecessary.*

## `Entry<Attacker> saveAttacker(UserId userId, Attacker attacker)`

Save attacker.
Returns null on failure.

## `Entry<Weapon> saveWeapon(UserId userId, Weapon weapon)`

Save weapon.
Returns null on failure.

## `Entry<Defender> saveDefender(UserId userId, Defender defender)`

Save defender.
Returns null on failure.

## `boolean saveSimulation(UserId userId, Pk attackerPk, Pk weaponPk, Pk defenderPk, Modifiers modifiers, FixedSimResults results)`

Save simulation.
Return success status.

## `List<Entry<Attacker>> loadAttackers(UserId userId)`

Loads all the attackers the user has access to, including pre-filled
ones.

## `List<Entry<Weapon>> loadWeapons(UserId userId)`

Loads all the weapons the user has access to, including pre-filled ones.

## `List<Entry<Defender>> loadDefenders(UserId userId)`

Loads all the defenders the user has access to, including pre-filled
ones.

## `List<Entry<Simulation>> loadSimulations(UserId userId)`

Loads all the simulations the user has saved.

## `Entry<Attacker> getAttacker(UserId userId, Pk pk)`

Get the attacker with the corresponding primary key, if it exists and
the user has access to it.

## `Entry<Weapon> getWeapon(UserId userId, Pk pk)`

Get the weapon with the corresponding primary key, if it exists and
the user has access to it.

## `Entry<Defender> getDefender(UserId userId, Pk pk)`

Get the defender with the corresponding primary key, if it exists and
the user has access to it.

## `Entry<Simulation> getSimulation(UserId userId, Pk pk)`

Get the simulation with the corresponding primary key, if it exists and
the user has access to it.

## `class UserId`

Probably just a String or integer, in which case we would just use those
classes directly.

## `class Pk`

Primary key type.
Probably just an integer.

## `class Simulation`

Contains all the information about a saved simulation needed to display
it and produce further simulation results.

Includes an Attacker, Weapon, Defender, Modifiers, and FixedSimResults.

## `class FixedSimResults`

The simulation results that only need to be computed once, when the user
adds a simulation.

*If no such data is saved, then this class is completely unnecessary.*

## `class Entry<UnitType>`

Represents a UnitType in the database.
Contains the UnitType object, the UserId of its owner, and its primary
key.

*Or maybe we should add these extra fields to the UnitType classes...*
