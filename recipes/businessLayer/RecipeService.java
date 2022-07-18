package recipes.businessLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.persistenceLayer.RecipeRepository;

import java.util.Optional;

@Service
public class RecipeService {
    //todo: implement methods for searching recipe by name, searching by category,
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    public void addRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    public void deleteRecipe(Recipe recipe) {
        recipeRepository.delete(recipe);
    }
}
