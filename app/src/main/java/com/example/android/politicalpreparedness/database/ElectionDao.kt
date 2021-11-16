package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    //TODO: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert_Elections(electionList: List<Election>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollowedElection(election: Election)




    //TODO: Add select all election query
    @Query("SELECT * FROM election_table")
    fun getElectionList(): LiveData<List<Election>>


    //TODO: Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :electionId")
    suspend fun getElectionById(electionId: Int) : Election?

    //TODO: Add delete query
    @Query("DELETE FROM election_table WHERE id = :electionId")
    suspend fun deleteElection(electionId: Int)

    //TODO: Add clear query
    @Query("DELETE FROM election_table")
    suspend fun clearElection()

}