package recipes.persistenceLayer;

import org.springframework.stereotype.Repository;
import recipes.businessLayer.Recipe;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
