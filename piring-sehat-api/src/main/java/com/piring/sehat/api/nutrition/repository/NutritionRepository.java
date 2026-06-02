package com.piring.sehat.api.nutrition.repository;

import com.piring.sehat.api.nutrition.model.Nutrition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NutritionRepository extends JpaRepository<Nutrition, Long> {
    // Ini sama dengan query: SELECT * FROM nutrition WHERE name ILIKE '%name%'
    List<Nutrition> findByNameContainingIgnoreCase(String name);
}