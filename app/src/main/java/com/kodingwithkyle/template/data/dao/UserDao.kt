package com.kodingwithkyle.template.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kodingwithkyle.template.data.models.User

@Dao
interface UserDao {
    @get:Query("SELECT * FROM users WHERE isSelf = 1")
    val fetchSelf: LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)
}
