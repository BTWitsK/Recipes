package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import recipes.businessLayer.*;

import javax.validation.Valid;
import java.util.*;


/**
 * Controller class to handle all requests to the server
 * @author Kae Mattis
 */
@RestController
@Validated
public class recipeController {
    @Autowired
    ChefService chefService;

    @Autowired
    RecipeService recipeService;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * Looks for specific recipe using the provided id
     * @param id Long provided as a Path Variable
     * @return JSON object of recipe if it is found in database, 404 Status otherwise
     */
    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable long id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (recipe.isPresent()) {
            return new ResponseEntity<>(recipeService.getRecipeById(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Looks for recipe using Request Parameters
     * @param category type of recipe to look for in the database
     * @param name specific recipe to look for in the database
     * @return JSON array representing all recipes containing the specified category, or JSON array
     * representing al the recipes containing the specified name, if no recipes found returns an empty array
     */
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

    /**
     * Adds new recipe to database
     * @param user authenticated user
     * @param recipe JSON object representing the recipe to be added to database
     * @return JSON object with recipe id that has been added OK HTTP status
     */
    @PostMapping("/api/recipe/new")
    public ResponseEntity<?> postRecipe(Authentication user, @Valid @RequestBody Recipe recipe) {
        Chef currentChef = (Chef) user.getPrincipal();
        Chef author = chefService.getChefByUserName(currentChef.getUsername()).get();

        recipeService.addRecipe(recipe, author);
        return new ResponseEntity<>(Map.of("id", recipe.getId()), HttpStatus.OK);
    }

    /**
     * Adds user to database
     * @param user JSON object representing the user to be added
     * @return OK status if user object meets the required specifications, bad request otherwise
     */
    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody Chef user) {

        if (chefService.notRegistered(user)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            chefService.addUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Updates recipe already present in the database if the current user is the author of said recipe
     * @param user Authenticated user
     * @param id long representing the id of the recipe to be updated
     * @param recipe JSON object representing the information of the new recipe that is to be updated
     * @return Forbidden if the user is not the creator of the recipe, no content if the recipe was updated,
     * Not found if there was no recipe found with that id
     */
    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<?> updateRecipe(Authentication user,
                                          @PathVariable long id, @Valid @RequestBody Recipe recipe) {
        Optional<Recipe> oldRecipe = recipeService.getRecipeById(id);
        Chef currentChef = (Chef) user.getPrincipal();
        Chef author = chefService.getChefByUserName(currentChef.getUsername()).get();

        if (oldRecipe.isPresent()) {
            if (recipeService.isAuthor(oldRecipe.get(), author)) {
                oldRecipe.get().setName(recipe.getName());
                oldRecipe.get().setCategory(recipe.getCategory());
                oldRecipe.get().setDescription(recipe.getDescription());
                oldRecipe.get().setDirections(recipe.getDirections());
                oldRecipe.get().setIngredients(recipe.getIngredients());
                recipeService.addRecipe(oldRecipe.get(), author);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes recipe from database
     * @param user Authorized user
     * @param id long representing the id of the recipe to be deleted
     * @return No content if deletion is successful, forbidden if the current user
     * didn't create the recipe, not found if there is no recipe with the id
     */
    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<?> deleteRecipe(Authentication user, @PathVariable long id) {
        Chef currentChef = (Chef) user.getPrincipal();
        Chef author = chefService.getChefByUserName(currentChef.getUsername()).get();
        Optional<Recipe> recipe = recipeService.getRecipeById(id);

        if (recipe.isPresent()) {
            if (recipeService.isAuthor(recipe.get(), author)) {
                recipeService.deleteRecipe(recipe.get());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
