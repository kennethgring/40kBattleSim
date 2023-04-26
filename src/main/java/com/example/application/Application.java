package com.example.application;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.example.application.unit.*;

@SpringBootApplication
@Controller
public class Application {

    private LinkedList<Attacker> attackers;
    private LinkedList<Weapon> weapons;
    private LinkedList<Defender> defenders;
    private LinkedList<SimpleSimulation> simulations;
    private int maxUserId = 0;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public Application() {
        attackers = new LinkedList<>();
        attackers.add(new Attacker("Warriarch Hammerius", 0, 0));
        attackers.add(new Attacker("Stabbystab, the Attackist", 0, 0));
        weapons = new LinkedList<>();
        weapons.add(new Weapon("Damascus Longsword", 0, false, 0, 0, 0, 0));
        weapons.add(new Weapon("The Slayinator", 0, false, 0, 0, 0, 0));
        weapons.add(new Weapon("Combination Laser Minigun and Chainsaw "
                               + "Launcher", 0, false, 0, 0, 0, 0));
        defenders = new LinkedList<>();
        defenders.add(new Defender("Invictus the Unpuncturable", 0, 0, 0, 0,
                                   0));
        defenders.add(new Defender("Shield Guy", 0, 0, 0, 0, 0));
        simulations = new LinkedList<>();
    }

    /**
     * Show forms to add units and simulations.
     */
    @GetMapping("/")
    public String home(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {
        ensureUserId(request, response);
        model.addAttribute("attackers", attackers);
        model.addAttribute("weapons", weapons);
        model.addAttribute("defenders", defenders);
        return "home";
    }

    /**
     * Show saved simulations.
     */
    @GetMapping("/simulations")
    public String comparisonPage(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {
        ensureUserId(request, response);
        model.addAttribute("simulations", simulations);
        return "simulations";
    }

    /**
     * Show saved attackers, weapons, and defenders.
     */
    @GetMapping("/units")
    public String unitViewPage(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {
        ensureUserId(request, response);
        model.addAttribute("attackers", attackers);
        model.addAttribute("weapons", weapons);
        model.addAttribute("defenders", defenders);
        return "units";
    }

    @PostMapping("/submit/new-attacker")
    public RedirectView newAttacker(
            @RequestParam(name="name") String name,
            HttpServletRequest request,
            HttpServletResponse response) {
        ensureUserId(request, response);
        attackers.add(new Attacker(name, 0, 0));
        return seeOther("/");
    }

    @PostMapping("/submit/new-weapon")
    public RedirectView newWeapon(
            @RequestParam(name="name") String name,
            HttpServletRequest request,
            HttpServletResponse response) {
        ensureUserId(request, response);
        weapons.add(new Weapon(name, 0, false, 0, 0, 0, 0));
        return seeOther("/");
    }

    @PostMapping("/submit/new-defender")
    public RedirectView newDefender(
            @RequestParam(name="name") String name,
            HttpServletRequest request,
            HttpServletResponse response) {
        ensureUserId(request, response);
        defenders.add(new Defender(name, 0, 0, 0, 0, 0));
        return seeOther("/");
    }

    @PostMapping("/submit/new-simulation")
    public RedirectView newSimulation(
            @RequestParam(name="attacker") String attackerName,
            @RequestParam(name="weapon") String weaponName,
            @RequestParam(name="defender") String defenderName,
            @RequestParam(name="hit-plus-1", defaultValue="off")
                boolean hitPlusOne,
            @RequestParam(name="hit-minus-1", defaultValue="off")
                boolean hitMinusOne,
            @RequestParam(name="reroll-hits", defaultValue="off")
                boolean rerollHits,
            @RequestParam(name="reroll-hits-1", defaultValue="off")
                boolean rerollHitsOne,
            @RequestParam(name="reroll-wounds", defaultValue="off")
                boolean rerollWounds,
            @RequestParam(name="exploding-hits", defaultValue="off")
                boolean explodingHits,
            @RequestParam(name="mortal-wound-hits", defaultValue="off")
                boolean mortalWoundHits,
            @RequestParam(name="mortal-wound-wounds", defaultValue="off")
                boolean mortalWoundWounds,
            @RequestParam(name="extra-ap-wound", defaultValue="off")
                boolean extraAPWound,
            @RequestParam(name="save-plus-1", defaultValue="off")
                boolean savePlusOne,
            @RequestParam(name="save-minus-1", defaultValue="off")
                boolean saveMinusOne,
            @RequestParam(name="invul-save", defaultValue="off")
                boolean invulSave,
            @RequestParam(name="reroll-save", defaultValue="off")
                boolean rerollSave,
            @RequestParam(name="reroll-save-1", defaultValue="off")
                boolean rerollSaveOne,
            @RequestParam(name="damage-minus-1", defaultValue="off")
                boolean damageMinusOne,
            HttpServletRequest request,
            HttpServletResponse response) {
        ensureUserId(request, response);
        Attacker attacker = findUnitWithName(attackers, attackerName,
                                             Attacker::getName);
        Weapon weapon = findUnitWithName(weapons, weaponName, Weapon::getName);
        Defender defender = findUnitWithName(defenders, defenderName,
                                             Defender::getName);
        Modifiers modifiers = new Modifiers(new boolean[] {
            hitPlusOne,
            hitMinusOne,
            rerollHits,
            rerollHitsOne,
            rerollWounds,
            explodingHits,
            mortalWoundHits,
            mortalWoundWounds,
            extraAPWound,
            savePlusOne,
            saveMinusOne,
            invulSave,
            rerollSave,
            rerollSaveOne,
            damageMinusOne});
        SimpleSimulation simulation =
            new SimpleSimulation(attacker, weapon, defender, modifiers);
        simulations.add(simulation);
        return seeOther("/simulations");
    }

    /**
     * Get the user's ID from a cookie, setting the cookie if needed.
     */
    private int ensureUserId(HttpServletRequest request,
                             HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-id")) {
                    try {
                        int cookieUserId = Integer.valueOf(cookie.getValue());
                        if (0 < cookieUserId && cookieUserId <= maxUserId) {
                            return cookieUserId;
                        }
                    } catch (NumberFormatException exc) {}
                    break;
                }
            }
        }
        maxUserId++;
        Cookie cookie = new Cookie("user-id", String.valueOf(maxUserId));
        cookie.setPath("/");
        response.addCookie(cookie);
        return maxUserId;
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
     * Find a unit with a given name in a list.
     */
    private <Unit> Unit findUnitWithName(List<Unit> list, String name,
                                         Function<Unit, String> getName)
            throws NoSuchUnitException {
        for (Unit element : list) {
            if (getName.apply(element).equals(name)) {
                return element;
            }
        }
        throw new NoSuchUnitException("No such unit: " + name);
    }

}
