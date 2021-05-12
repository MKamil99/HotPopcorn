package com.example.hotpopcorn.view.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.hotpopcorn.databinding.FragmentCompanyDetailsBinding
import com.example.hotpopcorn.viewmodel.CompanyViewModel

class CompanyDetailsFragment : AbstractDetailsFragment() {
    // Binding fragment with ViewModel:
    private lateinit var companyVM : CompanyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        companyVM = ViewModelProvider(requireActivity()).get(CompanyViewModel::class.java)
    }

    // Binding Fragment with layout:
    private var _binding: FragmentCompanyDetailsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCompanyDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Displaying current company's data in TextViews and ImageView:
        companyVM.currentCompany.observe(viewLifecycleOwner, { currentCompany ->
            // Name:
            binding.tvCompanyName.text = currentCompany.name

            // HeadQuarters:
            if (!currentCompany.headquarters.isNullOrEmpty()) {
                binding.tvCompanyHQ.text = currentCompany.headquarters
                binding.tvCompanyHQ.visibility = View.VISIBLE
            } else binding.tvCompanyHQ.visibility = View.GONE

            // Origin country:
            if (!currentCompany.origin_country.isNullOrEmpty())
                binding.tvCompanyHQ.text = "${currentCompany.headquarters} (${currentCompany.origin_country})"

            // Homepage:
            if (!currentCompany.homepage.isNullOrEmpty()) {
                binding.tvHomepage.text = currentCompany.homepage
                binding.tvHomepage.visibility = View.VISIBLE
            } else binding.tvHomepage.visibility = View.GONE

            // Logo:
            if (!currentCompany.logo_path.isNullOrEmpty()) {
                val url = "https://image.tmdb.org/t/p/w185${currentCompany.logo_path}"
                Glide.with(binding.root).load(url).override(500, 200).fitCenter().into(binding.ivCompanyLogo)
                binding.ivCompanyLogo.visibility = View.VISIBLE
            } else binding.ivCompanyLogo.visibility = View.GONE
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}