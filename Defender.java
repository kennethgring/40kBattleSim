public class Defender {
    private int size;
    private int toughness;
    private int save;
    private int wounds;
    private int feelNoPain;

    public Defender(int size, int toughness, int save, int wounds, int feelNoPain) {
        this.size = size;
        this.toughness = toughness;
        this.save = save;
        this.wounds = wounds;
        this.feelNoPain = feelNoPain;
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
}