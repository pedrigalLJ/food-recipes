package com.example.foodrecipe.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.foodrecipe.activities.MealActivity
import com.example.foodrecipe.databinding.FragmentHomeBinding
import com.example.foodrecipe.pojo.Meal
import com.example.foodrecipe.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal

    companion object {
        const val MEAL_ID = "com.example.foodrecipe.fragments.idMeal"
        const val MEAL_NAME = "com.example.foodrecipe.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.foodrecipe.fragments.thumbMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
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

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

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