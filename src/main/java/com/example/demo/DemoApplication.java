package com.example.demo;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

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
public class DemoApplication {

    private static OffsetDateTime startupTime;
    private HashMap<String, UserData> userDataMap;
    private LinkedList<String> availableRecipes;
    private static final String[] allRecipesArray = new String[] {
        "butter",
        "chocolate chip",
        "gingerbread",
        "gingersnap",
        "macaroon",
        "molasses",
        "oatmeal",
        "oatmeal raisin",
        "peanut butter",
        "shortbread",
        "snickerdoodle",
        "sugar",
        "white chocolate macadamia",
    };

    public static void main(String[] args) {
        startupTime = OffsetDateTime.now(ZoneId.systemDefault());
        SpringApplication.run(DemoApplication.class, args);
    }

    public DemoApplication() {
        userDataMap = new HashMap<>();
        List<String> allRecipes = Arrays.asList(allRecipesArray);
        Collections.shuffle(allRecipes);
        availableRecipes = new LinkedList<>(allRecipes);
    }

    /**
     * Display info about a user's ID and data.
     */
    @GetMapping("/")
    public String home(
            @CookieValue(name="user-id", defaultValue="") String userId,
            Model model,
            HttpServletResponse response) {
        UserData userData = userDataMap.get(userId);
        boolean isNewUser = userData == null;
        String recipe;
        if (isNewUser) {
            userId = UUID.randomUUID().toString();
            userData = new UserData();
            try {
                recipe = availableRecipes.pop();
            } catch (NoSuchElementException e) {
                recipe = null;
            }
            userData.setRecipe(recipe);
            userDataMap.put(userId, userData);
            response.addCookie(newCookie("user-id", userId));
        } else {
            recipe = userData.getRecipe();
        }
        model.addAttribute("isNewUser", isNewUser);
        model.addAttribute("userId", userId);
        model.addAttribute("recipe", recipe != null ? recipe : "nothing");
        model.addAttribute("sorry",
                           recipe == null || recipe.contains("raisin"));
        model.addAttribute("startupTime", formattedTime(startupTime));
        return "home";
    }

    /**
     * Set the user ID cookie to the given string and redirect home.
     */
    @GetMapping("/share/{userId}")
    public RedirectView share(
            @PathVariable String userId,
            HttpServletResponse response) {
        if (userDataMap.containsKey(userId)) {
            response.addCookie(newCookie("user-id", userId));
        }
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/");
        redirectView.setStatusCode(HttpStatus.SEE_OTHER);
        return redirectView;
    }

    /**
     * Clear cookies.
     */
    @GetMapping("/clear")
    public RedirectView clear(HttpServletResponse response) {
        response.addCookie(newCookie("user-id", "", 0));
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/");
        redirectView.setStatusCode(HttpStatus.SEE_OTHER);
        return redirectView;
    }

    /**
     * Create a cookie at the path "/".
     */
    private static Cookie newCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        return cookie;
    }

    /**
     * Create a cookie at the path "/" with the given expiry data.
     */
    private static Cookie newCookie(String name, String value, int maxAge) {
        Cookie cookie = newCookie(name, value);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    /**
     * Format date and time for display.
     */
    private static String formattedTime(OffsetDateTime dateTime) {
        return dateTime
            .withNano(0)
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            .replaceFirst("T", " ");
    }

}
