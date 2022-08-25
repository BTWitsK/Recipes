package recipes.persistenceLayer;

import org.springframework.stereotype.Repository;
import recipes.businessLayer.Recipe;
import org.springframework.data.repository.CrudRepository;

/**
 * Recipe repository used to interact with database
 */
@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}

