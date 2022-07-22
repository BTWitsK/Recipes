package recipes.businessLayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
public class Chef implements UserDetails {
    //implemented the parent child relation with "elementCollection" instead of one to many annotation
    @Id
    @Email
    @NotNull
    @Column(name = "ID")
    private String username;

    @NotBlank
    @Min(8)
    @Column(name = "PASSWORD")
    private String password;

    @ElementCollection
    @CollectionTable(name = "RECIPES", joinColumns = @JoinColumn(name = "ID"))
    private List<Recipe> recipeList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
