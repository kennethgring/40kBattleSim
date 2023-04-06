package com.example.application.views.main;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("") // (1)
public class MainView extends VerticalLayout { // (2)

    public MainView() {
        Farts fartsGenerator = new Farts();
        CalculateDamage calculator = new CalculateDamage();

        Button button = new Button ("Generate Fart");
        TextField letter = new TextField("Lastname initial:");
        TextField number = new TextField("Birthday, day of month:");
        TextField fart = new TextField("Your fart is:");

        HorizontalLayout hl = new HorizontalLayout(letter, number, button);
        hl.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        fart.setWidth("30em");

        button.addClickListener(click->
                fart.setValue(fartsGenerator.generateFart(letter.getValue(), number.getValue())));

        add(hl);
        add(fart);

        Button avgDmgButton = new Button ("Calculate Avg Dmg");
        Button avgMdlKillButton = new Button ("Calculate Avg Models Killed");

        Text Attacker = new Text("Attacker Information:");
        TextField Attacker_balSkill = new TextField("Ballistic Skill:");
        TextField Attacker_wepSkill = new TextField("Weapon Skill:");
        HorizontalLayout AttackerLayout = new HorizontalLayout(Attacker, Attacker_balSkill, Attacker_wepSkill);
        AttackerLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        Text Weapon = new Text("Weapon Information:");
        TextField Weapon_num = new TextField("Number:");
        TextField Weapon_range = new TextField("Is Ranged:");
        TextField Weapon_attacks = new TextField("Attacks:");
        TextField Weapon_strength = new TextField("Strength:");
        TextField Weapon_armorPen = new TextField("Armor Penetration:");
        TextField Weapon_damage = new TextField("Damage:");
        HorizontalLayout WeaponLayout = new HorizontalLayout(Weapon, Weapon_num, Weapon_range, Weapon_attacks,
                Weapon_strength, Weapon_armorPen, Weapon_damage);
        WeaponLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        Text Defender = new Text("Defender:");
        TextField Defender_size = new TextField("Size:");
        TextField Defender_toughness = new TextField("Is Ranged:");
        TextField Defender_save = new TextField("Attacks:");
        TextField Defender_wounds = new TextField("Strength:");
        TextField Defender_feelNoPain = new TextField("Armor Penetration:");
        TextField Defender_invulSave = new TextField("invulnerability Save:");
        HorizontalLayout DefenderLayout = new HorizontalLayout(Defender, Defender_size, Defender_toughness, Defender_save,
                Defender_wounds, Defender_feelNoPain, Defender_invulSave);
        DefenderLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        TextField avgDmg = new TextField("Average Damage:");
        TextField avgMdlKill = new TextField("Average Models Killed:");

        avgDmgButton.addClickListener(click->
                avgDmg.setValue(String.valueOf(calculator.calcAvgDamage(
                        new Attacker(Integer.valueOf(Attacker_balSkill.getValue()), Integer.valueOf(Attacker_wepSkill.getValue())),
                        new Weapon(Integer.valueOf(Weapon_num.getValue()), Boolean.valueOf(Weapon_range.getValue()),
                                Integer.valueOf(Weapon_attacks.getValue()), Integer.valueOf(Weapon_strength.getValue()),
                                Integer.valueOf(Weapon_armorPen.getValue()), Integer.valueOf(Weapon_damage.getValue())),
                        new Defender(Integer.valueOf(Defender_size.getValue()), Integer.valueOf(Defender_toughness.getValue()),
                                Integer.valueOf(Defender_save.getValue()), Integer.valueOf(Defender_wounds.getValue()),
                                Integer.valueOf(Defender_feelNoPain.getValue()), Boolean.valueOf(Defender_invulSave.getValue()))
                ))));

        avgMdlKillButton.addClickListener(click->
                avgMdlKill.setValue(String.valueOf(calculator.calcAvgModelsKilled(
                        calculator.calcAvgDamage(
                                new Attacker(Integer.valueOf(Attacker_balSkill.getValue()), Integer.valueOf(Attacker_wepSkill.getValue())),
                                new Weapon(Integer.valueOf(Weapon_num.getValue()), Boolean.valueOf(Weapon_range.getValue()),
                                        Integer.valueOf(Weapon_attacks.getValue()), Integer.valueOf(Weapon_strength.getValue()),
                                        Integer.valueOf(Weapon_armorPen.getValue()), Integer.valueOf(Weapon_damage.getValue())),
                                new Defender(Integer.valueOf(Defender_size.getValue()), Integer.valueOf(Defender_toughness.getValue()),
                                        Integer.valueOf(Defender_save.getValue()), Integer.valueOf(Defender_wounds.getValue()),
                                        Integer.valueOf(Defender_feelNoPain.getValue()), Boolean.valueOf(Defender_invulSave.getValue()))
                        ),
                        new Weapon(Integer.valueOf(Weapon_num.getValue()), Boolean.valueOf(Weapon_range.getValue()),
                                Integer.valueOf(Weapon_attacks.getValue()), Integer.valueOf(Weapon_strength.getValue()),
                                Integer.valueOf(Weapon_armorPen.getValue()), Integer.valueOf(Weapon_damage.getValue())),
                        new Defender(Integer.valueOf(Defender_size.getValue()), Integer.valueOf(Defender_toughness.getValue()),
                                Integer.valueOf(Defender_save.getValue()), Integer.valueOf(Defender_wounds.getValue()),
                                Integer.valueOf(Defender_feelNoPain.getValue()), Boolean.valueOf(Defender_invulSave.getValue()))
                ))));

        add(AttackerLayout);
        add(WeaponLayout);
        add(DefenderLayout);
        add(avgDmgButton);
        add(avgMdlKillButton);

        add(avgDmg);
        add(avgMdlKill);

        JDBC jdbc = new JDBC("jdbc:mysql://localhost:3306/JDBCTest");

        Button db = new Button ("JDBC Test");
        TextField queryRes = new TextField("Query Result:");
        db.addClickListener(click-> queryRes.setValue(jdbc.Connect("select * from students")));

        add(db);
        add(queryRes);
    }
}
