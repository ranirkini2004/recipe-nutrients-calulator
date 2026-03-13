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

    fun addIngredient(ingredient: Ingredient) {
        _ingredients.value = _ingredients.value + ingredient
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