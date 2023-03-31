package com.example.demo;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
public class DemoApplication {

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
        SpringApplication.run(DemoApplication.class, args);
    }

    public DemoApplication() {
        userDataMap = new HashMap<>();
        List<String> allRecipes = Arrays.asList(allRecipesArray);
        Collections.shuffle(allRecipes);
        availableRecipes = new LinkedList<>(allRecipes);
    }

    @GetMapping("/")
    public String home(
            @CookieValue(name="user-id", defaultValue="") String userID,
            Model model,
            HttpServletResponse response) {
        UserData userData = userDataMap.get(userID);
        boolean isNewUser = userData == null;
        String recipe;
        if (isNewUser) {
            userID = UUID.randomUUID().toString();
            userData = new UserData();
            try {
                recipe = availableRecipes.pop();
            } catch (NoSuchElementException e) {
                recipe = null;
            }
            userData.setRecipe(recipe);
            userDataMap.put(userID, userData);
            response.addCookie(new Cookie("user-id", userID));
        } else {
            recipe = userData.getRecipe();
        }
        model.addAttribute("userID", userID);
        model.addAttribute("recipe", recipe);
        model.addAttribute("userDataMap", userDataMap.toString());
        return "home";
    }

    @GetMapping("/share/{userID}")
    @ResponseBody
    public String share(@PathVariable String userID) {
        return "You want to restore the user ID \"" + userID + "\".";
    }

    @GetMapping("/clear")
    @ResponseBody
    public String clear() {
        return "You want to reset your user ID.";
    }

}
