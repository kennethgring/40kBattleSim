public class Modifiers {
    private boolean hitPlusOne;
    private boolean hitMinusOne;
    private boolean rerollHits;
    private boolean rerollHitsOne;
    private boolean rerollWounds;
    private boolean explodingHits;
    private boolean mortalWoundHits;
    private boolean mortalWoundWounds;
    private boolean extraAPWound;
    private boolean savePlusOne;
    private boolean saveMinusOne;
    private boolean invulSave;
    private boolean rerollSave;
    private boolean rerollSaveOne;
    private boolean damageMinusOne;

    public Modifiers(boolean[] modifiers) {
        this.hitPlusOne = modifiers[0];
        this.hitMinusOne = modifiers[1];
        this.rerollHits = modifiers[2];
        this.rerollHitsOne = modifiers[3];
        this.rerollWounds = modifiers[4];
        this.explodingHits = modifiers[5];
        this.mortalWoundHits = modifiers[6];
        this.mortalWoundWounds = modifiers[7];
        this.extraAPWound = modifiers[8];
        this.savePlusOne = modifiers[9];
        this.saveMinusOne = modifiers[10];
        this.invulSave = modifiers[11];
        this.rerollSave = modifiers[12];
        this.rerollSaveOne = modifiers[13];
        this.damageMinusOne = modifiers[14];
    }

    public boolean getHitPlusOne() {
        return this.hitPlusOne;
    }

    public boolean getHitMinusOne() {
        return this.hitMinusOne;
    }

    public boolean getRerollHits() {
        return this.rerollHits;
    }

    public boolean getRerollHitsOne() {
        return this.rerollHitsOne;
    }

    public boolean getRerollWounds() {
        return this.rerollWounds;
    }

    public boolean getExplodingHits() {
        return this.explodingHits;
    }

    public boolean getMortalWoundHits() {
        return this.mortalWoundHits;
    }

    public boolean getMortalWoundWounds() {
        return this.mortalWoundWounds;
    }

    public boolean getExtraAPWound() {
        return this.extraAPWound;
    }

    public boolean getSavePlusOne() {
        return this.savePlusOne;
    }

    public boolean getSaveMinusOne() {
        return this.saveMinusOne;
    }

    public boolean getInvulSave() {
        return this.invulSave;
    }

    public boolean getRerollSave() {
        return this.rerollSave;
    }

    public boolean getRerollSaveOne() {
        return this.rerollSaveOne;
    }

    public boolean getDamageMinusOne() {
        return this.damageMinusOne;
    }
}