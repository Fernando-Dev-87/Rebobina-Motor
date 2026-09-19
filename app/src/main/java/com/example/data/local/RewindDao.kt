package com.example.data.local

import androidx.room.*
import com.example.data.model.RewindService
import kotlinx.coroutines.flow.Flow

@Dao
interface RewindDao {
    @Query("SELECT * FROM rewind_services ORDER BY dateTimestamp DESC")
    fun getAllServices(): Flow<List<RewindService>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: RewindService): Long

    @Delete
    suspend fun deleteService(service: RewindService)

    @Query("SELECT * FROM rewind_services WHERE id = :id")
    suspend fun getServiceById(id: Long): RewindService?
}
