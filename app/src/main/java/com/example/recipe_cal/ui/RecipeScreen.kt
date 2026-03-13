package com.example.recipe_cal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipe_cal.ui.components.MacroPieChart
import com.example.recipe_cal.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(recipeViewModel: RecipeViewModel = viewModel()) {
    val ingredients by recipeViewModel.ingredients.collectAsState()
    val totals = remember(ingredients) { recipeViewModel.getMacroTotals() }

    // --- NEW: UI State variables to hold what the user types ---
    var foodNameInput by remember { mutableStateOf("") }
    var weightInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Macro Calculator", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dashboard Card (Unchanged)
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MacroPieChart(
                        protein = totals.protein, carbs = totals.carbs, fat = totals.fat,
                        modifier = Modifier.size(100.dp)
                    )
                    Column(horizontalAlignment = Alignment.Start) {
                        Text("Total: ${totals.totalCalories.toInt()} kcal", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("🔴 Protein: ${totals.protein.toInt()}g", fontWeight = FontWeight.Medium)
                        Text("🟢 Carbs: ${totals.carbs.toInt()}g", fontWeight = FontWeight.Medium)
                        Text("🟡 Fat: ${totals.fat.toInt()}g", fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- NEW: Input Section ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = foodNameInput,
                    onValueChange = { foodNameInput = it },
                    label = { Text("Food (e.g., Egg)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it },
                    label = { Text("Weight (g)") },
                    modifier = Modifier.weight(0.6f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val weight = weightInput.toFloatOrNull()
                    if (weight != null && weight > 0) {
                        val success = recipeViewModel.addFoodByName(foodNameInput, weight)
                        if (success) {
                            // Clear inputs on success
                            foodNameInput = ""
                            weightInput = ""
                            errorMessage = ""
                        } else {
                            errorMessage = "Not in database. Try: white rice, egg, apple"
                        }
                    } else {
                        errorMessage = "Please enter a valid weight"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Calculate & Add Food")
            }

            // Show error message if something goes wrong
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Polished Ingredient List (Unchanged)
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(ingredients) { ingredient ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(ingredient.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Text("${ingredient.weightGrams}g", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("P: ${ingredient.totalProtein.toInt()}g", color = MaterialTheme.colorScheme.error)
                                Text("C: ${ingredient.totalCarbs.toInt()}g", color = MaterialTheme.colorScheme.primary)
                                Text("F: ${ingredient.totalFat.toInt()}g", color = MaterialTheme.colorScheme.tertiary)
                            }
                        }
                    }
                }
            }
        }
    }
}