package com.example.recipe_cal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipe_cal.model.Ingredient
import com.example.recipe_cal.ui.components.MacroPieChart
import com.example.recipe_cal.viewmodel.RecipeViewModel

@Composable
fun RecipeScreen(recipeViewModel: RecipeViewModel = viewModel()) {
    val ingredients by recipeViewModel.ingredients.collectAsState()
    val totals = recipeViewModel.getMacroTotals()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Total Calories: ${totals.totalCalories.toInt()} kcal",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        MacroPieChart(
            protein = totals.protein,
            carbs = totals.carbs,
            fat = totals.fat
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            recipeViewModel.addIngredient(
                Ingredient("Chicken Breast", 200f, 31f, 0f, 3.6f)
            )
        }) {
            Text("Add Test Chicken (200g)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(ingredients) { ingredient ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(ingredient.name)
                        Text("${ingredient.weightGrams}g")
                    }
                }
            }
        }
    }
}