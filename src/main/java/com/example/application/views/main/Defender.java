package com.example.application.views.main;

public class Defender {
    private int size;
    private int toughness;
    private int save;
    private int wounds;
    private int feelNoPain;
    private boolean invulSave;

    public Defender(int size, int toughness, int save, int wounds, int feelNoPain, boolean invulSave) {
        this.size = size;
        this.toughness = toughness;
        this.save = save;
        this.wounds = wounds;
        this.feelNoPain = feelNoPain;
        this.invulSave = invulSave;
    }

    public int getSize() {
        return this.size;
    }

    public int getToughness() {
        return this.toughness;
    }

    public int getSave() {
        return this.save;
    }

    public int getWounds() {
        return this.wounds;
    }

    public int getFeelNoPain() {
        return this.feelNoPain;
    }

    public boolean getInvulSave() {
        return this.invulSave;
    }
}

