package com.example.application;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.example.application.unit.*;

@SpringBootApplication
@Controller
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Show forms to add units and simulations.
     */
    @GetMapping("/")
    public String home(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {
        int userId = ensureUserId(request, response);
        model.addAttribute("userId", userId);
        model.addAttribute("attackers", Bridge.loadAttackers(userId));
        model.addAttribute("weapons", Bridge.loadWeapons(userId));
        model.addAttribute("defenders", Bridge.loadDefenders(userId));
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
        int userId = ensureUserId(request, response);
        model.addAttribute("userId", userId);
        model.addAttribute("simulations", Bridge.loadSimulations(userId));
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
        int userId = ensureUserId(request, response);
        model.addAttribute("userId", userId);
        model.addAttribute("attackers", Bridge.loadAttackers(userId));
        model.addAttribute("weapons", Bridge.loadWeapons(userId));
        model.addAttribute("defenders", Bridge.loadDefenders(userId));
        return "units";
    }

    @PostMapping("/submit/new-attacker")
    public RedirectView newAttacker(
            @RequestParam(name="name") String name,
            @RequestParam(name="bal-skill") int balSkill,
            @RequestParam(name="wep-skill") int wepSkill,
            HttpServletRequest request,
            HttpServletResponse response) {
            if (name.isEmpty()
                    || balSkill < 2 || balSkill > 6
                    || wepSkill < 2 || wepSkill > 6) {
                throw new BadParamsException("Invalid attacker parameter(s)");
            }
        int userId = ensureUserId(request, response);
        Attacker attacker = new Attacker(name, balSkill, wepSkill);
        Bridge.saveAttacker(userId, attacker);
        return seeOther("/");
    }

    @PostMapping("/submit/new-weapon")
    public RedirectView newWeapon(
            @RequestParam(name="name") String name,
            @RequestParam(name="num") int num,
            @RequestParam(name="type") String type,
            @RequestParam(name="attacks") int attacks,
            @RequestParam(name="strength") int strength,
            @RequestParam(name="armor-pen") int armorPen,
            @RequestParam(name="damage") int damage,
            HttpServletRequest request,
            HttpServletResponse response) {
        boolean isRanged = type.equals("ranged");
        if (name.isEmpty()
                || num < 1
                || (!isRanged && !type.equals("melee"))
                || attacks < 1
                || strength < 1
                || armorPen > 0
                || damage < 1) {
            throw new BadParamsException("Invalid weapon parameter(s)");
        }
        int userId = ensureUserId(request, response);
        Weapon weapon = new Weapon(name, num, isRanged, attacks, strength,
                                   armorPen, damage);
        Bridge.saveWeapon(userId, weapon);
        return seeOther("/");
    }

    @PostMapping("/submit/new-defender")
    public RedirectView newDefender(
            @RequestParam(name="name") String name,
            @RequestParam(name="size") int size,
            @RequestParam(name="toughness") int toughness,
            @RequestParam(name="save") int save,
            @RequestParam(name="wounds") int wounds,
            @RequestParam(name="feel-no-pain") int feelNoPain,
            HttpServletRequest request,
            HttpServletResponse response) {
        if (name.isEmpty()
                || size < 1
                || toughness < 1
                || save < 2 || save > 7
                || wounds < 1
                || feelNoPain < 2 || feelNoPain > 7) {
            throw new BadParamsException("Invalid defender parameter(s)");
        }
        int userId = ensureUserId(request, response);
        Defender defender = new Defender(name, size, toughness, save, wounds,
                                         feelNoPain);
        Bridge.saveDefender(userId, defender);
        return seeOther("/");
    }

    @PostMapping("/submit/new-simulation")
    public RedirectView newSimulation(
            @RequestParam(name="attacker") int attackerPk,
            @RequestParam(name="weapon") int weaponPk,
            @RequestParam(name="defender") int defenderPk,
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
        int userId = ensureUserId(request, response);
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
        boolean success = Bridge.saveSimulation(
            userId, attackerPk, weaponPk, defenderPk, modifiers);
        if (!success) {
            throw new NoSuchUnitException(
                "Could not find all specified units");
        }
        return seeOther("/simulations");
    }

    /**
     * Load a user ID cookie.
     */
    @GetMapping("/share-session/{userId}")
    public RedirectView shareSession(
            @PathVariable int userId,
            HttpServletResponse response) {
        if (userId != 0 && Bridge.userExists(userId)) {
            response.addCookie(userIdCookie(userId));
        }
        return seeOther("/");
    }

    /**
     * Clear the user ID cookie.
     */
    @GetMapping("/clear-session")
    public RedirectView clearSession(HttpServletResponse response) {
        Cookie cookie = userIdCookie(-1);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return seeOther("/");
    }

    /**
     * Incomplete side-by-side simulation comparison page.
     */
    @GetMapping("/compare-two-teaser")
    public String compareTwoTeaser(Model model) {
        // Calculation 1
        model.addAttribute("attackerName1", "Warriarch Hammerius");
        model.addAttribute("defenderName1", "Invictus the Unpuncturable");
        model.addAttribute("avgTotalDamage1", 5.30003);
        model.addAttribute("avgModelsKilled1", 2.556);
        model.addAttribute("simTotalDamage1", 7);
        model.addAttribute("simModelsKilled1", 4);

        // Calculation 2
        model.addAttribute("attackerName2", "Stabbystab the Attackist");
        model.addAttribute("defenderName2", "Shield Guy");
        model.addAttribute("avgTotalDamage2", 7.6699);
        model.addAttribute("avgModelsKilled2", 4.667);
        model.addAttribute("simTotalDamage2", 8);
        model.addAttribute("simModelsKilled2", 5);

        return "compare-two-teaser";
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
                        if (cookieUserId != 0
                                && Bridge.userExists(cookieUserId)) {
                            return cookieUserId;
                        }
                    } catch (NumberFormatException exc) {}
                    break;
                }
            }
        }
        int userId = Bridge.addUser();
        response.addCookie(userIdCookie(userId));
        return userId;
    }

    private Cookie userIdCookie(int userId) {
        Cookie cookie = new Cookie("user-id", String.valueOf(userId));
        cookie.setPath("/");
        return cookie;
    }

    @ExceptionHandler(NoSuchUnitException.class)
    public ResponseEntity<String> handleNoSuchUnit(
            NoSuchUnitException exception) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(exception.getMessage());
    }

    @ExceptionHandler(BadParamsException.class)
    public ResponseEntity<String> handleBadParams(
            BadParamsException exception) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
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

class NoSuchUnitException extends RuntimeException {
    public NoSuchUnitException(String message) {
        super(message);
    }
}

class BadParamsException extends RuntimeException {
    public BadParamsException(String message) {
        super(message);
    }
}
