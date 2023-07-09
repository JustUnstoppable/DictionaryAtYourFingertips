package com.example.myvocabulary.streakDB

import androidx.room.TypeConverter
import java.util.*

class ConverterClass {
        @TypeConverter
        fun toDate(dateLong: Long?): Date? {
            return dateLong?.let { Date(it) }
        }

        @TypeConverter
        fun fromDate(date: Date?): Long? {
            return date?.time
        }
}