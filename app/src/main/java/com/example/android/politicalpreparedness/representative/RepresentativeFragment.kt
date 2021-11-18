package com.example.android.politicalpreparedness.representative

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.setNewValue
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import java.util.Locale
import java.util.Observer
import java.util.jar.Manifest

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentRepresentativeBinding

    companion object {
        //TODO: Add Constant for Location request
        const val REQUEST_LOCATION_PERMISSION = 1

    }

    //TODO: Declare ViewModel
    lateinit var viewModel:RepresentativeViewModel;
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(RepresentativeViewModel::class.java)

        //TODO: Establish bindings
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //TODO: Define and assign Representative adapter
        val representative_Adapter = RepresentativeListAdapter()
        binding.representativesRecycler.adapter = representative_Adapter

        //TODO: Populate Representative adapter
        viewModel.representativesList.
        observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                representative_Adapter.submitList(it)
            }
        })

        //TODO: Establish button listeners for field and location search
        binding.buttonLocation.setOnClickListener {
            getLocation()
        }



        viewModel.address.observe(viewLifecycleOwner,  androidx.lifecycle.Observer{
            it?.let {
                binding.state.setNewValue(it.state)
            }
        })
        binding.buttonSearch.setOnClickListener {
            viewModel.getRepresentatives()
            Toast.makeText(context,viewModel.address.value.toString(),Toast.LENGTH_SHORT).show()

            hideKeyboard()

        }
return  binding.root;
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults.isNotEmpty()  )
                getLocation()
        } else {
            Toast.makeText(requireContext(), "Please enable access to your location", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            //TODO: Request Location permissions
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf<String>(ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION)
            false
        }
    }

    private fun isPermissionGranted() : Boolean {
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        return ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address


        if (checkLocationPermissions()) {
            LocationServices.getFusedLocationProviderClient(requireContext())
                    .lastLocation.addOnSuccessListener {
                        viewModel.getAddressFromGeoLocation(geoCodeLocation(it))
                    }

        } else {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        } }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}