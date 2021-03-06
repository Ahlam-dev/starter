package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val election = VoterInfoFragmentArgs.fromBundle(arguments!!).argElection
        val activity = requireNotNull(this.activity)
        val viewModel = ViewModelProvider(this, VoterInfoViewModelFactory(activity.application, election)).get(VoterInfoViewModel::class.java)

        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.followButton.setOnClickListener {
            viewModel.onFollowButtonClicked();

        }
        viewModel.url.observe(viewLifecycleOwner, Observer {
            it?.let {

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            }
        })


        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks
        viewModel.isFollowed.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    binding.followButton.text = getString(R.string.unfollow_election)
                } else {
                    binding.followButton.text = getString(R.string.follow_election)
                }
            }
        })

        if (activity is AppCompatActivity) {
            activity.supportActionBar?.title = election.name
        }

        return binding.root
    }


}