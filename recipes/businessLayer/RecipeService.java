package recipes.businessLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.persistenceLayer.RecipeRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class used to interact with database
 */
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * Searches database for recipe using provided id
     * @param id long representing id of recipe
     * @return Optional containing recipe if it is found, empty Optional otherwise
     */
    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    /**
     * Finds recipes in the database containing name provided as a String
     * @param name String value containing name to search for
     * @return List containing Recipes which contain the name provided
     */
    public List<Recipe> getRecipesByName(String name) {
        ArrayList<Recipe> recipeList = new ArrayList<>();
        recipeRepository.findAll().forEach(recipeList::add);
        return recipeList.stream()
                .filter(recipe -> recipe.getName().toLowerCase().contains(name.toLowerCase()))
                .sorted(Comparator.comparing(Recipe::getDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Finds recipes from database using provided category
     * @param name String value of category to search for
     * @return List containing recipes that match the provided category name
     */
    public List<Recipe> getRecipesByCategory(String name) {
        ArrayList<Recipe> recipeList = new ArrayList<>();
        recipeRepository.findAll().forEach(recipeList::add);
        return recipeList.stream()
                .filter(recipe -> name.equalsIgnoreCase(recipe.getCategory()))
                .sorted(Comparator.comparing(Recipe::getDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Checks to see if the chef is the author of the recipe
     * @param recipe recipe object to check
     * @param chef chef to be checked if is author of recipe
     * @return true if the chef is the author of the recipe, false otherwise
     */
    public boolean isAuthor(Recipe recipe, Chef chef) {
        return recipe.getAuthor().getEmail().equals(chef.getEmail());
    }

    /**
     * Adds recipe to the database after updating when it was created and by whom
     * @param recipe recipe to add to database
     * @param author chef that created the recipe
     */
    public void addRecipe(Recipe recipe, Chef author) {
        recipe.setDate(LocalDateTime.now());
        recipe.setAuthor(author);
        recipeRepository.save(recipe);
    }

    /**
     * Deletes recipe provided from database
     * @param recipe recipe to delete from database
     */
    public void deleteRecipe(Recipe recipe) {
        recipeRepository.delete(recipe);
    }
}

