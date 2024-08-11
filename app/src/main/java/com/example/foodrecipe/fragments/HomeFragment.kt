package com.example.foodrecipe.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodrecipe.activities.MealActivity
import com.example.foodrecipe.adapters.CategoryMealAdapter
import com.example.foodrecipe.adapters.PopularMealAdapter
import com.example.foodrecipe.databinding.FragmentHomeBinding
import com.example.foodrecipe.pojo.Meal
import com.example.foodrecipe.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularMealAdapter: PopularMealAdapter
    private lateinit var categoryMealAdapter: CategoryMealAdapter

    companion object {
        const val MEAL_ID = "com.example.foodrecipe.fragments.idMeal"
        const val MEAL_NAME = "com.example.foodrecipe.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.foodrecipe.fragments.thumbMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        popularMealAdapter = PopularMealAdapter()
        categoryMealAdapter = CategoryMealAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observePopularItems()
        onPopularItemClick()

        prepareCategoryItemsRecyclerView()
        viewModel.getCategories()
        observeCategoryItems()

    }

    private fun prepareCategoryItemsRecyclerView() {
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealAdapter
        }
    }

    private fun observeCategoryItems() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.categoryMeal.collectLatest { categories ->
                if (categories != null) {
                    categoryMealAdapter.setCategoryList(categories)
                }
            }
        }
    }

    private fun onPopularItemClick() {
        popularMealAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.rvPopularMeals.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMealAdapter
        }
    }

    private fun observePopularItems() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.popularMeal.collectLatest { meals ->
                // 'meals' is now a List<MealsByCategory>?
                popularMealAdapter.setMeals(
                    mealList = if (meals != null) ArrayList(meals) else ArrayList()
                )
            }
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent )
        }
    }

    private fun observerRandomMeal() {
        viewLifecycleOwner.lifecycleScope
            .launch {
                viewModel.randomMealData
                    .collectLatest { meal ->
                        // update UI with the meal data
                        meal?.let {
                            Glide.with(this@HomeFragment)
                                .load(it.strMealThumb)
                                .into(binding.imgRandomMeal)

                            randomMeal = meal
                        }
                    }
            }
    }


}