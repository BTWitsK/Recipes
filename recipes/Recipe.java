package recipes;

import lombok.*;

@Data
public class Recipe {
    String name;
    String description;
    String[] ingredients;
    String[] directions;
}