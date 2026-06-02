package com.piring.sehat.api.nutrition.service;

import com.piring.sehat.api.nutrition.model.Nutrition;
import com.piring.sehat.api.nutrition.repository.NutritionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NutritionService {

    @Autowired
    private NutritionRepository nutritionRepository;

    public List<Nutrition> searchFoodByName(String query) {
        return nutritionRepository.findByNameContainingIgnoreCase(query);
    }
}