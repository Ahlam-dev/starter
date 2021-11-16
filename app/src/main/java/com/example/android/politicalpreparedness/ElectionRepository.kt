package com.example.android.politicalpreparedness

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ElectionRepository (private val database: ElectionDatabase) {

// get saved elections
   val followedElections: LiveData<List<Election>> = database.electionDao.getElectionList()

    suspend fun follow_Election(election: Election){
        withContext(Dispatchers.IO){
          //  val follow = FollowElection(election.id)
            database.electionDao.insertFollowedElection(election)
        }
    }
    suspend fun unfollow_Election(election: Election){
        withContext(Dispatchers.IO){
            database.electionDao.deleteElection(election.id)
        }
    }

    suspend fun isSaved(election: Election): Boolean{
        var isSavedElection = false
        withContext(Dispatchers.IO){
            val result = database.electionDao.getElectionById(election.id)
            isSavedElection = result != null
        }
        return isSavedElection
    }



}