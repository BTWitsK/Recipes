package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import recipes.businessLayer.Recipe;
import recipes.businessLayer.RecipeService;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@Validated
//todo: implement two new endpoints
public class recipeController {

    @Autowired
    RecipeService recipeService;

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable long id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (recipe.isPresent()) {
            return new ResponseEntity<>(recipeService.getRecipeById(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/recipe/search")
    //todo: implement searching by category / name
    //take two mutually exclusive params,
    //category: returns json array of all recipes, search is case-insensitive sort by date newest first
    //name return all recipe with CONTAINS name, case insensitive sort by date
    //if not found should return empty Json array, if params incorrect return 400 or params not valid
    //if search ok return code 200 (ok)

    @PostMapping("/api/recipe/new")
    public ResponseEntity<?> postRecipe(@Valid @RequestBody Recipe recipe) {
        recipeService.addRecipe(recipe);
        return new ResponseEntity<>(Map.of("id", recipe.getId()), HttpStatus.OK);
    }

    @PutMapping("/api/recipe/{id}")
    //todo: implement recipe updating
    //update recipe and date, return 204, if recipe not found return 404
    //return 400 badrequest if recipe doesn't follow restrictions

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable long id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);

        if (recipe.isPresent()) {
            recipeService.deleteRecipe(recipe.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
