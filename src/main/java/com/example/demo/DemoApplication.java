package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/")
    public String greeting(
            @RequestParam(name="name", defaultValue="World") String name,
            Model model) {
        model.addAttribute("name", name);
        return "greeting";
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
