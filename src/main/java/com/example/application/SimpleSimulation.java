package com.example.application;

import com.example.application.unit.*;

public class SimpleSimulation {
    private Attacker attacker;
    private Weapon weapon;
    private Defender defender;
    private Modifiers modifiers;
    private Simulation simulation;
    public SimpleSimulation(Attacker attacker, Weapon weapon,
                            Defender defender, Modifiers modifiers) {
        this.attacker = attacker;
        this.weapon = weapon;
        this.defender = defender;
        this.modifiers = modifiers;
        simulation = new Simulation(attacker, weapon, defender, modifiers);
    }
    public Attacker getAttacker() {
        return attacker;
    }
    public Weapon getWeapon() {
        return weapon;
    }
    public Defender getDefender() {
        return defender;
    }
    public Modifiers getModifiers() {
        return modifiers;
    }
    public Simulation getSimulation() {
        return simulation;
    }
}
