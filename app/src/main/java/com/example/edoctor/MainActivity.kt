package com.example.edoctor

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.edoctor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val bottomNavigationView = binding.bNav
        val fragmentManager = supportFragmentManager

        // Проверка, есть ли сохраненный фрагмент при перевороте
        if (savedInstanceState == null) {
            // Если нет сохраненного состояния, устанавливаем HomeFragment
            currentFragment = HomeFragment()
            fragmentManager.beginTransaction().replace(R.id.place_holder, currentFragment!!).commit()
        } else {
            // Если состояние сохранено, восстанавливаем фрагмент
            currentFragment = fragmentManager.findFragmentById(R.id.place_holder)
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.bHome -> HomeFragment()
                R.id.bAdd -> AddFragment()
                R.id.bProfile -> ProfileFragment()
                else -> null
            }
            if (fragment != null) {
                currentFragment = fragment // Обновляем текущий фрагмент
                fragmentManager.beginTransaction().replace(R.id.place_holder, fragment).commit()
            }
            true
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Обновление макета для альбомной ориентации
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Обновление макета для портретной ориентации
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем текущий фрагмент
        outState.putString("currentFragment", currentFragment?.javaClass?.name)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Восстанавливаем текущий фрагмент
        val fragmentName = savedInstanceState.getString("currentFragment")
        val fragment = when (fragmentName) {
            HomeFragment::class.java.name -> HomeFragment()
            AddFragment::class.java.name -> AddFragment()
            ProfileFragment::class.java.name -> ProfileFragment()
            else -> HomeFragment()
        }
        currentFragment = fragment
    }
}
