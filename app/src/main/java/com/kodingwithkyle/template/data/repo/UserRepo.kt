package com.kodingwithkyle.template.data.repo

import com.kodingwithkyle.template.data.dao.UserDao
import com.kodingwithkyle.template.data.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepo internal constructor(private val userDao: UserDao) {

    fun fetchSelf() = userDao.fetchSelf

    suspend fun insertUser(user: User) {
        withContext(Dispatchers.IO) {
            userDao.insertUser(user)
        }
    }

    suspend fun deleteUser(user: User) {
        withContext(Dispatchers.IO) {
            userDao.deleteUser(user)
        }
    }

    companion object {

        @Volatile
        private var instance: UserRepo? = null

        fun getInstance(userDao: UserDao) =
            instance ?: synchronized(this) {
                instance
                    ?: UserRepo(userDao).also { instance = it }
            }
    }
}