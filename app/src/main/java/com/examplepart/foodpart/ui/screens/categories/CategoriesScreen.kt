package com.examplepart.foodpart.ui.screens.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.examplepart.foodpart.R
import com.examplepart.foodpart.datamodel.foodCategories
import com.examplepart.foodpart.ui.common.FoodPartAppBar
import com.examplepart.foodpart.ui.common.FoodsList
import com.examplepart.foodpart.ui.common.ShowError
import com.examplepart.foodpart.core.AppScreens


@Composable
fun CategoriesScreen(navController: NavController) {
    val foodCategories = foodCategories
    var selectedCategoryIndex by remember { mutableStateOf<Int?>(0) }
    var selectedSubCategoryIndex by remember { mutableStateOf<Int?>(0) }
    Scaffold(
        topBar = {
            FoodPartAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 16.dp, 16.dp, 0.dp),
                title = stringResource(id = R.string.foodPart),
                showStartIcon = false,
                showEndIcon = false,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(foodCategories) { index, foodCategoryModel ->
                    val startPadding = if (index == 0) 16.dp else 0.dp
                    val endPadding = if (index == foodCategories.size - 1) 16.dp else 0.dp

                    FoodCategoryChip(
                        modifier = Modifier.padding(start = startPadding, end = endPadding),
                        foodCategoryModel = foodCategoryModel,
                        isSelected = selectedCategoryIndex == index
                    ) {
                        selectedCategoryIndex = index
                    }
                }
            }

            Divider(
                modifier = Modifier
                    .padding(vertical = 5.dp, horizontal = 16.dp)
                    .height(0.5.dp),
                color = MaterialTheme.colors.onSurface
            )

            val selectedCategory = selectedCategoryIndex?.let { foodCategories.getOrNull(it) }
            val hasSubcategories = selectedCategory?.subCategories?.isNotEmpty() == true

            selectedCategory?.let { category ->
                Column {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(category.subCategories) { index, subCategoryModel ->
                            val startPadding = if (index == 0) 16.dp else 0.dp
                            val endPadding = if (index == foodCategories.size - 1) 16.dp else 0.dp

                            SubFoodCategoryChip(
                                modifier = Modifier.padding(start = startPadding, end = endPadding),
                                subFoodCategoryModel = subCategoryModel,
                                isSelected = selectedSubCategoryIndex == index
                            ) {
                                selectedSubCategoryIndex = index
                            }
                        }
                    }
                    if (hasSubcategories) {
                        Divider(
                            modifier = Modifier
                                .padding(vertical = 5.dp, horizontal = 16.dp)
                                .height(0.5.dp),
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }
                val selectedSubCategory =
                    selectedSubCategoryIndex?.let { category.subCategories.getOrNull(it) }
                selectedSubCategory?.let { subCategory ->
                    if (subCategory.foods.isNotEmpty()) {
                        FoodsList(
                            items = subCategory.foods,
                        ) {
                            navController.navigate(AppScreens.FoodDetail.createRoute(it))
                        }
                    } else {
                        ShowError(
                            errorMessage = stringResource(id = R.string.foodCategoriesNotFound),
                            buttonTitle = stringResource(id = R.string.retry)
                        ) {
                            //doRetry
                        }
                    }
                }
            }
        }
    }


}

