package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import recipes.businessLayer.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@Validated
public class recipeController {
    @Autowired
    ChefService chefService;

    @Autowired
    RecipeService recipeService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable long id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (recipe.isPresent()) {
            return new ResponseEntity<>(recipeService.getRecipeById(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/recipe/search")
    public ResponseEntity<?> searchForRecipe(@RequestParam(name = "category", required = false) String category,
                                             @RequestParam(name = "name", required = false) String name) {
        if ((category == null && name == null) || (category != null && name != null)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (category == null) {
            return new ResponseEntity<>(
                    Optional.of(recipeService.getRecipesByName(name))
                            .orElse(Collections.emptyList()), HttpStatus.OK);
        }

        return new ResponseEntity<>(
                Optional.of(recipeService.getRecipesByCategory(category))
                        .orElse(Collections.emptyList()), HttpStatus.OK);
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<?> postRecipe(@Valid @RequestBody Recipe recipe) {
        recipeService.addRecipe(recipe);
        return new ResponseEntity<>(Map.of("id", recipe.getId()), HttpStatus.OK);
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody Chef user) {

        if (chefService.notRegistered(user)){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            chefService.addUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    //Todo: implement registration endpoint

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable long id, @Valid @RequestBody Recipe recipe) {
        Optional<Recipe> oldRecipe = recipeService.getRecipeById(id);

        if (oldRecipe.isPresent()) {
            oldRecipe.get().setName(recipe.getName());
            oldRecipe.get().setCategory(recipe.getCategory());
            oldRecipe.get().setDescription(recipe.getDescription());
            oldRecipe.get().setDirections(recipe.getDirections());
            oldRecipe.get().setIngredients(recipe.getIngredients());
            recipeService.addRecipe(oldRecipe.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

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
