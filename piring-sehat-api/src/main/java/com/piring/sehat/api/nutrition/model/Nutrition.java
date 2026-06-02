package com.piring.sehat.api.nutrition.model;

import jakarta.persistence.*;

@Entity
@Table(name = "nutrition") // Pastikan nama tabel ini sama persis dengan di Supabase
public class Nutrition {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private Integer calories;
    private Double proteins;
    private Double carbohydrate;
    private Double fat;
    private String image;

    // ==========================================
    // GETTER & SETTER (Wajib ada agar terbaca sebagai JSON)
    // ==========================================
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }

    public Double getProteins() { return proteins; }
    public void setProteins(Double proteins) { this.proteins = proteins; }

    public Double getCarbohydrate() { return carbohydrate; }
    public void setCarbohydrate(Double carbohydrate) { this.carbohydrate = carbohydrate; }

    public Double getFat() { return fat; }
    public void setFat(Double fat) { this.fat = fat; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}