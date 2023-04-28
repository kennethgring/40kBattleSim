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
        model.addAttribute("attackers", FakeBridge.loadAttackers(userId));
        model.addAttribute("weapons", FakeBridge.loadWeapons(userId));
        model.addAttribute("defenders", FakeBridge.loadDefenders(userId));
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
        List<Simulation> simulations = FakeBridge.loadSimulations(userId);
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
        int userId = ensureUserId(request, response);
        model.addAttribute("attackers", FakeBridge.loadAttackers(userId));
        model.addAttribute("weapons", FakeBridge.loadWeapons(userId));
        model.addAttribute("defenders", FakeBridge.loadDefenders(userId));
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
        FakeBridge.saveAttacker(userId, attacker);
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
                || armorPen > 0 || armorPen < -6
                || damage < 1) {
            throw new BadParamsException("Invalid weapon parameter(s)");
        }
        int userId = ensureUserId(request, response);
        Weapon weapon = new Weapon(name, num, isRanged, attacks, strength,
                                   armorPen, damage);
        FakeBridge.saveWeapon(userId, weapon);
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
                // Wounds can be any value, I guess.
                || (feelNoPain != 0 && (feelNoPain < 2 || feelNoPain > 6))) {
            throw new BadParamsException("Invalid defender parameter(s)");
        }
        int userId = ensureUserId(request, response);
        Defender defender = new Defender(name, size, toughness, save, wounds,
                                         feelNoPain);
        FakeBridge.saveDefender(userId, defender);
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
        boolean success = FakeBridge.saveSimulation(
            userId, attackerPk, weaponPk, defenderPk, modifiers);
        if (!success) {
            throw new NoSuchUnitException(
                "Could not find all specified units");
        }
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
                        if (cookieUserId != 0
                                && FakeBridge.userExists(cookieUserId)) {
                            return cookieUserId;
                        }
                    } catch (NumberFormatException exc) {}
                    break;
                }
            }
        }
        int userId = FakeBridge.addUser();
        Cookie cookie = new Cookie("user-id", String.valueOf(userId));
        cookie.setPath("/");
        response.addCookie(cookie);
        return userId;
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

class FakeBridge {

    private static HashMap<Integer, UserData> db;
    private static int pkCounter = 10000;
    private static Random random = new Random();
    static {
        db = new HashMap<>();
        db.put(0, new UserData());
        saveAttacker(0, new Attacker("Warriarch Hammerius", 4, 6));
        saveAttacker(0, new Attacker("Stabbystab, the Attackist", 2, 5));
        saveWeapon(0, new Weapon("Damascus Longsword", 1, false, 1, 6, 0, 3));
        saveWeapon(0, new Weapon("The Slayinator", 1, false, 2, 9, -1, 3));
        saveWeapon(0, new Weapon("Combination Laser Minigun and Chainsaw "
                                 + "Launcher", 1, true, 4, 9, -3, 2));
        saveDefender(0, new Defender("Invictus the Unpuncturable", 1, 4, 7, 0,
                                     6));
        saveDefender(0, new Defender("Shield Guy", 2, 2, 5, 0, 0));
    }

    public static boolean userExists(int userId) {
        return db.containsKey(userId);
    }

    public static int addUser() {
        int userId;
        for (;;) {
            userId = random.nextInt(1 << 23);
            if (!userExists(userId)) {
                break;
            }
        }
        db.put(userId, new UserData());
        return userId;
    }

    public static boolean saveAttacker(int userId, Attacker attacker) {
        int pk = pkCounter++;
        Entry<Attacker> entry = new Entry<>(attacker, userId, pk);
        db.get(userId).attackers.put(pk, entry);
        return true;
    }
    public static boolean saveWeapon(int userId, Weapon weapon) {
        int pk = pkCounter++;
        Entry<Weapon> entry = new Entry<>(weapon, userId, pk);
        db.get(userId).weapons.put(pk, entry);
        return true;
    }
    public static boolean saveDefender(int userId, Defender defender) {
        int pk = pkCounter++;
        Entry<Defender> entry = new Entry<>(defender, userId, pk);
        db.get(userId).defenders.put(pk, entry);
        return true;
    }

    public static boolean saveSimulation(int userId, int attackerPk,
                                         int weaponPk, int defenderPk,
                                         Modifiers modifiers) {
        UserData userData = db.get(userId);
        Entry<Attacker> atkEntry = userData.attackers.get(attackerPk);
        Entry<Weapon> wepEntry = userData.weapons.get(weaponPk);
        Entry<Defender> defEntry = userData.defenders.get(defenderPk);
        if (atkEntry == null) {
            atkEntry = db.get(0).attackers.get(attackerPk);
        }
        if (wepEntry == null) {
            wepEntry = db.get(0).weapons.get(weaponPk);
        }
        if (defEntry == null) {
            defEntry = db.get(0).defenders.get(defenderPk);
        }
        if (atkEntry == null || wepEntry == null || defEntry == null) {
            return false;
        }
        userData.simulations.add(new Simulation(
            atkEntry.getUnitType(), wepEntry.getUnitType(),
            defEntry.getUnitType(), modifiers));
        return true;
    }

    public static List<Entry<Attacker>> loadAttackers(int userId) {
        return loadUnits(userId, data -> data.attackers);
    }
    public static List<Entry<Weapon>> loadWeapons(int userId) {
        return loadUnits(userId, data -> data.weapons);
    }
    public static List<Entry<Defender>> loadDefenders(int userId) {
        return loadUnits(userId, data -> data.defenders);
    }

    private static <Unit> List<Entry<Unit>> loadUnits(
            int userId, Function<UserData,
            HashMap<Integer, Entry<Unit>>> getEntries) {
        List<Entry<Unit>> list = new LinkedList<>();
        for (Entry<Unit> entry : getEntries.apply(db.get(0)).values()) {
            list.add(entry);
        }
        for (Entry<Unit> entry : getEntries.apply(db.get(userId)).values()) {
            list.add(entry);
        }
        return list;
    }

    public static List<Simulation> loadSimulations(int userId) {
        List<Simulation> simulations = db.get(userId).simulations;
        for (Simulation sim : simulations) {
            sim.reSimulate();
        }
        return simulations;
    }

    private static class UserData {
        public HashMap<Integer, Entry<Attacker>> attackers;
        public HashMap<Integer, Entry<Weapon>> weapons;
        public HashMap<Integer, Entry<Defender>> defenders;
        public LinkedList<Simulation> simulations;
        public UserData() {
            attackers = new HashMap<>();
            weapons = new HashMap<>();
            defenders = new HashMap<>();
            simulations = new LinkedList<>();
        }
    }

}
