package com.example.android.politicalpreparedness.election

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment: Fragment() {
    private val viewModel: ElectionsViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, ElectionsViewModelFactory(activity.application)).get(ElectionsViewModel::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
try{
        var binding =FragmentElectionBinding.inflate(
                inflater
                );

        binding.lifecycleOwner = this


        binding.viewModel = viewModel




        binding.upcomingRecycler.adapter = ElectionListAdapter(ElectionListener {
            viewModel.onElectionClicked(it)

        })
        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer {
           // binding.upcomingRecycler.submitList(it)
            it?.let {
                (binding.upcomingRecycler.adapter as ElectionListAdapter).submitList(it)
            }
        })

        binding.savedRecycler.adapter=ElectionListAdapter(ElectionListener {
            viewModel.onElectionClicked(it)
        })

        viewModel.followedElections.observe(viewLifecycleOwner ,  Observer {
            it?.let {
                (binding.savedRecycler
                .adapter as ElectionListAdapter).submitList(it)
            }
        })

      viewModel.navigateToSelectedElection.observe(viewLifecycleOwner, Observer{
            it?.let {
               this.findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it))
                viewModel.doneNavigation()


            }
        })
        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters
return binding.root
      } catch (e:Exception ) {
    Log.e(TAG, "onCreateView", e);
    throw e;
}
}

    //TODO: Refresh adapters when fragment loads

}