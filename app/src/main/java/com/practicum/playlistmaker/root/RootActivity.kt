package com.practicum.playlistmaker.root

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityRootBinding
import kotlinx.coroutines.flow.combine

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivityRootBinding.inflate(inflater)
        setContentView(binding.root)

        bottomNavigationManager()
    }

    private fun bottomNavigationManager() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.playerFragment -> hideBottomNavigation()
                R.id.playlistCreationFragment -> hideBottomNavigation()
                R.id.playlistFragment -> hideBottomNavigation()
                else -> showBottomNavigation()
            }
        }
    }

    private fun hideBottomNavigation() {
        binding.bottomNavigationView.isVisible = false
        binding.divider.isVisible = false
    }

    private fun showBottomNavigation() {
        binding.bottomNavigationView.isVisible = true
        binding.divider.isVisible = true
    }
}

fun Context.dpToPx(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics
    ).toInt()
}

fun Context.getDeclination(trackCount: Int): String {
    return when {
        trackCount % 10 == 1 -> "трек"
        trackCount % 10 in 2..4 -> "трека"
        trackCount % 100 in 11..19 -> "треков"
        else -> "треков"
    }
}

fun Context.timeStringToMillis(timeString: String): Long {
    val parts = timeString.split(":")
    val minutes = parts[0].toLong()
    val seconds = parts[1].toLong()

    return (minutes * 60 + seconds) * 1000
}

fun Context.isDarkTheme(): Boolean {
    return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}