package com.example.application.unit;

public class Attacker {
    private String name;
    private int balSkill;
    private int wepSkill;

    public Attacker() {}

    public Attacker(String name, int balSkill, int wepSkill) {
        this.name = name;
        this.balSkill = balSkill;
        this.wepSkill = wepSkill;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBalSkill(int balSkill) {
        this.balSkill = balSkill;
    }

    public void setWepSkill(int wepSkill) {
        this.wepSkill = wepSkill;
    }

    public String getName() {
        return this.name;
    }

    public int getBalSkill() {
        return this.balSkill;
    }

    public int getWepSkill() {
        return this.wepSkill;
    }
}