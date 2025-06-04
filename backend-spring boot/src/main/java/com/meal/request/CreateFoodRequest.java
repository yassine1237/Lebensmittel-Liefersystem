package com.meal.request;



import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFoodRequest {
    private String name;
    private String description;
    private Long price;
    private Long categoryId;  // Changed to categoryId for easier handling
    private List<String> images;
    private Long restaurantId;
    private boolean vegetarian;
    private boolean seasonal;
    private List<Long> ingredientIds;
    // Changed to ingredientIds for easier handling


}
