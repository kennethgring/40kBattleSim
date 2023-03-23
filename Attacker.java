public class Attacker {
    private int balSkill;
    private int wepSkill;

    public Attacker(int balSkill, int wepSkill) {
        this.balSkill = balSkill;
        this.wepSkill = wepSkill;
    }

    public int getBalSkill() {
        return this.balSkill;
    }

    public int getWepSkill() {
        return this.wepSkill;
    }
}