# 40kBattleSim

Tabletop Damage Calculator

The 40kBattleSim Tabletop Damage Calculator is a tool for Warhammer 40,000 players to input unit data and to simulate attacks between them. Functionality for a user to input the statistics of an attacker, weapon, and defender is implemented, but not required for the user as some units are already provided. The user can simulate attacks between an attacker (using a weapon) against a defender. This simulation will roll dice and produce a different result every time, following the same rules as Warhammer 40,000. In addition to the simulation, the average result based off of statistics will also be provided. Once the simulation is over, the user will be able to compare said simulation to previous simulations. All input units and weapons will be saved in a MySQL DB and entries will be tied to a unique userID so users can keep a record of all units input and simulations run.

Expected users are both novice Warhammer players wishing to test the capabilities of different units and experienced players wishing to find the most optimal units for upcoming tournaments. 


## Project Specification on Google Docs

https://docs.google.com/document/d/18cqy8gi2caCHGMhSnG-sYAwAHNzUV2VBMd70Hj5fMwQ/edit?usp=sharing


## Building and Running the Spring Boot Application

```sh
./mvnw
```

Or, with a different port than the default, e.g. `8888`:

```sh
PORT=8888 ./mvnw
```
