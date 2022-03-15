package com.example.af.db

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Created by mah on 2022/3/11.
 */

@Dao
interface HouseDao {
    @Query("SELECT * FROM House ORDER BY other1, price")
    fun getAll(): LiveData<List<House>>

    @Query("SELECT * FROM House WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): LiveData<House>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg houses: House)

    @Update
    fun update(vararg houses: House)

    @Delete
    fun delete(house: House)
}