package com.example.android.politicalpreparedness.representative

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel: ViewModel() {

    //TODO: Establish live data for representatives and address
    private val _representativeList = MutableLiveData<List<Representative>>()
    val representativesList: LiveData<List<Representative>>
        get() = _representativeList

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address
    //TODO: Create function to fetch representatives from API from a provided address


    public fun getRepresentatives() {
        viewModelScope.launch {

            try {
                val address = _address.value!!.toString()

                val (offices, officials)= CivicsApi.retrofitService.getRepresentatives(address)
                _representativeList.value = offices.flatMap { office ->
                            office.getRepresentatives(officials)
                        }


            } catch (e: Exception) {
                Log.i("Error Message", e.stackTrace.toString())
print("hi")
            }

        }

    }
    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location
    fun getAddressFromGeoLocation(address: Address) {
        _address.value = address

    }
    //TODO: Create function to get address from individual fields
    init {
        _address.value = Address("", "", "", "Alabama", "")
    }
}
