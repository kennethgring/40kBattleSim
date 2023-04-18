package com.example.application;

public class SimpleSimulation {
    private SimpleUnit attacker;
    private SimpleUnit weapon;
    private SimpleUnit defender;
    private SimpleUnit modifiers;
    public SimpleSimulation(SimpleUnit attacker, SimpleUnit weapon,
                            SimpleUnit defender, SimpleUnit modifiers) {
        this.attacker = attacker;
        this.weapon = weapon;
        this.defender = defender;
        this.modifiers = modifiers;
    }
    public SimpleUnit getAttacker() {
        return attacker;
    }
    public SimpleUnit getWeapon() {
        return weapon;
    }
    public SimpleUnit getDefender() {
        return defender;
    }
    public SimpleUnit getModifiers() {
        return modifiers;
    }
}
