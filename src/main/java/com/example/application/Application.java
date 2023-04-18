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
        //     @RequestParam(name="atk-bal-skill") int atkBalSkill,
        //     @RequestParam(name="atk-wep-skill") int atkWepSkill,
        //     @RequestParam(name="wep-number") int wepNumber,
        //     @RequestParam(name="wep-type") String wepType,
        //     @RequestParam(name="wep-attacks") int wepAttacks,
        //     @RequestParam(name="wep-strength") int wepStrength,
        //     @RequestParam(name="wep-armor-pen") int wepArmorPen,
        //     @RequestParam(name="wep-damage") int wepDamage,
        //     @RequestParam(name="def-size") int defSize,
        //     @RequestParam(name="def-toughness") int defToughness,
        //     @RequestParam(name="def-save") int defSave,
        //     @RequestParam(name="def-wounds") int defWounds,
        //     @RequestParam(name="def-fnp") int defFnp,
        //     @RequestParam(name="def-invul-save", defaultValue="off")
        //         boolean defInvulSave,
        //     Model model,
        //     HttpServletResponse response) {
        ) {
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
    @GetMapping("/new-weapon")
    public String NewWeapon() {

        return "new-weapon";
    }

    /**
     * Add a new attacker.
     */
    @GetMapping("/new-attacker")
    public String NewAttacker() {

        return "new-attacker";
    }

    /**
     * Add a new attacker.
     */
    @GetMapping("/new-defender")
    public String NewDefender() {

        return "new-defender";
    }

    /**
     * Add a new simulation.
     */
    @GetMapping("/new-simulation")
    public String NewSimulation() {

        return "new-simulation";
    }   
}
