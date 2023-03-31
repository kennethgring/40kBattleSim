package com.example.demo;

/**
 * Container for user data.
 */
public class UserData {

    private String recipe = null;

    public String getRecipe() {
        return this.recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String toString() {
        String recipeRepr = recipe != null ? "recipe=" + recipe : "";
        return "UserData(" + recipeRepr + ")";
    }

}
