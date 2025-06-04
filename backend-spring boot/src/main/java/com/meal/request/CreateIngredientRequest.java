package com.meal.request;

import lombok.Data;

@Data
public class CreateIngredientRequest {


    private String name;
    private Long categoryId;
    private Long restaurantId;
}
