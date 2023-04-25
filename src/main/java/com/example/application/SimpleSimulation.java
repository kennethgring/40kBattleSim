package com.example.application;

import com.example.application.unit.*;

public class SimpleSimulation {
    private SimpleUnit attacker;
    private SimpleUnit weapon;
    private SimpleUnit defender;
    private Modifiers modifiers;
    public SimpleSimulation(SimpleUnit attacker, SimpleUnit weapon,
                            SimpleUnit defender, Modifiers modifiers) {
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
    public Modifiers getModifiers() {
        return modifiers;
    }
}
