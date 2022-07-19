package recipes.businessLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.persistenceLayer.RecipeRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    public List<Recipe> getRecipesByName(String name) {
        //needs testing
        ArrayList<Recipe> recipeList = new ArrayList<>();
        recipeRepository.findAll().forEach(recipeList::add);
        return recipeList.stream()
                .filter(recipe -> recipe.getName().toLowerCase().contains(name.toLowerCase()))
                .sorted(Comparator.comparing(Recipe::getDate))
                .collect(Collectors.toList());
    }

    public List<Recipe> getRecipesByCategory(String name) {
        //needs testing
        ArrayList<Recipe> recipeList = new ArrayList<>();
        recipeRepository.findAll().forEach(recipeList::add);
        return recipeList.stream()
                .filter(recipe -> name.equalsIgnoreCase(recipe.getCategory()))
                .sorted(Comparator.comparing(Recipe::getDate))
                .collect(Collectors.toList());
    }

    public void addRecipe(Recipe recipe) {
        recipe.setDate(LocalDate.now());
        recipeRepository.save(recipe);
    }

    public void deleteRecipe(Recipe recipe) {
        recipeRepository.delete(recipe);
    }
}
