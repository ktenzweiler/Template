package com.kodingwithkyle.template.authentication.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey @ColumnInfo(name = "_id") val _id: String,
    val email: String,
    val authToken: String?,
    var isSelf: Boolean = false
)