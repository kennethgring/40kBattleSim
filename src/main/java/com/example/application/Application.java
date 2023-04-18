package com.example.application;

import java.util.LinkedList;

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
     * Show the simulation form.
     */
    @GetMapping("/")
    public String home(Model model, HttpServletResponse response) {
        return "home";
    }

}
