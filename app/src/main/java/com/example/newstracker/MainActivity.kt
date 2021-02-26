package com.example.newstracker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newstracker.databinding.ActivityMainBinding
import com.example.newstracker.repository.RetrofitRepository
import com.example.newstracker.viewModel.result.ResultVM
import com.example.newstracker.viewModel.result.ResultVMFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var model : ResultVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeBottomNavigation()
        val repository = RetrofitRepository()
        val viewModelFactory = ResultVMFactory(repository)
        model =  ViewModelProvider(this, viewModelFactory).get(ResultVM::class.java)
    }

    // function to connect Navigation Component to Bottom Navigation View
    private fun initializeBottomNavigation() {
        val navController = findNavController(R.id.fragment)
        binding.bottomNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.addUserPreference, R.id.newsArticlesFragment, R.id.webViewFragment -> {
                    binding.bottomNavigation.visibility = View.GONE
                }
                else -> binding.bottomNavigation.visibility = View.VISIBLE
            }
        }
    }

}