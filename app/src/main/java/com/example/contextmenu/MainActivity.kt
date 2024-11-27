package com.example.contextmenu

import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contextmenu.databinding.ActivityMainBinding
import java.util.Locale
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentViewId: Int? = 0
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
        binding.apply {
            registerForContextMenu(gradeET)
            registerForContextMenu(randomNumTV)
            randomBTN.setOnClickListener {
                randomNumTV.text = String.format(Locale.getDefault(),"%d", Random.nextInt(1, 51))
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        currentViewId = v?.id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_color_quality -> currentViewId?.let {
                when (currentViewId) {
                    R.id.gradeET -> processGrade()
                    R.id.random_numTV -> processRandomNum()
                    else -> throw IllegalStateException("Wrong id of target view")
                }
            }
            R.id.menu_exit -> finish()
            else -> return super.onContextItemSelected(item)
        }
        return true
    }

    private fun processGrade(){
        binding.apply {
            if ("[1-5]".toRegex().matches(gradeET.text.toString())) {
                val color = when(gradeET.text.toString().toInt()) {
                    1 -> getColor(R.color.orange)
                    2 -> getColor(R.color.yellow)
                    3 -> getColor(R.color.green)
                    4 -> getColor(R.color.blue)
                    5 -> getColor(R.color.red)
                    else -> throw IllegalStateException("Error in regular expression")
                }
                gradeET.setBackgroundColor(color)
            } else {
                makeToast(R.string.enter_number_between_1_and_5)
            }
        }
    }

    private fun processRandomNum(){
        binding.apply {
            if (randomNumTV.text.toString().isNotBlank()) {
                val color = when(randomNumTV.text.toString().toInt()) {
                    in 1..10 -> getColor(R.color.red)
                    in 11..20 -> getColor(R.color.orange)
                    in 21..30 -> getColor(R.color.yellow)
                    in 31..40 -> getColor(R.color.green)
                    in 41..50 -> getColor(R.color.blue)
                    else -> throw IllegalStateException("Error in regular expression")
                }
                randomNumTV.setBackgroundColor(color)
            } else {
                makeToast(R.string.generate_number_first)
            }
        }
    }

    private fun makeToast(@StringRes str: Int) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }
}