package com.kodingwithkyle.template.authentication.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kodingwithkyle.template.authentication.data.models.User

@Dao
interface UserDao {
    @get:Query("SELECT * FROM users WHERE isSelf = 1")
    val fetchSelf: LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)
}
