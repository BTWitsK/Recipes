package recipes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Random;

@SpringBootApplication
public class RecipesApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecipesApplication.class, args);
    }
}

@RestController
class recipeController {
    HashMap<Long, Recipe> recipeMap = new HashMap<>();
    long id;

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable long id) {
        if (recipeMap.containsKey(id)) {
            return new ResponseEntity<>(recipeMap.get(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<?> postRecipe(@RequestBody Recipe recipe) {
        do {
            id = new Random().nextLong(100) * recipe.hashCode() % 31;
        } while (recipeMap.containsKey(id));

        recipeMap.put(id, recipe);

        return new ResponseEntity<>(String.format("{id= %d}", id), HttpStatus.OK);
    }

}

