package com.kodingwithkyle.template.authentication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kodingwithkyle.template.authentication.data.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE isSelf = 1")
    fun fetchSelf(): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)
}
