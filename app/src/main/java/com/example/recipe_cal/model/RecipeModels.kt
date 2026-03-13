package com.example.recipe_cal.model

data class Ingredient(
    val name: String,
    val weightGrams: Float,
    val proteinPer100g: Float,
    val carbsPer100g: Float,
    val fatPer100g: Float
) {
    val totalProtein: Float get() = (weightGrams / 100f) * proteinPer100g
    val totalCarbs: Float get() = (weightGrams / 100f) * carbsPer100g
    val totalFat: Float get() = (weightGrams / 100f) * fatPer100g
}

data class MacroTotals(val protein: Float, val carbs: Float, val fat: Float) {
    val totalCalories: Float get() = (protein * 4f) + (carbs * 4f) + (fat * 9f)
}