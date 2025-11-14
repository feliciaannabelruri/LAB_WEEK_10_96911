package com.example.lab_week_10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.database.TotalObject
import com.example.lab_week_10.viewmodels.TotalViewModel
import java.util.Date

class MainActivity : AppCompatActivity() {
    private val db by lazy { prepareDatabase() }

    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeValueFromDatabase()
        prepareViewModel()
    }

    override fun onStart() {
        super.onStart()
        // Show toast with last update date
        viewModel.total.value?.date?.let { date ->
            if (date.isNotEmpty()) {
                Toast.makeText(this, "Last updated: $date", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateText(totalObject: TotalObject) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, totalObject.value)
    }

    private fun prepareViewModel() {
        viewModel.total.observe(this) {
            updateText(it)
        }

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java, "total-database"
        )
            .fallbackToDestructiveMigration()  // Add this for version update
            .allowMainThreadQueries()
            .build()
    }

    private fun initializeValueFromDatabase() {
        val total = db.totalDao().getTotal(ID)
        if (total.isEmpty()) {
            db.totalDao().insert(Total(id = 1, total = TotalObject(0, "")))
        } else {
            viewModel.setTotal(total.first().total)
        }
    }

    override fun onPause() {
        super.onPause()
        val currentDate = Date().toString()
        viewModel.updateDate(currentDate)
        db.totalDao().update(Total(ID, viewModel.total.value!!))
    }

    companion object {
        const val ID: Long = 1
    }
}