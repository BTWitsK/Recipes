package recipes.persistenceLayer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.businessLayer.Chef;

/**
 * Chef repository used to interact with database
 */
@Repository
public interface ChefRepository extends CrudRepository<Chef, String> {
}
