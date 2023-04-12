// Calc class that holds all fields needed for calculation
public class Calc{
    // Values in Attacker table
    private String attacker_unit_name;
    private int ballistic_skill;
    private int weapon_skill;

    // Values in Weapon table
    private boolean weapon_type;
    private String weapon_name;
    private int attacks;
    private int strength;
    private int armor_pen;
    private double damage;

    // Values in Defender table
    private String defender_unit_name;
    private int size;
    private int toughness;
    private int save;
    private int wounds;
    private int feel_no_pain;

    // Values in Calculations table
    private String[] modifiers;

    // Calc constructor
    // This sets all fields in the calc object
    public Calc(String attacker_unit_name, int ballistic_skill, int weapon_skill, boolean weapon_type, String weapon_name, int attacks, int strength, int armor_pen, double damage, String defender_unit_name, int size, int toughness, int save, int wounds, int feel_no_pain, String[] modifiers){
        this.attacker_unit_name = attacker_unit_name;
        this.ballistic_skill = ballistic_skill;
        this.weapon_skill = weapon_skill;

        this.weapon_type = weapon_type;
        this.weapon_name = weapon_name;
        this.attacks = attacks;
        this.strength = strength;
        this.armor_pen = armor_pen;
        this.damage = damage;

        this.defender_unit_name = defender_unit_name;
        this.size = size;
        this.toughness = toughness;
        this.save = save;
        this.wounds = wounds;
        this.feel_no_pain = feel_no_pain;

        this.modifiers = modifiers;
    }

    // Below are the getter methods for each field

    public String getAttackerUnitName(){
        return attacker_unit_name;
    }

    public int getBallisticSkill(){
        return ballistic_skill;
    }

    public int getWeaponSkill(){
        return weapon_skill;
    }

    public boolean getWeaponType(){
        return weapon_type;
    }

    public String getWeaponName(){
        return weapon_name;
    }

    public int getAttacks(){
        return attacks;
    }

    public int getStrength(){
        return strength;
    }

    public int getArmorPen(){
        return armor_pen;
    }

    public double getDamage(){
        return damage;
    }

    public String getDefenderUnitName(){
        return defender_unit_name;
    }

    public int getSize(){
        return size;
    }

    public int getToughness(){
        return toughness;
    }

    public int getSave(){
        return save;
    }

    public int getWounds(){
        return wounds;
    }

    public int getFeelNoPain(){
        return feel_no_pain;
    }

    public String[] getModifiers(){
        return modifiers;
    }
}
