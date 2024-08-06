package com.example.foodrecipe.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.foodrecipe.R
import com.example.foodrecipe.databinding.ActivityMealBinding
import com.example.foodrecipe.fragments.HomeFragment
import com.example.foodrecipe.viewmodel.HomeViewModel
import com.example.foodrecipe.viewmodel.MealViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var viewModel: MealViewModel

    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealYoutubeLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MealViewModel::class.java]

        getMealInformationFromIntent()
        setInformationInView()

        loadingCase()
        viewModel.getMealDetails(mealId)
        observerMealDetails()
        onYoutubeImageClick()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mealYoutubeLink))
            startActivity(intent)
        }
    }

    private fun observerMealDetails() {
        onResponseCase()
        lifecycleScope.launch {
            viewModel.mealDetails.collectLatest { meal ->
                // update UI with the meal data
                meal?.let {
                    binding.tvCategory.text = "Category: ${meal.strCategory}"
                    binding.tvArea.text = "Area: ${meal.strArea}"
                    binding.tvInstructionsDetails.text = meal.strInstructions
                    mealYoutubeLink = meal.strYoutube
                }

            }
        }
    }

    private fun setInformationInView() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imageMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase() {
        binding.progressBar.visibility = VISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.btnFavorites.visibility = View.INVISIBLE
        binding.tvInstructionsLabel.visibility = View.INVISIBLE
        binding.tvInstructionsDetails.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    private fun onResponseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.tvCategory.visibility = VISIBLE
        binding.tvArea.visibility = VISIBLE
        binding.btnFavorites.visibility = VISIBLE
        binding.tvInstructionsLabel.visibility = VISIBLE
        binding.tvInstructionsDetails.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
}