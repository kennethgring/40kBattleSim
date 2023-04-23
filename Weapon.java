public class Weapon {
    private String name;
    private int num;
    private boolean isRanged;
    private int attacks;
    private int strength;
    private int armorPen;
    private int damage;

    // TODO: Add generic constructor and setter methods
    public Weapon(String name, int num, boolean isRanged, int attacks, int strength, int armorPen, int damage) {
        this.name = name;
        this.num = num;
        this.isRanged = isRanged;
        this.attacks = attacks;
        this.strength = strength;
        this.armorPen = armorPen;
        this.damage = damage;
    }

    public String getName() {
        return this.name;
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