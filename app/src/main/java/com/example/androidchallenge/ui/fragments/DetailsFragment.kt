package com.example.androidchallenge.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.androidchallenge.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val args : DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        val detail = args.itemDetail
        requireActivity().title = detail.name

        binding.tvFullName.text = detail.full_name
        binding.tvDescription.text = detail.details
        binding.tvRegion.text = detail.region
        binding.tvTimeZone.text = detail.timezone
        binding.tvWebsite.text = detail.website

        binding.tvWebsite.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(detail.website))
            startActivity(intent)
        }
        return binding.root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}