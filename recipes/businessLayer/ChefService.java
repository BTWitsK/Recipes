package recipes.businessLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import recipes.persistenceLayer.ChefRepository;

import java.util.Optional;

/**
 * This is a Service class to interact with the Chef Repository
 * @author Kae Mattis
 */
@Service
public class ChefService implements UserDetailsService {
    private final ChefRepository chefRepository;

    @Autowired
    public ChefService(ChefRepository chefRepository) {
        this.chefRepository = chefRepository;
    }

    /**
     * Searches database for Chef entity
     * @param email String value representing the user's username
     *              in this case the Chef's email address
     * @return Optional of Chef class containing the specified User or empty optional if not found
     */
    public Optional<Chef> getChefByUserName(String email) {
        return chefRepository.findById(email);
    }

    /**
     * Checks database for User/Chef
     * @param user Chef class object
     * @return boolean true if Chef is not currently in the database, false otherwise
     */
    public boolean notRegistered(Chef user) {
        return chefRepository.findById(user.getUsername()).isEmpty();
    }

    /**
     * Adds the provided User/Chef to the database
     * @param user Chef class object
     */
    public void addUser(Chef user) {
        chefRepository.save(user);
    }

    /**
     *
     * @param email the username identifying the user whose data is required.
     * @return Chef object using provided email
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Chef> chefCheck = getChefByUserName(email);

        if (chefCheck.isEmpty()) {
            throw new UsernameNotFoundException("Not found: " + email);
        }

        return chefCheck.get();
    }
}
