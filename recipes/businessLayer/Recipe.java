package recipes.businessLayer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "RECIPES")
public class Recipe {
    //todo: implement (not blank)fields date(min 8) and category
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    @JsonIgnore
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "INGREDIENTS", joinColumns = @JoinColumn(name = "id"))
    private List<String> ingredients;

    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "DIRECTIONS", joinColumns = @JoinColumn(name = "id"))
    private List<String> directions;

}

