package recipes.businessLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.persistenceLayer.ChefRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ChefService {
    private final ChefRepository chefRepository;

    @Autowired
    public ChefService(ChefRepository chefRepository) {
        this.chefRepository = chefRepository;
    }

    public Optional<Chef> getChefByUserName(String email) {
        return chefRepository.findById(email);
    }

    public void addUser(Chef user) {
        chefRepository.save(user);
    }

    public void addRecipe(Chef user, Recipe recipe) {
        user.getRecipeList().add(recipe);
        chefRepository.save(user);
    }

    public List<Recipe> getRecipes(Chef user) {
        return user.getRecipeList();
    }
}
