package com.example.android.politicalpreparedness.representative

import android.app.Application
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
import kotlin.coroutines.coroutineContext

class RepresentativeViewModel: ViewModel() {

    //TODO: Establish live data for representatives and address
    private val _representativeList = MutableLiveData<List<Representative>>()
    val representativesList: LiveData<List<Representative>>
        get() = _representativeList

   /* private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address*/
   private val address = MutableLiveData<Address?>()
    val addLine1 = MutableLiveData<String>()
    val addLine2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val zip = MutableLiveData<String>()
    //TODO: Create function to fetch representatives from API from a provided address


    public fun getRepresentatives(address: Address) {
        viewModelScope.launch {


                val response = CivicsApi.retrofitService.getRepresentatives(address.toFormattedString())
                val offices = response.offices
                val officials = response.officials

                val representativesList = mutableListOf<Representative>()

                offices.forEach { office ->
                    representativesList.addAll(office.getRepresentatives(officials))
                }
                _representativeList.value = representativesList

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
   // fun getAddressFromGeoLocation(address: Address) {
      //  _address.value = address

   // }



    fun getAddressFromGeoLocation(address: Address) {
        this.address.value = address
        addLine1.value = address.line1
        address.line2.let { addLine2.value = it }
        city.value = address.city
        state.value = address.state
        zip.value = address.zip
        getRepresentatives(address)
    }

    //Created function to get address from individual fields
    fun fetchRepresentatives() {
        val address = Address(addLine1.value!!, addLine2.value,  city.value!!, state.value!! ,  zip.value!!)
        getRepresentatives(address)
    }
    //TODO: Create function to get address from individual fields

}
