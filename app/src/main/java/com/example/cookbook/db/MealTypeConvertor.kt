package com.example.cookbook.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.jar.Attributes

@TypeConverters
class MealTypeConvertor {

    @TypeConverter
    fun fromAnyToString(attribute: Any?): String{
        return attribute?.toString() ?: ""
    }

    @TypeConverter
    fun fromStringToAny(attribute: String?): Any{
        return attribute ?: ""
    }
}