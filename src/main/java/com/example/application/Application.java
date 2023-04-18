package com.example.application;

import java.util.LinkedList;
import java.util.List;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.RedirectView;

@SpringBootApplication
@Controller
public class Application {

    private LinkedList<SimpleUnit> attackers;
    private LinkedList<SimpleUnit> weapons;
    private LinkedList<SimpleUnit> defenders;
    private LinkedList<SimpleSimulation> simulations;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public Application() {
        attackers = new LinkedList<>();
        attackers.add(new SimpleUnit("Warriarch Hammerius"));
        attackers.add(new SimpleUnit("Stabbystab, the Attackist"));
        weapons = new LinkedList<>();
        weapons.add(new SimpleUnit("Damascus Longsword"));
        weapons.add(new SimpleUnit("The Slayinator"));
        weapons.add(new SimpleUnit("Combination Laser Minigun and Chainsaw "
                                   + "Launcher"));
        defenders = new LinkedList<>();
        defenders.add(new SimpleUnit("Invictus the Unpuncturable"));
        defenders.add(new SimpleUnit("Shield Guy"));
        simulations = new LinkedList<>();
    }

    /**
     * Show forms to add units and simulations.
     */
    @GetMapping("/")
    public String home(Model model, HttpServletResponse response) {
        model.addAttribute("attackers", attackers);
        model.addAttribute("weapons", weapons);
        model.addAttribute("defenders", defenders);
        return "home";
    }

    /**
     * Show saved simulations.
     */
    @GetMapping("/simulations")
    public String comparisonPage(Model model, HttpServletResponse response) {
        model.addAttribute("simulations", simulations);
        return "simulations";
    }

    @PostMapping("/submit/new-attacker")
    public RedirectView newAttacker(
            @RequestParam(name="name") String name,
            HttpServletResponse response) {
        attackers.add(new SimpleUnit(name));
        return seeOther("/");
    }

    @PostMapping("/submit/new-weapon")
    public RedirectView newWeapon(
            @RequestParam(name="name") String name,
            HttpServletResponse response) {
        weapons.add(new SimpleUnit(name));
        return seeOther("/");
    }

    @PostMapping("/submit/new-defender")
    public RedirectView newDefender(
            @RequestParam(name="name") String name,
            HttpServletResponse response) {
        defenders.add(new SimpleUnit(name));
        return seeOther("/");
    }

    @PostMapping("/submit/new-simulation")
    public RedirectView newSimulation(
            @RequestParam(name="attacker") String attackerName,
            @RequestParam(name="weapon") String weaponName,
            @RequestParam(name="defender") String defenderName,
            @RequestParam(name="modifiers") String modifiersName,
            HttpServletResponse response) {
        SimpleUnit attacker = findUnitWithName(attackers, attackerName);
        SimpleUnit weapon = findUnitWithName(weapons, weaponName);
        SimpleUnit defender = findUnitWithName(defenders, defenderName);
        SimpleUnit modifiers = new SimpleUnit(modifiersName);
        SimpleSimulation simulation =
            new SimpleSimulation(attacker, weapon, defender, modifiers);
        simulations.add(simulation);
        return seeOther("/simulations");
    }

    @ExceptionHandler(NoSuchUnitException.class)
    public ResponseEntity<String> handleNoSuchUnit(
            NoSuchUnitException exception) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(exception.getMessage());
    }

    /**
     * Create an HTTP 303 See Other redirect to the given path.
     */
    private RedirectView seeOther(String path) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(path);
        redirectView.setStatusCode(HttpStatus.SEE_OTHER);
        return redirectView;
    }

    /**
     * Find a SimpleUnit with a given name in a list.
     */
    private SimpleUnit findUnitWithName(List<SimpleUnit> list, String name)
            throws NoSuchUnitException {
        for (SimpleUnit element : list) {
            if (element.getName().equals(name)) {
                return element;
            }
        }
        throw new NoSuchUnitException("No such unit: " + name);
    }

}
