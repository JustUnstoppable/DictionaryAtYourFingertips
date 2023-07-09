package com.example.myvocabulary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myvocabulary.databinding.ActivityStreakBinding
import com.example.myvocabulary.streakDB.*
import com.squareup.timessquare.CalendarPickerView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StreakActivity : AppCompatActivity() {
    private lateinit var streakViewModel: StreakViewModel
    lateinit var binding:ActivityStreakBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@StreakActivity, R.layout.activity_streak)
        val nextYear: Calendar = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, 1)
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val streakDao= streakDatabase.getInstance(this).streakDao
        val streakRepository= streakRepository(streakDao)
        val factory=streakViewModelFactory(streakRepository)
        streakViewModel=ViewModelProvider(this,factory)[StreakViewModel::class.java]
        binding.streakViewModel=streakViewModel
        binding.lifecycleOwner = this

        lifecycleScope.launch {

            //streakViewModel.insert(streak(0,Date()))

                streakViewModel.streakList.observe(this@StreakActivity,
                    androidx.lifecycle.Observer { items ->
                        items.let {
                            val arr = it

                            for (a in arr) {
                                dateStrings.add(a.streakDate)
                            }

                            for (dateString in dateStrings) {
                                      sdf.parse(dateString)
                                      dates.add(sdf.parse(dateString))
                              }
                            binding.calendar.init(sdf.parse("2023-01-01"), nextYear.time)
                                .inMode(CalendarPickerView.SelectionMode.MULTIPLE)
                                .withSelectedDates(dates)

                        }

                    })
            }


            //val dateStrings: MutableList<String> = ArrayList()
//        dateStrings.add("2023-01-25")
//        dateStrings.add("2023-01-28")
//        dateStrings.add("2023-01-27")
//        dateStrings.add("2023-01-26")
//        dateStrings.add("2023-01-21")

//        val dates: MutableList<Date> = ArrayList(dateStrings.size)

//        for (dateString in dateStrings) {
//            // sdf.parse(dateString) - convert the String into a Date accoring the pattern
//            // dates.add(...) - add the Date to the list
//            dates.add(sdf.parse(dateString))
//        }

            binding.calendar.init(sdf.parse("2023-01-01"), nextYear.time)
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE)
                .withSelectedDates(dates)

//        val sharedPreferences = getSharedPreferences("YOUR PREF KEY", Context.MODE_PRIVATE)
//        val c = Calendar.getInstance()
//
//        val thisDay = c[Calendar.DAY_OF_YEAR] // GET THE CURRENT DAY OF THE YEAR
//
//
//        val lastDay = sharedPreferences.getInt(
//            "YOUR DATE PREF KEY",
//            0
//        ) //If we don't have a saved value, use 0.
//
//
//        var counterOfConsecutiveDays = sharedPreferences.getInt(
//            "YOUR COUNTER PREF KEY",
//            0
//        ) //If we don't have a saved value, use 0.
//
//
//        if (lastDay == thisDay - 1) {
//            // CONSECUTIVE DAYS
//            counterOfConsecutiveDays = counterOfConsecutiveDays + 1
//            sharedPreferences.edit().putInt("YOUR DATE PREF KEY", thisDay)
//            sharedPreferences.edit().putInt("YOUR COUNTER PREF KEY", counterOfConsecutiveDays).commit()
//        } else {
//            sharedPreferences.edit().putInt("YOUR DATE PREF KEY", thisDay)
//            sharedPreferences.edit().putInt("YOUR COUNTER PREF KEY", 1).commit()
//        }


    }


    companion object{
         var dates: MutableList<Date> =ArrayList()
        val dateStrings: MutableList<String> = ArrayList()

    }
}

