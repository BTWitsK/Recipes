package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import recipes.businessLayer.ChefService;

/**
 * Class implements Web Security which is used to authorize users
 * as well as encrypt passwords
 */
@EnableWebSecurity
public class WebSecurityConfigurerImp extends WebSecurityConfigurerAdapter {

    @Autowired
    ChefService chefService;

    @Override
    protected void configure(AuthenticationManagerBuilder authenticator) throws Exception {
        authenticator.userDetailsService(chefService)
                .passwordEncoder(getEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("api/register", "/actuator/shutdown").permitAll()
                .mvcMatchers("/api/recipe/*", "/actuator/*").authenticated()
                .and()
                .csrf().disable()
                .httpBasic();
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
