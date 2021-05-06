package com.example.hotpopcorn.view.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.hotpopcorn.databinding.FragmentCompanyDetailsBinding
import com.example.hotpopcorn.viewmodel.CompanyViewModel

class CompanyDetailsFragment : Fragment() {
    private var _binding: FragmentCompanyDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var companyVM : CompanyViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Binding fragment with layout and VM:
        _binding = FragmentCompanyDetailsBinding.inflate(inflater, container, false)
        companyVM = ViewModelProvider(requireActivity()).get(CompanyViewModel::class.java)

        // Displaying current company's data in TextViews and ImageView:
        companyVM.currentCompany.observe(viewLifecycleOwner, {
            // Name:
            binding.tvCompanyName.text = it.name

            // HeadQuarters:
            if (!it.headquarters.isNullOrEmpty())
            {
                binding.tvCompanyHQ.text = it.headquarters
                binding.tvCompanyHQ.visibility = View.VISIBLE
            }
            else binding.tvCompanyHQ.visibility = View.GONE

            // Origin country:
            if (!it.origin_country.isNullOrEmpty())
                binding.tvCompanyHQ.text = "${it.headquarters} (${it.origin_country})"

            // Homepage:
            if (!it.homepage.isNullOrEmpty())
            {
                binding.tvHomepage.text = it.homepage
                binding.tvHomepage.visibility = View.VISIBLE
            }
            else binding.tvHomepage.visibility = View.GONE

            // Logo:
            if (!it.logo_path.isNullOrEmpty())
            {
                val url = "https://image.tmdb.org/t/p/w185${it.logo_path}"
                Glide.with(binding.root).load(url).override(500, 200).fitCenter().into(binding.ivCompanyLogo)
                binding.ivCompanyLogo.visibility = View.VISIBLE
            }
            else binding.ivCompanyLogo.visibility = View.GONE
        })

        return binding.root
    }
}