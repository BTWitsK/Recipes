package recipes.businessLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import recipes.persistenceLayer.ChefRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ChefService implements UserDetailsService {
    private final ChefRepository chefRepository;

    @Autowired
    public ChefService(ChefRepository chefRepository) {
        this.chefRepository = chefRepository;
    }

    public Optional<Chef> getChefByUserName(String email) {
        return chefRepository.findById(email);
    }

    public boolean notRegistered(Chef user) {
        return chefRepository.findById(user.getUsername()).isEmpty();
    }

    public void addUser(Chef user) {
        chefRepository.save(user);
    }

    public void addRecipe(Chef user, Recipe recipe) {
        user.getRecipeList().add(recipe);
        chefRepository.save(user);
    }

    public boolean canUpdateRecipe(Chef user, Recipe recipe) {
        if (user.getRecipeList().contains(recipe)) {
            user.getRecipeList().remove(recipe);
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Chef> chefCheck = getChefByUserName(email);

        if (chefCheck.isEmpty()) {
            throw new UsernameNotFoundException("Not found: " + email);
        }

        return chefCheck.get();
    }
}
