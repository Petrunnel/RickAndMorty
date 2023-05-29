package com.petrunnel.rickandmorty.ui

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.petrunnel.rickandmorty.R
import com.petrunnel.rickandmorty.databinding.ActivityMainBinding
import com.petrunnel.rickandmorty.extentions.viewBinding
import com.petrunnel.rickandmorty.ui.main.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, MainFragment())
            .commit()

        binding.ivToolbarLogo.setOnClickListener {
            showAbout()
        }
    }

    private fun showAbout() {
        AlertDialog
            .Builder(this)
            .setCancelable(false)
            .setTitle(R.string.dialog_about_title)
            .setMessage(R.string.dialog_about_message)
            .setPositiveButton(R.string.dialog_positive_button_text) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}