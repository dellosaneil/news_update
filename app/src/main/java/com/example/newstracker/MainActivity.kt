package com.example.newstracker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newstracker.repository.Repository
import com.example.newstracker.viewModel.MainVM
import com.example.newstracker.viewModel.MainVMFactory
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var repository: Repository
    private lateinit var viewModelFactory : ViewModelProvider.Factory
    private lateinit var viewModel : MainVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repository = Repository()
        viewModelFactory = MainVMFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainVM::class.java)
        viewModel.retrieveArticles()

        viewModel.getArticles().observe(this, {
            if(it.isSuccessful){
                println(it.body()?.articles)
            }
        })
    }


}