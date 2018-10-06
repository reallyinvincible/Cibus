package com.example.bakingapp.models;

import java.util.List;

public class FetchedRecipeList {

    private final List<Recipe> recipes;

    public FetchedRecipeList(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
