package com.example.turecallerdialog.database.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.*
import com.example.turecallerdialog.model.ContactModel


@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<ContactModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repos: ContactModel)

    @Update(entity = ContactModel::class)
    suspend fun update(repos: ContactModel)

    @Query("SELECT * FROM contact WHERE isChecked LIKE :checked ORDER BY dummyName DESC")
    fun getNickNamedContact(checked : Boolean): PagingSource<Int, ContactModel>



    @Query("SELECT * FROM contact ORDER BY dummyName ASC")
    fun getAllContact(): LiveData<List<ContactModel>>



    @Query("SELECT * FROM contact WHERE phoneNumber LIKE '%' || :number || '%'")
    suspend fun getNickNamed(number : String): List<ContactModel>

    @Query("SELECT * FROM contact WHERE isChecked LIKE :checked AND phoneNumber LIKE '%' || :number || '%'")
    suspend fun getCheckedNumber(checked : Boolean,number: String): List<ContactModel>

    // @Query("SELECT * FROM Posts WHERE " +"name LIKE :queryString OR description LIKE :queryString " + "ORDER BY stars DESC, name ASC")
    /*@Query("SELECT * FROM Contact WHERE " +"type LIKE :queryString")
    fun reposByName(queryString: String): PagingSource<Int, ContactModel>*/

    @Query("DELETE FROM Contact")
    suspend fun clearRepos()
}