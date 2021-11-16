package com.example.android.politicalpreparedness.election

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.ElectionRepository
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class VoterInfoViewModel(
         application: Application,val election: Election) : AndroidViewModel(application){

    //TODO: Add live data to hold voter info
   private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    var voteraddress= ""


    //TODO: Add var and methods to populate voter info
    fun getVoterInfo() {
        var state = election.division?.state
        var country = election.division?.country

       val address= "$state,$country";
        val Id = election.id
        viewModelScope.launch {
            try {
                _voterInfo.value = CivicsApi.retrofitService.getVoterinfo(address, Id)
            } catch (e: Exception) {
                Log.d(TAG, e.printStackTrace().toString())
            }
        }
    }

    //TODO: Add var and methods to support loading URLs
    var url = MutableLiveData<String>()
    fun loading_Urls(uri: String?) {
        url.value = uri
    }

    //TODO: Add var and methods to save and remove elections to local database
    private val electionDatabase = getInstance(application)
    private val electionRepository = ElectionRepository(electionDatabase)

    private val _isFollowed = MutableLiveData(false)
    val isFollowed: LiveData<Boolean>
        get() = _isFollowed

    fun onFollowButtonClicked() {

        viewModelScope.launch {
            withContext(Dispatchers.IO){
        if (_isFollowed.value == true) {
            electionRepository.unfollow_Election(election)
            _isFollowed.value=false
        } else {
                electionRepository.follow_Election(election)
            _isFollowed.value=true;
        }
            }
        }

    }
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */
init {
        viewModelScope.launch {
            withContext(Dispatchers.IO){

                getVoterInfo()
                voteraddress= voterInfo.value?.state!![0].electionAdministrationBody.correspondenceAddress?.toFormattedString().toString()

            }
        }


}
}