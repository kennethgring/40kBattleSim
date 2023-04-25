package com.example.application;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.example.application.calc.Attacker;
import com.example.application.calc.CalculateDamage;
import com.example.application.calc.Defender;
import com.example.application.calc.Weapon;

@SpringBootApplication
@Controller
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Show the simulation form.
     */
    @GetMapping("/")
    public String home(Model model, HttpServletResponse response) {
        return "home";
    }

    /**
     * Run a simulation.
     */
    @GetMapping("/simulation")
    public String simulation(
            // @RequestParam(name="atk-bal-skill") int atkBalSkill,
            // @RequestParam(name="atk-wep-skill") int atkWepSkill,
            // @RequestParam(name="wep-number") int wepNumber,
            // @RequestParam(name="wep-type") String wepType,
            // @RequestParam(name="wep-attacks") int wepAttacks,
            // @RequestParam(name="wep-strength") int wepStrength,
            // @RequestParam(name="wep-armor-pen") int wepArmorPen,
            // @RequestParam(name="wep-damage") int wepDamage,
            // @RequestParam(name="def-size") int defSize,
            // @RequestParam(name="def-toughness") int defToughness,
            // @RequestParam(name="def-save") int defSave,
            // @RequestParam(name="def-wounds") int defWounds,
            // @RequestParam(name="def-fnp") int defFnp,
            // @RequestParam(name="def-invul-save", defaultValue="off")
                // boolean defInvulSave,
            Model model,
            HttpServletResponse response) {
        // boolean wepIsRanged = wepType.equals("ranged");
        // model.addAttribute("atkBalSkill", atkBalSkill);
        // model.addAttribute("atkWepSkill", atkWepSkill);
        // model.addAttribute("wepNumber", wepNumber);
        // model.addAttribute("wepIsRanged", wepIsRanged);
        // model.addAttribute("wepAttacks", wepAttacks);
        // model.addAttribute("wepStrength", wepStrength);
        // model.addAttribute("wepArmorPen", wepArmorPen);
        // model.addAttribute("wepDamage", wepDamage);
        // model.addAttribute("defSize", defSize);
        // model.addAttribute("defToughness", defToughness);
        // model.addAttribute("defSave", defSave);
        // model.addAttribute("defWounds", defWounds);
        // model.addAttribute("defFnp", defFnp);
        // model.addAttribute("defInvulSave", defInvulSave);

        model.addAttribute("Test", "Test Text");


        // CalculateDamage calculator = new CalculateDamage();
        // Attacker atk = new Attacker(atkBalSkill, atkWepSkill);
        // Weapon wep = new Weapon(wepNumber, wepIsRanged, wepAttacks,
        //                         wepStrength, wepArmorPen, wepDamage);
        // Defender def = new Defender(defSize, defToughness, defSave, defWounds,
        //                             defFnp);

        // double avgDamage = calculator.calcAvgDamage(atk, wep, def);
        // int avgModelsKilled = calculator.calcAvgModelsKilled(avgDamage, wep,
        //                                                      def);
        // model.addAttribute("avgDamage", avgDamage);
        // model.addAttribute("avgModelsKilled", avgModelsKilled);

        return "simulation";
    }

    /**
     * Add a new weapon.
     */
    @PostMapping("/new-weapon")
    public String NewWeapon(@RequestParam(name="wep-name") String wepName,
                            @RequestParam(name="wep-number") int wepNumber,
                            @RequestParam(name="wep-type") String wepType,
                            @RequestParam(name="wep-attacks") int wepAttacks,
                            @RequestParam(name="wep-strength") int wepStrength,
                            @RequestParam(name="wep-armor-pen") int wepArmorPen,
                            @RequestParam(name="wep-damage") int wepDamage,
                            @RequestParam(name="wep-exploding-hits", defaultValue="off")
                boolean wepExplodingHits,
                            @RequestParam(name="wep-mort-wound-six-hit", defaultValue="off")
                boolean wepMortWoundSixHit,
                            @RequestParam(name="wep-mort-wound-six-wound", defaultValue="off")
                boolean wepMortWoundSixWound,
                            @RequestParam(name="wep-add-ap-six-wound", defaultValue="off")
                boolean wepAddApSixWound,
                            Model model,
                            HttpServletResponse response) {
        System.out.println("created new weapon");
        return "home";
    }

    /**
     * Add a new attacker.
     */
    @PostMapping("/new-attacker")
    public String NewAttacker(@RequestParam(name="atk-name") String atkName,
                              @RequestParam(name="atk-bal-skill") int atkBalSkill,
                              @RequestParam(name="atk-wep-skill") int atkWepSkill,
                              @RequestParam(name="atk-wep-one") String atkWepOne,
                              @RequestParam(name="atk-wep-two") String atkWepTwo,
                              @RequestParam(name="atk-wep-three") String atkWepThree,
                              @RequestParam(name="atk-plus-one-to-hit", defaultValue="off")
                boolean atkPlusOneToHit,
                              @RequestParam(name="atk-minus-one-to-hit", defaultValue="off")
                boolean atkMinusOneToHit,
                              @RequestParam(name="atk-plus-one-to-wound", defaultValue="off")
                boolean atkPlusOneToWound,
                              @RequestParam(name="atk-minus-one-to-wound", defaultValue="off")
                boolean atkMinusOneToWound,
                              @RequestParam(name="atk-reroll-hits", defaultValue="off")
                boolean atkRerollHits,
                              @RequestParam(name="atk-reroll-hits-roll-of-one", defaultValue="off")
                boolean atkRerollHitsRollOfOne,
                              @RequestParam(name="atk-reroll-wounds", defaultValue="off")
                boolean atkRerollWounds,
                              @RequestParam(name="atk-reroll-wounds-roll-of-one", defaultValue="off")
                boolean atkRerollWoundsRollOfOne,
                              Model model,
                              HttpServletResponse response) {
        System.out.println("created new attacker");
        return "home";
    }

    /**
     * Add a new attacker.
     */
    @PostMapping("/new-defender")
    public String NewDefender(@RequestParam(name="def-name") String defName,
                              @RequestParam(name="def-size") int defSize,
                              @RequestParam(name="def-toughness") int defToughness,
                              @RequestParam(name="def-save") int defSave,
                              @RequestParam(name="def-wounds") int defWounds,
                              @RequestParam(name="def-fnp") int defFnp,
                              @RequestParam(name="def-plus-one-to-save", defaultValue="off")
                boolean defPlusOneToSave,
                              @RequestParam(name="def-minus-one-to-save", defaultValue="off")
                boolean defMinusOneToSave,
                              @RequestParam(name="def-invul-save", defaultValue="off")
                boolean defInvulSave,
                              @RequestParam(name="def-reroll-saves", defaultValue="off")
                boolean defRerollSaves,
                              @RequestParam(name="def-reroll-saves-of-one", defaultValue="off")
                boolean defRerollSavesOfOne,
                              @RequestParam(name="def-minus-one-to-damage", defaultValue="off")
                boolean defMinusOneToDamage,
                              Model model,
                              HttpServletResponse response) { 
        System.out.println("created new defender");
        return "home";
    }

    /**
     * Add a new simulation.
     */
    @PostMapping("/new-simulation")
    public String NewSimulation(@RequestParam(name="atk-name") String atkName,
                                @RequestParam(name="def-name") String defName,
                                Model model,
                                HttpServletResponse response) {
        System.out.println("created new simulation");
        return "simulation";
    }   
}
