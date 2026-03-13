package com.example.recipe_cal.viewmodel

import androidx.lifecycle.ViewModel
import com.example.recipe_cal.model.Ingredient
import com.example.recipe_cal.model.MacroTotals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipeViewModel : ViewModel() {

    private val _ingredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val ingredients: StateFlow<List<Ingredient>> = _ingredients.asStateFlow()

    // A mini local database of macros per 100g (Protein, Carbs, Fat)
    private val foodDatabase = mapOf(
        "chicken breast" to Triple(31f, 0f, 3.6f),
        "white rice" to Triple(2.7f, 28f, 0.3f),
        "egg" to Triple(13f, 1.1f, 11f),
        "apple" to Triple(0.3f, 14f, 0.2f),
        "almonds" to Triple(21f, 22f, 49f),
        "oats" to Triple(16.9f, 66.3f, 6.9f)
    )

    // Searches the database and adds the ingredient if found
    fun addFoodByName(name: String, weight: Float): Boolean {
        val query = name.trim().lowercase()
        val macros = foodDatabase[query]

        return if (macros != null) {
            // Capitalize the first letter nicely for the UI
            val formattedName = name.trim().replaceFirstChar { it.uppercase() }
            val newIngredient = Ingredient(
                name = formattedName,
                weightGrams = weight,
                proteinPer100g = macros.first,
                carbsPer100g = macros.second,
                fatPer100g = macros.third
            )
            // Update the list
            _ingredients.value = _ingredients.value + newIngredient
            true // Tell the UI it was successful
        } else {
            false // Tell the UI the food wasn't found
        }
    }

    fun getMacroTotals(): MacroTotals {
        val currentList = _ingredients.value
        return MacroTotals(
            protein = currentList.sumOf { it.totalProtein.toDouble() }.toFloat(),
            carbs = currentList.sumOf { it.totalCarbs.toDouble() }.toFloat(),
            fat = currentList.sumOf { it.totalFat.toDouble() }.toFloat()
        )
    }
}