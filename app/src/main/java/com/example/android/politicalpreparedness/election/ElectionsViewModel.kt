package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.ElectionRepository
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(
                         application: Application) : AndroidViewModel(application){

    private val electionDatabase = getInstance(application)
    private val electionsRepository = ElectionRepository(electionDatabase)

    //Done: Create live data val for upcoming elections
    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() =_upcomingElections

    //Done: Create live data val for saved elections
    private val _followedElections = MutableLiveData<List<Election>>()
    val followedElections: LiveData<List<Election>>
        get() = _followedElections

    private val _navigateToSelectedElection = MutableLiveData<Election>()
    val navigateToSelectedElection: LiveData<Election>
        get() = _navigateToSelectedElection


    //TODO: Create live data val for saved elections
    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
init {
        viewModelScope.launch {
            displayUpcomingElections()
            _followedElections.value= electionsRepository.followedElections.value
        }

    }

    private fun displayUpcomingElections() {
        viewModelScope.launch {
            with(Dispatchers.IO){
                try {
                    _upcomingElections.value = CivicsApi.retrofitService.getElections().elections
                }catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            }

    }


    fun onElectionClicked( election: Election){
        _navigateToSelectedElection.value=election

    }
     fun doneNavigation(){
        _navigateToSelectedElection.value=null
    }
}