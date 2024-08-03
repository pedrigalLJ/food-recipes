package com.example.foodrecipe.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.foodrecipe.databinding.FragmentHomeBinding
import com.example.foodrecipe.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewmodel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(this)[HomeViewModel::class.java]
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

        viewmodel.getRandomMeal()
        observerRandomMeal()

    }

    private fun observerRandomMeal() {
        viewLifecycleOwner.lifecycleScope
            .launch {
                viewmodel.randomMealData
                    .collectLatest { meal ->
                        // update UI with the meal data
                        meal?.let {
                            Glide.with(this@HomeFragment)
                                .load(it.strMealThumb)
                                .into(binding.imgRandomMeal)
                        }
                    }
            }
    }


}