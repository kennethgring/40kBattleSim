package com.example.application.views.main;

public class Weapon {
    private int num;
    private boolean isRanged;
    private int attacks;
    private int strength;
    private int armorPen;
    private int damage;

    public Weapon(int num, boolean isRanged, int attacks, int strength, int armorPen, int damage) {
        this.num = num;
        this.isRanged = isRanged;
        this.attacks = attacks;
        this.strength = strength;
        this.armorPen = armorPen;
        this.damage = damage;
    }

    public int getNum() {
        return this.num;
    }

    public boolean getIsRanged() {
        return this.isRanged;
    }

    public int getAttacks() {
        return this.attacks;
    }

    public int getStrength() {
        return this.strength;
    }

    public int getArmorPen() {
        return this.armorPen;
    }

    public int getDamage() {
        return this.damage;
    }
}
