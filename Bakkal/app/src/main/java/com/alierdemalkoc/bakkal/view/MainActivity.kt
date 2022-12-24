package com.alierdemalkoc.bakkal.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.alierdemalkoc.bakkal.R
import com.alierdemalkoc.bakkal.databinding.ActivityMainBinding
import com.alierdemalkoc.bakkal.fragment.StockFragmentDirections


class MainActivity : AppCompatActivity() {
    private lateinit var navigationController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navigationController = navHostFragment.navController


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.my_menu,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.add_product -> {
                addProduct()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.fragmentContainerView)

        return navController.navigateUp()
    }

    private fun addProduct() {
        val actionToAddFromStock = StockFragmentDirections.goToAddFromStock("new")
        Navigation.findNavController(this, R.id.fragmentContainerView).navigate(actionToAddFromStock)

    }
    }

